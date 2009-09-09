package chaosrt.server;

import ssmith.lang.*;
import java.io.*;
import chaosrt.Server;
import chaosrt.client.TileMapView;

public class ServerGooeyBlob extends ServerObject {

  private static final int MAX_BLOBS = 200;
  private static int total_blobs=0;

  public ServerGooeyBlob(double x, double y, int side) throws IOException {
    super("Gooey Blob", Server.GOOEY_BLOB, x, y, side, true);
    is_valid_target = false;
    total_blobs++;
  }

  public void process() throws IOException {
    super.process();
    if (Functions.rnd(1, 1000) == 1 && total_blobs < MAX_BLOBS) {
      int x = (int)(this.rect.x+ Functions.rndDouble( -this.rect.width, this.rect.width));
      int y = (int)(this.rect.y+ Functions.rndDouble( -this.rect.height, this.rect.height));
      // Check we're not off the map
      if (x > 0 &&  x < Server.map_width * TileMapView.SQUARE_SIZE && y > 0 && y < Server.map_height * TileMapView.SQUARE_SIZE) {
    	  new ServerGooeyBlob(x, y, this.side);
      }
    } else if (Functions.rnd(1, 1500) == 1) {
      this.remove();
    }

  }

  public boolean canTraverse(int type) {
    return false;
  }

  public boolean collidedWith(ServerObject other) {
    return false;
  }

  public void hasCollidedWith(ServerObject o) {
    // Do nothing
  }

  public String getUpdateText() {
    return "";
  }

  // Call this if the thing is killed.
  public void remove() throws IOException {
    ServerGooeyBlob.total_blobs--;
    super.remove();
  }


}
