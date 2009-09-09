package chaosrt.server;

import ssmith.lang.*;
import java.io.*;
import chaosrt.Server;
import chaosrt.client.TileMapView;

public class ServerMagicFire extends ServerObject {

  private static final int MAX_FIRES = 100;
  private static int total_fires=0;

  public ServerMagicFire(double x, double y, int side) throws IOException {
    super("Magic Fire", Server.MAGIC_FIRE, x, y, side, true);
    is_valid_target = false;
    total_fires++;
  }

  public void process() throws IOException {
    super.process();
    // Move us about slowly
    this.move(Functions.rndDouble(-0.1, 0.1), Functions.rndDouble(-0.1, 0.1));

    if (Functions.rnd(1, 750) == 1 && total_fires < MAX_FIRES) {
      int x = (int)(this.rect.x+ Functions.rndDouble( -this.rect.width, this.rect.width));
      int y = (int)(this.rect.y+ Functions.rndDouble( -this.rect.height, this.rect.height));
      // Check we're not off the map
      if (x > 0 &&  x < Server.map_width * TileMapView.SQUARE_SIZE && y > 0 && y < Server.map_height * TileMapView.SQUARE_SIZE) {
    	  new ServerMagicFire(x, y, this.side);
      }
    } else if (Functions.rnd(1, 1000) == 1) {
      this.remove();
    }

  }

  public boolean canTraverse(int type) {
    return true;
  }

  public boolean collidedWith(ServerObject other) throws IOException {
    return (this.rect.intersects(other.rect));
  }

  public void hasCollidedWith(ServerObject o) throws IOException {
    if (o instanceof ServerMagicFire == false) {
      o.remove();
    }
  }

  public String getUpdateText() {
    return "";
  }

  // Call this if the thing is killed.
  public void remove() throws IOException {
    ServerMagicFire.total_fires--;
    super.remove();
  }


}
