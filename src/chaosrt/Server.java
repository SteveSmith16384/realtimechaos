/*
    Realtime Chaos - Copyright (C) 2005 Stephen Carlyle-Smith

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
import java.io.*;
import java.util.*;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerCorpse;
import chaosrt.server.ServerMapSquare;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerWizard;
import chaosrt.server.frmServerOptionForm;
import chaosrt.spell.Spell;

public final class Server {

  // Server to Client commands - todo - move to S2C
  public static final byte NEW_MAP_II=1, OBJECT_UPDATE_IIIBIC=2, NEW_OBJECT_IIIC=3, PLAYER_NO_IC=4;
  public static final byte REMOVE_OBJECT_IC=5, SPELLBOOK_UPDATE_IIC=6, TEXT_BBSC=7, PLAYER_UPDATE_IC=8;
  public static final byte HIDE_OBJECT_IC=9, PLAYERS_NAME=10;
  public static final byte MAPSQUARE_UPDATE=11, EXPLOSION_IIIC=12, ATTACK_FLASH_IIC=13, OBJECT_TEXT=14;
  public static final byte SHOW_OBJECT=15, BULLET_FLASH=16, UNDEAD_FLASH_IC=17;
  public static final byte LIGHTENING_BOLT_IIIIIIIC=18, NEW_SIDE_II=19, FROZEN_IB=20, UNDEAD_IB=21;
  public static final byte ZONE_OF_DARKNESS_IIC=22, INVISIBLE_IB=23, CMD_SMOKE=24, CHANGE_FORM_IBTC=25;
  public static final byte NO_OF_PLAYERS = 26;
  
  // Client to server commands - todo - move to C2S
  public static final byte NAME=1, NEW_DESTINATION_IIIC=2, DISCONNECTING=3;
  public static final byte TARGET_ENEMY=5, CAST_SPELL_IIIB=6, INFORMATION_I=7;

  // Map and object types - the creature must match the CSV file
  public static final int GRASS=0, ROAD=1, WATER=2, TREE=3, MUD=4, WALL=5, BULLET=9;
  public static final int CORPSE=10, ORC=11, DWARF=12, WIZARD=13, MOUNTED_SKELETON=15;
  public static final int ARCHER=16, MUMMY=17, GOOEY_BLOB=18, KNIGHT=19, TROLL=20;
  public static final int OGRE=21, GHOST=22, MAGIC_FIRE=24, HYDRA=25, SHADOW_WOOD=26;
  public static final int DRAGON=27, PSIREN=28, VORTEX=29, DOPPLEGANGER=30;

  public static final byte MAX_OBJECTS=20, CHECK_BYTE=0;
  public static int ILLUSION_DIVIDE=4;

  public static NetworkServer2 net_server;
  public static ServerPlayer[] players;
  public static ServerObject objects[] = new ServerObject[MAX_OBJECTS];
  public static ServerMapSquare map[][];
  public static ArrayList spells_in_effect = new ArrayList();
  public static int map_width, map_height;
  public static boolean use_undead;

  public Server() {
    try {
      frmServerOptionForm options = new frmServerOptionForm();
      while (options.isVisible()) {
    	  Functions.delay(100);
      }
      if (options.ok_clicked == false) {
    	  System.exit(0);
      }
      /*String pl_text = JOptionPane.showInputDialog(null, "Please enter no of players.", Statics.TITLE, JOptionPane.INFORMATION_MESSAGE);
      if (pl_text == null) {
    	  System.exit(0);
      }*/
      
      use_undead = options.optUseUndead.isSelected();
      map_width = new Integer(options.txtMapWidth.getText()).intValue();
      map_height = new Integer(options.txtMapWidth.getText()).intValue();
      createMap();

      int human_players = new Integer(options.txtHumanPlayers.getText()).intValue();
      int computer_players = new Integer(options.txtComputerPlayers.getText()).intValue();
      int pl = human_players + computer_players;
      players = new ServerPlayer[pl];

      net_server = new NetworkServer2(Statics.PORT, pl);
      collectPlayers();
      S2CCommunication.SendGameData();
      System.out.println("Server started.");
      start_game();
    } catch (Exception ex) {
      System.err.println("Error: "+ex.getMessage());
      ex.printStackTrace();
    }
  }

  private void collectPlayers() throws IOException {
    System.out.println("Server waiting for " + players.length + " users...");
    net_server.acceptUsers();

    for (byte p = 0 ; p < players.length ; p++) {
      players[p] = new ServerPlayer(p);
    }

    for (byte p=0 ; p < players.length ; p++) {
      // Create players wizard
      ServerWizard wiz = new ServerWizard(Functions.rndDouble(
	  TileMapView.SQUARE_SIZE, (map_width-2) * TileMapView.SQUARE_SIZE),
					     Functions.
					     rndDouble(TileMapView.SQUARE_SIZE,
	  (map_height-2) * TileMapView.SQUARE_SIZE), p);
      wiz.addToMap();
      players[p].wizard = wiz;
    }
  }

  private void start_game() throws IOException {
		while (true) {
			long start = System.currentTimeMillis();

			// read inputs
			for (int p = 0; p < players.length; p++) {
				if (net_server.getConnection(p).getInput().available() > 0) {
					players[p].decodeData();
				}
			}

			// process players
			for (int o = 0; o < players.length; o++) {
				ServerPlayer player = players[o];
				player.process();
			}

			// process objects
			for (int o = 0; o < objects.length; o++) {
				if (objects[o] != null) {
					objects[o].process();
				}
			}

			// Process spells in effect
			for (int s = 0; s < this.spells_in_effect.size(); s++) {
				Spell spell = (Spell) this.spells_in_effect.get(s);
				spell.process();
			}

			long wait = Statics.LOOP_DELAY - System.currentTimeMillis() + start;
			Functions.delay(wait);
		}
	}

  public static ServerMapSquare getMapSquareFromPixelCoords(double x, double y) throws IOException {
    x = x / TileMapView.SQUARE_SIZE;
    y = y / TileMapView.SQUARE_SIZE;

    if (x<0 || x>=map_width || y<0 || y>=map_height) {
      return new ServerMapSquare((byte)0, (byte)0, Server.WALL, 0);
    } else {
      return map[(int)x][(int)y];
    }
  }

  public static int GetNextFreeSlot() {
    for(int o=0 ; o<objects.length ; o++) {
      if (objects[o] == null) {
      	return o;
      }
    }
    IncreaseArray(objects.length + Server.MAX_OBJECTS);
    return GetNextFreeSlot();
  }

  public static void SendMessage(int side, String text) throws IOException {
    SendMessage(side, (byte)0, text);
  }

  public static void SendMessage(int side, byte priority, String text) throws IOException {
    net_server.getConnection(side).getOutput().writeByte(Server.TEXT_BBSC);
    net_server.getConnection(side).getOutput().writeByte(priority);
    net_server.getConnection(side).getOutput().writeByte(text.length());
    net_server.getConnection(side).getOutput().writeBytes(text);
    net_server.getConnection(side).getOutput().writeByte(Server.CHECK_BYTE);
  }

  public static void SendMessageToAll(String text) throws IOException {
    SendMessageToAll(text, (byte)0);
  }

  public static void SendMessageToAll(String text, byte priority) throws IOException {
    for (byte p = 0; p < players.length ; p++) {
	SendMessage(p, priority, text);
    }
  }

  public static void IncreaseArray(int at_least) {
    System.out.println("Increasing array.");
    ServerObject new_array[] = new ServerObject[at_least+MAX_OBJECTS];
    System.arraycopy(objects, 0, new_array, 0, objects.length);
    objects = new_array;
  }

  private void createMap() throws IOException {
    map = new ServerMapSquare[map_width][map_height];
    for (int z = 0; z < map_height; z++) {
      for (int x = 0; x < map_width; x++) {
    	  map[x][z] = new ServerMapSquare(x, z, Server.ROAD, 0);
	
	//int a = Functions.rnd(1, 80);
	  //map[x][z].setType(Server.MUD, false);
/*	} else if (a == 4) {
	  //map[x][z].setType(Server.WALL, false);
	} else if (a == 5 || a == 6 || a == 7) {
	  //map[x][z].setType(Server.ROAD, false);
	}
	else {
	  map[x][z].setType(Server.GRASS, false);
	}*/
      }
    }

/*    for (int z = 0; z < 5; z++) {
      this.addLake(WIDTH / 2, HEIGHT / 2);
    }
    //todo - add roads
*/
  }

  private void addLake(int map_x, int map_y) throws IOException {
    for (int z = 0; z < 20; z++) {
      if (isInsideMap(map_x, map_y) == true) {
	map[map_x][map_y].setType(Server.WATER, false);
      }
      map_x = map_x + Functions.rnd( -1, 1);
      map_y = map_y + Functions.rnd( -1, 1);
    }
  }

  public boolean isInsideMap(int map_x, int map_y) {
    return (map_x >= 1 & map_x < map_width - 1 & map_y >= 1 & map_y < map_height - 1);
  }

  public static ServerObject GetObjectAtPosition(int x, int y, boolean ignore_corpse) {
	  // Select objects
	  for (int o = 0; o < objects.length; o++) {
		  if (objects[o] != null) {
			  ServerObject obj = objects[o];
			  if (ignore_corpse == false || obj instanceof ServerCorpse == false) {
				  if (obj.rect.contains(x, y)) {
					  return obj;
				  }
			  }
		  }
	  }
	  return null;
  }
  
  public static void Quit() {
      net_server.closeAll();
    System.exit(0);
  }

  //****************************
  public static void main(String[] args) {
      new Server();
  }

}
