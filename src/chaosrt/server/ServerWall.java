package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerWall extends ServerObject {

  public ServerWall(int x, int y, int side) throws IOException {
    super("Wall", Server.WALL, x, y, side, true);
    is_valid_target = false;
  }

  protected boolean canTraverse(int type) {
    return false;
  }

  public boolean collidedWith(ServerObject other) {
    return true;
  }

  protected String getUpdateText() {
    return "";
  }

  public void hasCollidedWith(ServerObject o) {
    return;
  }

}
