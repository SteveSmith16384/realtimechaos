package chaosrt.server;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;

public class ServerVortex extends ServerObject {

  private double off_x, off_y;

  public ServerVortex(double x, double y, int side) throws IOException {
    super("Vortex", Server.VORTEX, x, y, side, true);
    is_valid_target = false;
    off_x = Functions.rndDouble(-2, 2);
    off_y = Functions.rndDouble(-2, 2);
  }

  public void process() throws IOException {
	  super.process();
	  // Move us about slowly
	  if (Functions.rnd(1, 100) == 1) {
		  off_x = Functions.rndDouble( -1, 1);
		  off_y = Functions.rndDouble( -1, 1);
	  }
	  this.move(off_x, off_y);
	  
  
	  if (Functions.rnd(1, 10000) == 1) {
		  this.remove();
	  } else {
		  // check it is still on the map!
	      if (rect.x < 0) rect.x = Server.map_width * TileMapView.SQUARE_SIZE;
	      if (rect.x > Server.map_width * TileMapView.SQUARE_SIZE) rect.x = 0;
	      if (rect.y < 0) rect.y = Server.map_height * TileMapView.SQUARE_SIZE;
	      if (rect.y > Server.map_height * TileMapView.SQUARE_SIZE) rect.y = 0;
	  }
	  
  }
  
  public boolean canTraverse(int type) {
    return true;
  }

  public boolean collidedWith(ServerObject other) throws IOException {
    return (this.rect.intersects(other.rect));
  }

  public void hasCollidedWith(ServerObject o) throws IOException {
      o.remove();
  }

  public String getUpdateText() {
    return "";
  }

}
