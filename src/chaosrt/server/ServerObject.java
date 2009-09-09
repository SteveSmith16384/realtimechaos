package chaosrt.server;

import ssmith.awt.*;
import java.io.*;
import chaosrt.Server;
import chaosrt.StatsFile;
import chaosrt.client.TileMapView;

public abstract class ServerObject {

  private static final int VIEW_RANGE = TileMapView.SQUARE_SIZE*10;

  public final String name;
  protected int no, angle_deg;
  public int side;
  public int current_health;
  public int type;
  public boolean destroyed=false, illusion=false;
  private int dest_x, dest_y;
  public ServerObject shot_target, dest_target;
  public RectangleDouble rect;
  protected boolean is_valid_target = true;

  // Stats
  public int att, def, magic_resistance, max_health;
  public double accuracy, speed, invisible_timer=0;
  public int shot_range, shot_power, time_takes_to_reload=0, reload_timer;
  public boolean can_shoot=false, undead=false, blocks_movement;

  public ServerObject(String obj_name, int type, double x, double y, int side, boolean blocks) throws IOException {
    this.name = obj_name;
    this.type = type;
    int slot = Server.GetNextFreeSlot();
    this.no = slot;
    Server.objects[slot] = this;
    rect = new RectangleDouble(x, y, 0, 0);
    dest_x = (int)x;
    dest_y = (int)y;
    this.side = side;
    this.blocks_movement = blocks;
    StatsFile.LoadServerStats(this);
    this.current_health = max_health;
    this.sendNewObjectToAll();
    this.sendUpdateToAll();
    if (this.undead) {
    	S2CCommunication.sendUndeadToAll(this);
    }
    this.sendTextUpdate(Server.players[this.side]);
  }

  public void setupShooter(int range, int reload_time, int shot_pow, double accur) {
    this.can_shoot = true;
    this.shot_range = range;
    this.time_takes_to_reload = reload_time;
    this.shot_power = shot_pow;
    this.accuracy = accur;
  }

  protected abstract boolean canTraverse(int type);

  /**
   * This should return true if two objects should register a collision
   */
  public abstract boolean collidedWith(ServerObject other) throws IOException;

  /**
   * This should return the text you want to appear above the image
   */
  protected abstract String getUpdateText();

  /**
   * This should do whatever wants to happen if two objects collide
   */
  public abstract void hasCollidedWith(ServerObject o) throws IOException;

  protected void sendTextUpdate(ServerPlayer player) throws IOException {
    if (player != null) {
	String text = this.getUpdateText();
	if (text.length() > 0) {
	  Server.net_server.getConnection(player.side).getOutput().writeByte(Server.OBJECT_TEXT);
	  Server.net_server.getConnection(player.side).getOutput().writeInt(this.no);
	  Server.net_server.getConnection(player.side).getOutput().writeByte(text.length());
	  Server.net_server.getConnection(player.side).getOutput().writeBytes(text);
	  Server.net_server.getConnection(player.side).getOutput().writeByte(Server.CHECK_BYTE);
	}
    }
  }

  public void sendNewObjectToAll() throws IOException {
    for (byte p = 0; p < Server.players.length; p++) {
      sendNewObject(Server.players[p]);
    }
  }

  public void sendNewObject(ServerPlayer player) throws IOException {
    if (player != null) {
      Server.net_server.getConnection(player.side).getOutput().writeByte(Server.
	  NEW_OBJECT_IIIC);
      Server.net_server.getConnection(player.side).getOutput().writeInt(no);
      Server.net_server.getConnection(player.side).getOutput().writeInt(type);
      Server.net_server.getConnection(player.side).getOutput().writeInt(side);
      //Server.server.getConnection(player.side).getOutput().writeBoolean(this.illusion);
      Server.net_server.getConnection(player.side).getOutput().writeByte(Server.
	  CHECK_BYTE);
    }
  }

  public void sendUpdateToAll() throws IOException {
    for (byte p = 0; p < Server.players.length ; p++) {
      this.sendUpdate(p);
    }
  }

  public void sendUpdate(int side) throws IOException {
    if (Server.players[side] != null) {
      if (this.destroyed == false) {
	  Server.net_server.getConnection(side).getOutput().writeByte(Server.
	      OBJECT_UPDATE_IIIBIC);
	  Server.net_server.getConnection(side).getOutput().writeInt(no);
	  Server.net_server.getConnection(side).getOutput().writeInt( (int)
	      rect.x);
	  Server.net_server.getConnection(side).getOutput().writeInt( (int)
	      rect.y);
	  Server.net_server.getConnection(side).getOutput().writeByte(this.
	      angle_deg / 10);
	  Server.net_server.getConnection(side).getOutput().writeInt(
	      current_health);
	  Server.net_server.getConnection(side).getOutput().writeByte(Server.
	      CHECK_BYTE);
      }
    }
  }

  /**
   * General processing.
   */
  public void process() throws IOException {
    if (can_shoot) {
      if (reload_timer > 0) {
	reload_timer--;
      }
    }
  }

  public void damaged(int amt, boolean corpse) throws IOException {
    if (destroyed == false) {
      this.current_health -= amt;
      if (this.current_health <= 0) {
	this.killed(corpse);
      } else {
	this.sendTextUpdate(Server.players[side]);
      }
    }
  }

  // Call this if the thing is killed.
  public void killed(boolean corpse) throws IOException {
    this.remove();
  }

  public void remove() throws IOException {
    this.destroyed = true;
    //System.out.println("Object " + no + " removed");
    Server.objects[no] = null;
    for (int p = 0; p < Server.players.length; p++) {
      Server.net_server.getConnection(p).getOutput().writeByte(Server.REMOVE_OBJECT_IC);
      Server.net_server.getConnection(p).getOutput().writeInt(no);
      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
    }
  }

  protected void hide() throws IOException {
    for (int p = 0; p < Server.players.length; p++) {
      this.hideFromPlayer(p);
    }
  }

  public void hideFromAllExceptOwner() throws IOException {
    for (int p = 0; p < Server.players.length; p++) {
      if (this.side != p) {
	this.hideFromPlayer(p);
      }
    }
  }

  protected void hideFromPlayer(int p) throws IOException {
    Server.net_server.getConnection(p).getOutput().writeByte(Server.HIDE_OBJECT_IC);
    Server.net_server.getConnection(p).getOutput().writeInt(no);
    Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
  }

  protected void show() throws IOException {
    for (int p = 0; p < Server.players.length; p++) {
      this.showToPlayer(p);
    }
  }

  protected void showToPlayer(int p) throws IOException {
    Server.net_server.getConnection(p).getOutput().writeByte(Server.SHOW_OBJECT);
    Server.net_server.getConnection(p).getOutput().writeInt(no);
    Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
  }

  public boolean canSee(ServerObject target) throws IOException {
    int sx = (int)this.rect.x;
    int sy = (int)this.rect.y;
    double x = sx;
    double y = sy;
    int tx = (int)target.rect.x;
    int ty = (int)target.rect.y;
    double dist = ssmith.lang.Functions.distance(sx, sy, tx, ty);
    int iterations = (int)dist / (TileMapView.SQUARE_SIZE / 2);
    if (iterations > 0) {
      double x_off = (tx - x) / iterations;
      double y_off = (ty - y) / iterations;
      for (int s = 0; s < iterations; s++) {
	x += x_off;
	y += y_off;
	if (Server.getMapSquareFromPixelCoords(x, y).blocks_view) {
	  return false;
	}
      }
    }
    return true;
  }

  public void setDest(int x, int y) {
    dest_target = null;
    this.dest_x = x - (int)(this.rect.width/2);
    this.dest_y = y - (int)(this.rect.height/2);
  }

  public void move(double off_x, double off_y) throws IOException {
    this.moveXByDist(off_x);
    this.moveYByDist(off_y);
    this.sendUpdateToAll();
  }
  /**
   * This returns true if the object moved, otherwise false;
   */
  protected boolean moveToDest() throws IOException {
    // See if we're tracking another object
    if (dest_target != null) {
      // Is it still around?
      if (dest_target.destroyed == true) {
	dest_target = null;
      } else {
	this.dest_x = (int) dest_target.rect.x;
	this.dest_y = (int) dest_target.rect.y;
      }
    }

    // See if we need to do anything
    if ((int)this.rect.x == dest_x && (int)this.rect.y == dest_y) {
      return false; // No movement required
    } else if (ssmith.lang.Functions.distance(this.rect.x, this.rect.y, dest_x, dest_y) < speed) {
      return false; // No movement required
    }

    int old_x = (int)this.rect.x;
    int old_y = (int)this.rect.y;

    double angle_rads = ssmith.opengl.Functions.getAngleInRadians(this.rect.x, this.rect.y, dest_x, dest_y);
    // Calc angle in degs for imagemaking
    this.angle_deg = (int) Math.toDegrees(angle_rads) + 180;
    //System.out.println("Angle:" + angle_rads);

    boolean successX = true;
    boolean movingX = false;
    if ( (int)this.rect.x != dest_x) {
      successX = moveXByAngle(angle_rads, false);
      movingX = true;
    }
    boolean successY = true;
    boolean movingY = false;
    if ( (int)this.rect.y != dest_y) {
      successY = moveYByAngle(angle_rads, false);
      movingY = true;
    }

    if (successX && successY) { // Managed to move in both directions
      // Do nothing
    }
    else if (successX && movingX) { // Only X
      moveXByAngle(angle_rads, true); // Move X again
    }
    else if (successY && movingY) { // Only Y
      moveYByAngle(angle_rads, true); // Move Y again
    }
    else { // Could not move
      this.dest_x = (int)this.rect.x;
      this.dest_y = (int)this.rect.y;
    }
    // Do we send an update?
    if (old_x != (int)this.rect.x || old_y != (int)this.rect.y) {
      this.sendUpdateToAll();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns whether succesful or not.
   */
  private boolean moveXByAngle(double angle_rads, boolean extra_move) throws IOException {
    double off_x = Math.sin(angle_rads) * (double)this.speed;
    if (extra_move) {
      if (off_x >= 0) {
	off_x = (double)this.speed - off_x;
      }
      else {
	off_x = (double) - this.speed - off_x;
      }
    }
    return moveXByDist(off_x);
  }

  private boolean moveXByDist(double off_x) throws IOException {
    rect.x += off_x;

    if (isTraversable() == false) {
      rect.x -= off_x;
      return false;
    } else {
      // See if we've collided with anyone else
      for (int o = 0; o < Server.objects.length; o++) {
	if (Server.objects[o] != null) {
	  if (Server.objects[o] != this) {
	    if (Server.objects[o].blocks_movement == true) {
	      if (this.collidedWith(Server.objects[o])) {
		this.hasCollidedWith(Server.objects[o]);
		rect.x -= off_x;
		return true; // So we don't lose the dest, or keep attacking.
	      }
	    }
	  }
	}
      }
      return true;
    }
  }

  /**
   * Returns whether succesful or not.
   */
  private boolean moveYByAngle(double angle_rads, boolean extra_move) throws IOException {
    double off_y = Math.cos(angle_rads) * (double)this.speed;
    if (extra_move) {
      if (off_y >= 0) {
	off_y = (double)this.speed - off_y;
      }
      else {
	off_y = (double) - this.speed - off_y;
      }
    }
    return moveYByDist(off_y);
  }

  private boolean moveYByDist(double off_y) throws IOException {
    rect.y += off_y;

    if (isTraversable() == false) {
      rect.y -= off_y;
      return false;
    } else {
      // See if we've collided with anyone else
      for (int o = 0; o < Server.objects.length; o++) {
	if (Server.objects[o] != null) {
	  if (Server.objects[o] != this) {
	    if (Server.objects[o].blocks_movement == true) {
	      if (this.collidedWith(Server.objects[o])) {
		this.hasCollidedWith(Server.objects[o]);
		rect.y -= off_y;
		return true; // So we don't lose the dest, or keep attacking.
	      }
	    }
	  }
	}
      }
      return true;
    }
  }

  /**
   * This returns true if it is traversable
   */
  public boolean isTraversable() throws IOException {
    if (canTraverse(Server.getMapSquareFromPixelCoords(rect.x,rect.y).type)) {
      if (canTraverse(Server.getMapSquareFromPixelCoords(rect.x+rect.width,rect.y).type)) {
	if (canTraverse(Server.getMapSquareFromPixelCoords(rect.x,rect.y+rect.height).type)) {
	  if (canTraverse(Server.getMapSquareFromPixelCoords(rect.x+rect.width,rect.y+rect.height).type)) {
	    return true;
	  } else {
	    return false;
	  }
	} else {
	  return false;
	}
      } else {
	return false;
      }
    } else {
      return false;
    }
  }

  protected void checkShotTarget() throws IOException {
    // See who they can see
    if (this.can_shoot) {
      if (this.shot_target == null) {
	int min_dist = this.shot_range;
	for (int o = 0; o < Server.objects.length; o++) {
	  if (Server.objects[o] != null) {
	    if (Server.objects[o] != this) {
	      if (Server.objects[o].side != this.side) {
		if (Server.objects[o].is_valid_target) {
		  if (Server.objects[o].invisible_timer == 0) {
		    if (this.undead || Server.objects[o].undead == false) {
		      int dist = (int) ssmith.lang.Functions.distance(this.rect.
			  x,
			  this.rect.y, Server.objects[o].rect.x,
			  Server.objects[o].rect.y);
		      if (dist <= VIEW_RANGE) { //min_dist) {
			if (this.canSee(Server.objects[o])) {
			  if (dist <= min_dist) {
			    min_dist = dist;
			    this.shot_target = Server.objects[o];
			  }
			}
		      }
		    }
		  }
		}
	      }
	    }
	  }
	}
      }
    }

    if (shot_target != null) {
      if (shot_target.destroyed || shot_target.invisible_timer > 0) {
	this.shot_target = null;
      }
      else {
	int dist = (int) ssmith.lang.Functions.distance(this.rect.x, this.rect.y,
	    shot_target.rect.x, shot_target.rect.y);
	if (dist > this.shot_range) {
	  shot_target = null;
	}
	else if (this.canSee(shot_target) == false) {
	  shot_target = null;
	}
	else {
	  this.shoot(shot_target);
	}
      }
    }
  }

  protected void checkDestTarget() throws IOException {
    if (dest_target != null) {
      if (dest_target.destroyed || dest_target.invisible_timer > 0) {
	this.dest_target = null;
      } else if (this.canSee(dest_target) == false) {
	shot_target = null;
      }
    }
  }

  public boolean isLandClearOfObjects() {
    for (int o = 0; o < Server.objects.length; o++) {
      if (Server.objects[o] != null) {
	if (Server.objects[o] != this) {
	  if (rect.intersects(Server.objects[o].rect)) {
	    return false;
	  }
	}
      }
    }
    return true;
  }

  /**
   * This returns false if they failed!
   */
  public boolean testMagicResistance() {
    int z = ssmith.lang.Functions.rnd(0, this.magic_resistance);
    return z == 0;
  }

  public void stopMoving() {
    dest_x = (int)rect.x;
    dest_y = (int)rect.y;
  }

  private void shoot(ServerObject t) throws IOException {
    if (reload_timer == 0) {
      double angle_rads = ssmith.opengl.Functions.getAngle(t.rect.x + (t.rect.width/2), t.rect.y + (t.rect.height/2), this.rect.x, this.rect.y);
      // adjust by accuracy
      angle_rads += ssmith.lang.Functions.rndDouble(-accuracy, accuracy);
      new ServerBullet(this, angle_rads);
      reload_timer = time_takes_to_reload;
    }
  }

/*  private void loadStats() throws IOException {
    TextFile tf = new TextFile();
    tf.openFile(Server.STATS_FILE, TextFile.READ);
    String line;
    boolean found = false;
    while (tf.isEOF() == false) {
      line = tf.readLine();
      if (ssmith.lang.Functions.GetParam(2, line, ",").equals(new Integer(this.type).toString())) {
	found = true;
	this.att = new Integer(ssmith.lang.Functions.GetParam(3, line, ",")).intValue();
	this.def = new Integer(ssmith.lang.Functions.GetParam(4, line, ",")).intValue();
	this.max_health = new Integer(ssmith.lang.Functions.GetParam(5, line, ",")).intValue();
	this.accuracy = new Integer(ssmith.lang.Functions.GetParam(6, line, ",")).intValue();
	this.speed = new Double(ssmith.lang.Functions.GetParam(7, line, ",")).doubleValue();
	this.shot_range = new Integer(ssmith.lang.Functions.GetParam(8, line, ",")).intValue();
	this.shot_range = shot_range * TileMapView.SQUARE_SIZE;
	this.shot_power = new Integer(ssmith.lang.Functions.GetParam(9, line, ",")).intValue();
	this.time_takes_to_reload = new Integer(ssmith.lang.Functions.GetParam(10, line, ",")).intValue();
	this.can_shoot = shot_range > 0;
	this.undead = ssmith.lang.Functions.GetParam(11, line, ",").equals("Y");
	this.magic_resistance = new Integer(ssmith.lang.Functions.GetParam(12, line, ",")).intValue();
      }
    } if (!found) {
      System.err.println("Object " + type + " not found in stats file.");
    }
  }
*/
  /**
   * This will add the object to the map, into a clear area.
   */
  public void addToMap() throws IOException {
	  while (isLandClearOfObjects() == false || this.isTraversable() == false) {
		  this.rect.x = this.rect.x + ssmith.lang.Functions.rndDouble(-this.rect.width, this.rect.width);
		  this.rect.y = this.rect.y + ssmith.lang.Functions.rndDouble(-this.rect.height, this.rect.height);
		  // Check we haven't wandered off the map
		  if (this.rect.x < 0) {
			  this.rect.x = 0;
		  }
		  if (this.rect.x > Server.map_width * TileMapView.SQUARE_SIZE) {
			  this.rect.x = Server.map_width * TileMapView.SQUARE_SIZE;
		  }
		  if (this.rect.y < 0) {
			  this.rect.y = 0;
		  }
		  if (this.rect.y > Server.map_height * TileMapView.SQUARE_SIZE) {
			  this.rect.y = Server.map_height * TileMapView.SQUARE_SIZE;
		  }
	  }
	  this.stopMoving();
	  this.sendUpdateToAll();
	  S2CCommunication.sendSmokeToAll(this);
	  
  }
  
  public String getDescriptionText(int for_side) {
    StringBuffer str = new StringBuffer();
    if (for_side == this.side) {
      // So enemies can't see if its a doppleganger or changed form
      str.append(this.name + "  Att:" + this.att + "  Def:" + this.def + "  ");
    }
    if (this.undead) {
      str.append("Undead  ");
    }
    return str.toString();
  }

  public void changeForm() throws IOException {
    int f = ssmith.lang.Functions.rnd(11, 28);
    String filename = StatsFile.GetFilename(f);

    for (byte p = 0; p < Server.players.length ; p++) {
      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHANGE_FORM_IBTC);
      Server.net_server.getConnection(p).getOutput().writeInt(this.no);
      Server.net_server.getConnection(p).getOutput().writeByte(filename.length());
      Server.net_server.getConnection(p).getOutput().writeBytes(filename);
      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
    }
  }

}
