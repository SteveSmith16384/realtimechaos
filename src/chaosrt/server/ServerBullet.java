package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerBullet extends ServerObject {

  private double x_off, y_off;

  public ServerBullet(ServerObject shooter, double angle_rads) throws IOException {
    super("Bullet", Server.BULLET, shooter.rect.x, shooter.rect.y, shooter.side, true);
    this.shot_range = shooter.shot_range;
    this.shot_power = shooter.shot_power;
    x_off = Math.sin(Math.toRadians(angle_rads)) * (double)this.speed;
    y_off = Math.cos(Math.toRadians(angle_rads)) * (double)this.speed;
    is_valid_target = false;
    this.undead = shooter.undead;
  }

  public boolean canTraverse(int type) {
    if (type == Server.GRASS || type == Server.ROAD || type == Server.WATER) {
      return true;
    } else {
      return false;
    }
  }

  public void process() throws IOException {
    this.rect.x += x_off;
    this.rect.y += y_off;

    if (this.isTraversable() == false) {
      ServerMapSquare sq = Server.getMapSquareFromPixelCoords(this.rect.x, this.rect.y);
      sq.damaged(this.shot_power);
      this.remove();
    }
    else {
      boolean send_update = true;
      // See if we've collided with anyone else
      for (int o = 0; o < Server.objects.length; o++) {
	if (Server.objects[o] != null) {
	  if (Server.objects[o] != this) {
	    if (Server.objects[o].side != this.side) {
	      if (Server.objects[o].type != Server.BULLET) {
		if (this.undead || Server.objects[o].undead == false) {
		  if (this.collidedWith(Server.objects[o])) {
		    send_update = false;
		    Server.objects[o].damaged(this.shot_power, true);
		    S2CCommunication.sendBulletFlashToAll(this);
		    this.remove();
		    break;
		  }
		}
	      }
	    }
	  }
	}
      }
      if (send_update) {
	this.sendUpdateToAll();
      }
    }
    this.shot_range -= (int)this.speed;
    if (shot_range <= 0) {
      this.remove();
    }
  }

  public boolean collidedWith(ServerObject other) {
    if (this.rect.intersects(other.rect) && other instanceof ServerCorpse == false) {
      return true;
    } else {
      return false;
    }
  }

  public void hasCollidedWith(ServerObject o) {
    // Do nothing
  }


  protected String getUpdateText() {
    return "";
  }

}
