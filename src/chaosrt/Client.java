/*
    Realtime Chaos - Copyright (C) 2007 Stephen Carlyle-Smith

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package chaosrt;

import ssmith.lang.*;
import ssmith.net.*;
import ssmith.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import chaosrt.client.C2SCommunication;
import chaosrt.client.ClientAttackFlash;
import chaosrt.client.ClientBulletFlash;
import chaosrt.client.ClientExplosion;
import chaosrt.client.ClientMapSquare;
import chaosrt.client.ClientMessageLog;
import chaosrt.client.ClientObject;
import chaosrt.client.frmClientOptionForm;
import chaosrt.client.ClientSmoke;
import chaosrt.client.ClientUndeadFlash;
import chaosrt.client.ClientWizard;
import chaosrt.client.ClientZoneOfDarkness;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.spell.Spell;
import java.util.*;
import java.awt.image.*;

public final class Client extends EZFrame {

	// Keys
	private static final int KEY_NEXT=78, KEY_PREV=80, KEY_CAST=32, KEY_ILLUSION=73;

	// Client Settings
	private static final int SCROLL_SPEED=15, MAP_MULT=4, MAP_COORDS=30, MIN_SELECTION_SIZE=20;
	private static final int MAX_MESSAGES=15;

	private static Font font = new Font("Impact", Font.PLAIN, 12);

	public NetworkClient2 net_client;
	private BufferStrategy BS;
	private static ImageCache imgCache;
	public static TileMapView view;
	private Image map_image;
	public static int side;
	private boolean keys[] = new boolean[255];
	public boolean illusion = false;//, examine = false;
	public static ArrayList spellbook = new ArrayList();
	public java.util.List selected_objects = Collections.synchronizedList(new ArrayList());
	public ArrayList other_graphics = new ArrayList();
	public static int mana=0, curr_spell=0;
	private boolean mouse_is_down = false; // ie. Should we draw the drag-square?
	private int mouse_start_x, mouse_start_y;
	public int mouse_end_y, mouse_end_x;
	private ClientMessageLog messages = new ClientMessageLog(MAX_MESSAGES);
	public static String[] player_names;
	private String player_name;

	public Client() {
		super(640, 480, Statics.TITLE);
		frmClientOptionForm options = new frmClientOptionForm();
		options.setVisible(true);
		while (options.isVisible()) {
			Functions.delay(1000);
		}
		if (options.ok_clicked) {
			String server = options.txtIPAddress.getText();
			player_name = options.txtName.getText();
			net_client = new NetworkClient2(Statics.PORT);

			int try_again = JOptionPane.YES_OPTION;
			while (try_again == JOptionPane.YES_OPTION) {
				try {
					net_client.connect(server);
					try_again = JOptionPane.NO_OPTION;
				}
				catch (UnknownHostException e) {
					try_again = JOptionPane.showConfirmDialog(this,
							"Error: " + e.getMessage() +
					"\nDo you wish to try again?");
				}
				catch (IOException e) {
					try_again = JOptionPane.showConfirmDialog(this,
							"Error: " + e.getMessage() +
							"\nDo you wish to try again?",
							"Error connecting",
							JOptionPane.YES_NO_OPTION);
				}
			}
			
			if (net_client.connected == false) {
				System.exit(0);
			}

			try {
				imgCache = new ImageCache(this);
				view = new TileMapView(this);

				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				this.setLocation(0, 0);
				this.setSize(d.width, d.height);
				this.setVisible(true);
				this.createBufferStrategy(2);
				BS = this.getBufferStrategy();
				view.setSize(this.getWidth(), this.getHeight());
				start_game();
			} catch (IOException e) {
				System.err.println("Error:" +e.getMessage());
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Error:" +e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Error:" +e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.exit(0);
		}
	}

	public void start_game() throws IOException {
		while (this.isVisible()) {
			long start = System.currentTimeMillis();

			// Read data
			if (net_client.getInput().available() > 0) {
				decodeData();
			}

			processKeys();

			Graphics g = BS.getDrawGraphics();
			view.draw(g);
			drawOnScreen(g);
			BS.show();

			long wait = Statics.LOOP_DELAY - System.currentTimeMillis() + start;
			Functions.delay(wait);
		}
		this.quit();
	}

	private void processKeys() {
		// Process keys
		if (keys[104] == true || keys[38] == true) { // UP
			view.y_pos -= SCROLL_SPEED;
		}
		else if (keys[98] == true || keys[40] == true) { // Down
			view.y_pos += SCROLL_SPEED;
		}

		if (keys[102] == true || keys[39] == true) { // Right
			view.x_pos += SCROLL_SPEED;
		}
		else if (keys[100] == true || keys[37] == true) { // Left
			view.x_pos -= SCROLL_SPEED;
		}
	}

	private void drawOnScreen(Graphics g) {
		//Graphics2D g2 = (Graphics2D)g;

		// Draw map
		if (map_image != null) {
			g.drawImage(map_image, MAP_COORDS, MAP_COORDS, this);
		}

		// Draw overlaid graphics
		for (int i = 0; i < this.other_graphics.size(); i++) {
			ClientObject obj = (ClientObject)other_graphics.get(i);
			obj.draw(g);
		}

		ClientObject obj;

		// Draw our objects onto the map
		for(int o=0 ; o<view.objects.length ; o++) {
			obj = (ClientObject)view.getObject(o);
			if (obj != null) {
				int x = obj.rect.x / TileMapView.SQUARE_SIZE;
				int y = obj.rect.y / TileMapView.SQUARE_SIZE;
				if (obj.side == this.side) {
					g.setColor(Color.yellow);
					g.fillRect(MAP_COORDS + (x * MAP_MULT), MAP_COORDS + (y * MAP_MULT), MAP_MULT, MAP_MULT);
				}
			}
		}

		// Draw selector
		if (mouse_is_down) {
			g.setColor(Color.red);
			int x = Math.min(mouse_start_x, mouse_end_x);
			int y = Math.min(mouse_start_y, mouse_end_y);
			int w = Math.max(mouse_start_x, mouse_end_x) - x;
			int h = Math.max(mouse_start_y, mouse_end_y) - y;
			g.drawRect(x - view.x_pos, y - view.y_pos, w, h);
		}

		// Draw box round selected objects
		if (this.selected_objects.size() > 0) {
			for(int o=0 ; o < selected_objects.size() ; o++) {
				obj = (ClientObject)selected_objects.get(o);
				g.setColor(Color.red);
				g.drawRect(obj.rect.x - view.x_pos, obj.rect.y - view.y_pos, obj.rect.width,
						obj.rect.height);
			}
		}

		// Draw any that the mouse is over
		obj = view.getObjectAtPoint(this.mouse_end_x - view.x_pos, this.mouse_end_y - view.y_pos);
		if (obj != null) {
			if (obj.side == this.side && obj.selectable) {
				g.setColor(Color.magenta);
				g.drawRect(obj.rect.x - view.x_pos, obj.rect.y - view.y_pos,
						obj.rect.width,
						obj.rect.height);
			}
		}

		// On screen info
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString("Mana: " + mana, 200, 60);
		if (curr_spell >= 0 && curr_spell < spellbook.size()) {
			Spell spl = (Spell)spellbook.get(curr_spell);
			g.drawString("Current Spell: " + spl.name + " ("+spl.cost+")", 200, 80);
		}
		if (illusion) {
			g.drawString("ILLUSION", 200, 100);
		}
		messages.draw(g, this.getWidth()-200, 50);

		// List spells
		/*g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
	  g2.setColor(Color.LIGHT_GRAY);
	  g2.fillRect(40, 165, 150, spellbook.size() * 17);
		 */
		g.setColor(Color.white);
		for (int s=0 ; s<spellbook.size() ; s++) {
			Spell spl = (Spell)spellbook.get(s);
			if (s == this.curr_spell) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.WHITE);
			}
			g.drawString(s + "- " + spl.name, 50, 175 + (s*17));

		}

		// Players names
		/*if (player_names != null) {
	   for (int z = 0; z < player_names.length; z++) {
	   if (player_names[z] != null) {
	   g.setColor(Server.GetPlayerColour(z));
	   g.drawString(player_names[z], 20, 60 + (z * 20));
	   }
	   }
	   }*/

	}

	private void decodeData() throws IOException {
		while (net_client.getInput().available() > 0) {
			//int bytes = client.getInput().available();
			//this.setTitle("Bytes: "+client.getInput().available());
			byte b = net_client.getInput().readByte();
			if (b == Server.NEW_MAP_II) {
				int map_width = net_client.getInput().readInt();
				int map_height = net_client.getInput().readInt();
				view.setMapSize(map_width, map_height);
				map_image = this.createImage(map_width * MAP_MULT, map_height
						* MAP_MULT);
			} else if (b == Server.NO_OF_PLAYERS) {
				S2CCommunication.DecodeGameData(this);
				// Now send our name
				net_client.getOutput().writeByte(Server.NAME);
				net_client.getOutput().writeByte(player_name.length());
				net_client.getOutput().writeBytes(player_name);

			} else if (b == Server.OBJECT_UPDATE_IIIBIC) {
				int no = net_client.getInput().readInt();
				ClientObject obj = view.getObject(no);
				if (obj == null) {
					System.out.println("Error - updating null object " + no);
					// Swallow up the data anyway
					net_client.getInput().readInt();
					net_client.getInput().readInt();
					net_client.getInput().readByte();
					net_client.getInput().readInt();
				} else {
					int new_x = net_client.getInput().readInt();
					int new_y = net_client.getInput().readInt();
					obj.rect.x = new_x;
					obj.rect.y = new_y;
					obj.angle_deg = net_client.getInput().readByte() * 10;
					obj.health = net_client.getInput().readInt();
				}
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error in checksum on object update.");
				}
			} else if (b == Server.NEW_OBJECT_IIIC) {
				int no = net_client.getInput().readInt();
				int type = net_client.getInput().readInt();
				int side = net_client.getInput().readInt();
				//ClientObject o;
				if (type == Server.WIZARD) {
					new ClientWizard(this, no, side);
				} else {
					new ClientObject(this, type, no, side);
				}
				//ClientObject.AddClientObject(this, type, no, side);
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on new object.");
				}
			} else if (b == Server.REMOVE_OBJECT_IC) {
				int no = net_client.getInput().readInt();
				// See if its the selected object
				if (selected_objects.contains(view.getObject(no))) {
					selected_objects.remove(view.getObject(no));
				}
				view.removeObject(no);
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on remove object.");
				}
			} else if (b == Server.HIDE_OBJECT_IC) {
				int no = net_client.getInput().readInt();
				view.hideObject(no, true);
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on hide object.");
				}
			} else if (b == Server.SHOW_OBJECT) {
				int no = net_client.getInput().readInt();
				view.hideObject(no, false);
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on hide object.");
				}
			} else if (b == Server.PLAYER_NO_IC) {
				side = net_client.getInput().readInt();
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on player no.");
				}
			} else if (b == Server.TEXT_BBSC) {
				byte priority = net_client.getInput().readByte();
				byte length = net_client.getInput().readByte();
				byte ch[] = new byte[length];

				// Wait until enough bytes are avalable
				while (net_client.getInput().available() < length) {
					//System.out.println("Waiting..." + client.getInput().available()); 
				}

				net_client.getInput().read(ch, 0, length);
				messages.addMessage(new String(ch), priority);
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on msg receive.");
				}
			} else if (b == Server.OBJECT_TEXT) {
				int no = net_client.getInput().readInt();
				byte length = net_client.getInput().readByte();
				byte ch[] = new byte[length];

				// Wait until enough bytes are avalable
				while (net_client.getInput().available() < length) {
					//System.out.println("Waiting..." + client.getInput().available()); 
				}

				net_client.getInput().read(ch, 0, length);
				view.getObject(no).setText(new String(ch));
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on object msg receive.");
				}
			} else if (b == Server.CHANGE_FORM_IBTC) {
				int no = net_client.getInput().readInt();
				byte length = net_client.getInput().readByte();
				byte ch[] = new byte[length];

				// Wait until enough bytes are avalable
				while (net_client.getInput().available() < length) {
					/*	System.out.println("Waiting..."
							+ client.getInput().available());*/
				}

				net_client.getInput().read(ch, 0, length);
				view.getObject(no).filename = new String(ch);
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on change form.");
				}
			} else if (b == Server.PLAYER_UPDATE_IC) {
				mana = net_client.getInput().readInt();
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on player update.");
				}
			} else if (b == Server.NEW_SIDE_II) {
				view.objects[net_client.getInput().readInt()].side = net_client
				.getInput().readInt();
			} else if (b == Server.FROZEN_IB) {
				view.objects[net_client.getInput().readInt()].frozen = net_client
				.getInput().readBoolean();
			} else if (b == Server.INVISIBLE_IB) {
				int n = net_client.getInput().readInt();
				ClientObject obj = view.objects[n];
				if (obj != null) {
					obj.invisible = net_client.getInput().readBoolean();
				}
			} else if (b == Server.UNDEAD_IB) {
				view.objects[net_client.getInput().readInt()].undead = net_client
				.getInput().readBoolean();
			} else if (b == Server.SPELLBOOK_UPDATE_IIC) {
				int no_of_spells = net_client.getInput().readInt();
				spellbook.clear();
				for (int s = 0; s < no_of_spells; s++) {
					Spell spell = Spell.SpellFactory(net_client.getInput()
							.readInt());
					//System.out.println("Spell received: "+spell.name);
					spellbook.add(spell);
				}
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on spell update.");
				}
			} else if (b == Server.PLAYERS_NAME) {
				S2CCommunication.DecodeNameFromServer(this);
			} else if (b == Server.MAPSQUARE_UPDATE) {
				int x = net_client.getInput().readInt();
				int z = net_client.getInput().readInt();
				int type = net_client.getInput().readInt();
				Graphics g = map_image.getGraphics();
				ClientMapSquare sq;
				if (type == Server.GRASS) {
					sq = new ClientMapSquare(this, x, z, "grass");
					g.setColor(Color.GREEN);
				} else if (type == Server.WALL) {
					sq = new ClientMapSquare(this, x, z, "wall");
					g.setColor(Color.BLACK);
				} else if (type == Server.ROAD) {
					sq = new ClientMapSquare(this, x, z, "road");
					g.setColor(Color.gray);
				} else if (type == Server.MUD) {
					sq = new ClientMapSquare(this, x, z, "mud");
					g.setColor(Color.darkGray);
				} else if (type == Server.TREE) {
					sq = new ClientMapSquare(this, x, z, "tree");
					g.setColor(Color.BLACK);
				} else if (type == Server.WATER) {
					sq = new ClientMapSquare(this, x, z, "water");
					g.setColor(Color.cyan);
				} else {
					sq = new ClientMapSquare(this, x, z, "grass");
					System.out.println("Unknown mapsquare type " + type);
				}
				view.setMapSquare(x, z, sq);

				g.fillRect(x * MAP_MULT, z * MAP_MULT, MAP_MULT, MAP_MULT);

				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err
					.println("Error on checksum on mapsquare update.");
				}
			} else if (b == Server.EXPLOSION_IIIC) {
				int x = net_client.getInput().readInt();
				int y = net_client.getInput().readInt();
				int size = net_client.getInput().readInt();
				this.other_graphics.add(new ClientExplosion(this, x, y, size));
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on explosion.");
				}
			} else if (b == Server.ATTACK_FLASH_IIC) {
				ClientObject a2 = view.getObject(net_client.getInput().readInt());
				ClientObject b2 = view.getObject(net_client.getInput().readInt());
				if (a2 != null && b2 != null) {
					this.other_graphics.add(new ClientAttackFlash(this,
							(a2.rect.x + b2.rect.x) / 2,
							(a2.rect.y + b2.rect.y) / 2));
				}
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on attack flash.");
				}
			} else if (b == Server.LIGHTENING_BOLT_IIIIIIIC) {
				S2CCommunication.DecodeLighteningBolt(this);
			} else if (b == Server.ZONE_OF_DARKNESS_IIC) {
				int x1 = net_client.getInput().readInt();
				int y1 = net_client.getInput().readInt();
				this.other_graphics.add(new ClientZoneOfDarkness(this, x1, y1));
				byte check = net_client.getInput().readByte();
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on lightening bolt.");
				}
			} else if (b == Server.UNDEAD_FLASH_IC) {
				int a3 = net_client.getInput().readInt();
				byte check = net_client.getInput().readByte();
				this.other_graphics.add(new ClientUndeadFlash(this, this.view
						.getObject(a3)));
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on undead flash.");
				}
			} else if (b == Server.CMD_SMOKE) {
				int x = net_client.getInput().readInt();
				int y = net_client.getInput().readInt();
				byte check = net_client.getInput().readByte();
				this.other_graphics.add(new ClientSmoke(this, x, y));
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on smoke.");
				}
			} else if (b == Server.BULLET_FLASH) {
				int x = net_client.getInput().readInt();
				int y = net_client.getInput().readInt();
				byte check = net_client.getInput().readByte();
				this.other_graphics.add(new ClientBulletFlash(this, x, y));
				if (check != Server.CHECK_BYTE) {
					System.err.println("Error on checksum on explosion.");
				}
			} else {
				System.err.println("Unknown data type: " + b + " (" + (char) b
						+ ")");
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
				curr_spell = e.getKeyCode() - 48;
				if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK) {
					curr_spell = curr_spell + 10;
				}
				if (curr_spell >= spellbook.size()) {
					curr_spell = spellbook.size() - 1;
				}
			} else if (e.getKeyCode() == KEY_NEXT) { // Next
				if (curr_spell + 1 < spellbook.size()) {
					this.curr_spell++;
				}
			} else if (e.getKeyCode() == KEY_PREV) { // Prev
				if (curr_spell > 0) {
					this.curr_spell--;
				}
			} else if (e.getKeyCode() == KEY_CAST) { // Space (cast)
				C2SCommunication.castCurrentSpell(this);
			} else if (e.getKeyCode() == KEY_ILLUSION) { // Illusion
				this.illusion = !illusion;
			}
			/*
			 * else if (e.getKeyCode() == KEY_EXAMINE) { this.examine =
			 * !examine; }
			 */
			else { // Normal key press
				//System.out.println("Key: "+e.getKeyCode());
				keys[e.getKeyCode()] = true;
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void keyReleased(KeyEvent e) {
		try {
			keys[e.getKeyCode()] = false;
		} catch (Exception ex) {
			System.out.println("Error: "+ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void mousePressed(MouseEvent e) {
		try {
			// Have they clicked on the map?
			if (map_image != null) {
				if (e.getX() >= MAP_COORDS && e.getY() >= MAP_COORDS) {
					if (e.getX() <= MAP_COORDS + map_image.getWidth(this) &&
							e.getY() <= MAP_COORDS + map_image.getHeight(this)) {
						int x = (e.getX() - MAP_COORDS) / MAP_MULT;
						int y = (e.getY() - MAP_COORDS) / MAP_MULT;
						view.x_pos = (x * TileMapView.SQUARE_SIZE) - (view.getWidth() / 2);
						view.y_pos = (y * TileMapView.SQUARE_SIZE) - (view.getHeight() / 2);
						return;
					}
				}
			}

			if (e.getButton() == MouseEvent.BUTTON1) {
				mouse_is_down = true;
				mouse_start_x = e.getX() + view.x_pos;
				mouse_start_y = e.getY() + view.y_pos;
				mouse_end_x = mouse_start_x;
				mouse_end_y = mouse_start_y;
			} else { // RMB
				ClientObject obj = (ClientObject) view.getObjectAtPoint(e.getX(), e.getY());
				if (obj == null) { // Clear all selection
					selected_objects.clear();
				} else {
					// Get information
					net_client.getOutput().writeByte(Server.INFORMATION_I);
					net_client.getOutput().writeInt(obj.no);
				}
			}
		} catch (IOException ex) {
			System.out.println("Error: "+ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void mouseReleased(MouseEvent e) {
		//System.out.println("Mouse released.");
		try {
			// Have they clicked on the map
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (mouse_is_down) { // ie. were we just on the map when we clicked
					mouse_is_down = false;
					mouse_end_x = e.getX() + view.x_pos;
					mouse_end_y = e.getY() + view.y_pos;

					int x = Math.min(mouse_start_x, mouse_end_x);
					int y = Math.min(mouse_start_y, mouse_end_y);
					int w = Math.max(mouse_start_x, mouse_end_x) - x;
					int h = Math.max(mouse_start_y, mouse_end_y) - y;
					if (w > MIN_SELECTION_SIZE || h > MIN_SELECTION_SIZE) { // Have they dragged?
						selected_objects = view.getObjectsInRect(x, y, w, h);
						// Remove any not on our side
						ClientObject obj;
						for (int o = 0; o < selected_objects.size(); o++) {
							obj = (ClientObject) selected_objects.get(o);
							if (obj.side != this.side) {
								selected_objects.remove(obj);
								o--; // So we don't skip one!
							}
						}

					}
					else { // Not dragged at all
						ClientObject obj = (ClientObject) view.getObjectAtPoint(e.getX(), e.getY());
						if (obj == null) { // Not selected an object
							// Must be selecting a destination
							if (selected_objects.size() > 0) {
								x = e.getX() + view.x_pos;
								y = e.getY() + view.y_pos;
								// Set dest for all selected objects
								Point p = getCentreOfSelectionGroup();
								for (int o = 0; o < selected_objects.size(); o++) {
									obj = (ClientObject) selected_objects.get(o);
									int x_off = obj.rect.x - p.x;
									int y_off = obj.rect.y - p.y;
									obj.setDestination(x+x_off, y+y_off);
								}
								//System.out.println("Setting dest: "+p.x + "/"+p.y);
							}
						} else { // Selected indiv object
							if (obj.side == this.side) {
								if (e.getClickCount() == 1) { // Selected an object
									this.selected_objects.clear();
									this.selected_objects.add(obj);
								}
							}
							else { // Selected an enemy to target!
								// Set target for all selected objects
								for (int o = 0; o < selected_objects.size(); o++) {
									ClientObject selected_obj = (ClientObject) selected_objects.get(o);
									selected_obj.setTarget(obj.no);
								}
							}
						}
					}
				}
			}
			else if (e.getButton() == MouseEvent.BUTTON2) {
				// Do nothing - all in mouse pressed
			}
		} catch (IOException ex) {
			System.out.println("Error: "+ex.getMessage());
			ex.printStackTrace();
		}
	}

	public Point getCentreOfSelectionGroup() {
		int l=9999, r=0, t=9999, b=0;
		ClientObject obj;
		for (int o=0 ; o<selected_objects.size() ; o++) {
			obj = (ClientObject) selected_objects.get(o);
			if (obj.rect.x < l) {
				l = obj.rect.x;
			}
			if (obj.rect.x > r) {
				r = obj.rect.x;
			}
			if (obj.rect.y < t) {
				t = obj.rect.y;
			}
			if (obj.rect.y > b) {
				b = obj.rect.y;
			}
		}
		return new Point((l+r)/2, (t+b)/2);
	}


	public void mouseMoved(MouseEvent e) {
		//System.out.println("Mouse moved.");
		mouse_end_x = e.getX() + view.x_pos;
		mouse_end_y = e.getY() + view.y_pos;
	}

	public void mouseDragged(MouseEvent e) {
		//System.out.println("Mouse dragged.");
		mouse_end_x = e.getX() + view.x_pos;
		mouse_end_y = e.getY() + view.y_pos;
	}

	/*  public void mouseClicked(MouseEvent evt) {
    if (evt.getClickCount() == 2) {
      System.out.println("Double-click");
      ClientObject obj = (ClientObject)view.getObjectAtPoint(evt.getX() + view.x_pos, evt.getY() + view.y_pos);
      if (obj != null) {
	if (obj.type == Server.APC) {
	  if (selected_objects.size() > 0) {
	    // Send "get into APC"
	    ClientObject order_obj;
	    for (int o = 0; o < selected_objects.size(); o++) {
	      order_obj = (ClientObject) selected_objects.get(o);
	      // Set dest as APC first
	      order_obj.setDestination(obj.x_pos, obj.y_pos);
	      try {
		client.getOutput().writeByte(Server.ENTER_APC);
		client.getOutput().writeInt(order_obj.no);
	      } catch (Exception ex) {

	      }
	    }
	  } else {
	    // Send "get out of APC"
	    try {
	      client.getOutput().writeByte(Server.EXIT_APC);
	      client.getOutput().writeInt(obj.no);
	    } catch (Exception ex) {

	    }
	  }
	}
      }
    }
  }
	 */
	public void windowClosing(WindowEvent e) {
		quit();
	}

	public void quit() {
		this.setVisible(false);
		try {
			net_client.getOutput().writeByte(Server.DISCONNECTING);
			net_client.close();
		} catch (IOException e) {
			System.err.println("Error:" +e.getMessage());
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static Color GetPlayerColour(int side) {
		if (side == 0) {
			return Color.cyan;
		} else if (side == 1) {
			return Color.red;
		} else if (side == 2) {
			return Color.yellow;
		} else if (side == 3) {
			return Color.cyan;
		} else if (side == -1) {
			return Color.white;
		} else {
			System.err.println("Running out of colours for players! ("+side+")");
			return Color.white;
		}
	}

	public static Image GetImage(String filename) {
		return imgCache.getImage("images/" + filename);
	}

	//*******************************
	public static void main(String[] args) {
		new Client();
	}

}

