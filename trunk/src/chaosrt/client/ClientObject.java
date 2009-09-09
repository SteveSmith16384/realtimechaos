package chaosrt.client;

import java.awt.*;
import java.io.*;

import chaosrt.Client;
import chaosrt.Server;
import chaosrt.Statics;
import chaosrt.StatsFile;

public class ClientObject {

  protected final Client client;
  public int no;
  public int angle_deg, side, health=0;
  public boolean selectable, do_not_draw = false, under_object = false, frozen=false, invisible=false;
  public boolean undead=false;
  public String filename;
  public Rectangle rect;
  private String text = new String(""); // For the message that floats above them.

  /**
   * This constructor is for normal objects that load from the stats file.
   */
  public ClientObject(Client client, int type, int no, int side) throws IOException {
    rect = new Rectangle(-1000, -1000, 0, 0);
    StatsFile.LoadClientStats(this, type);
    this.side = side;
    this.client = client;
    this.no = no;
    Client.view.addObject(this, no);
  }

  /**
   * This constructor is for f/x that don't load from the stats file.
   */
  public ClientObject(Client client, int x, int y) {
    rect = new Rectangle(x, y, 0, 0);
    this.client = client;
    // This does not need to be added to the objects file!
    // It gets added to other_objects!
  }

  public void setDestination(int x, int y) throws IOException {
      client.net_client.getOutput().writeByte(Server.NEW_DESTINATION_IIIC);
      client.net_client.getOutput().writeInt(this.no);
      client.net_client.getOutput().writeInt(x);
      client.net_client.getOutput().writeInt(y);
      client.net_client.getOutput().writeByte(Server.CHECK_BYTE);
  }

  public void setTarget(int enemy_no) throws IOException {
      client.net_client.getOutput().writeByte(Server.TARGET_ENEMY);
      client.net_client.getOutput().writeInt(this.no);
      client.net_client.getOutput().writeInt(enemy_no);
  }

  public void draw(Graphics g) {
	  if (Statics.DEMO) {
		  if (this.filename.equalsIgnoreCase("orc")) {
			  int xzz=0;
		  }
	  }
	  // Do not draw if set so
	  if (do_not_draw) {
		  return;
	  }
	  
	  int x = this.rect.x - Client.view.x_pos;
	  int y = this.rect.y - Client.view.y_pos;
	  
	  if (x>=-rect.width && x <= Client.view.getWidth()) {
		  if (y >=-rect.height && y <= Client.view.getHeight()) {
			  if (this.filename.length() > 0) {
				  g.drawImage(Client.GetImage(this.filename + ".gif"), x, y, Client.view);
				  this.drawExtra(g, x, y);
			  }
			  // Draw text
			  if (text.length() > 0) {
				  g.setColor(Client.GetPlayerColour(side));
				  g.drawString(text, x, y);
			  }
			  manualDraw(g, x, y);
		  }
	  }
  }
  
  protected void manualDraw(Graphics g, int x, int y) {
	  // override if reqd.
  }
  
  /**
   * This can be over-ridden to draw anything special
   */
  protected void drawExtra(Graphics g, int x, int y) {
    if (this.undead) {
      g.setColor(Color.darkGray);
      g.drawRect(x, y, this.rect.width, this.rect.height);
    }
    if (this.frozen) {
      g.setColor(Color.cyan.brighter());
      g.drawRect(x, y, this.rect.width, this.rect.height);
    }
    if (this.invisible) {
      g.setColor(Color.white);
      g.drawLine(x, y, x, y+15);
    }
  }

  public void setText(String msg) {
    text = msg;
  }

  public boolean isInPoint(int pixel_x, int pixel_y) {
    if (rect.contains(pixel_x, pixel_y)) {
	return true;
    } else {
      return false;
    }
  }

}
