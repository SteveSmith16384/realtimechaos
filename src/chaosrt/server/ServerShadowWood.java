package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerShadowWood extends ServerPerson {

  public ServerShadowWood(double x, double y, int side) throws IOException {
    super("Shadow Wood", Server.SHADOW_WOOD, x, y, side, true);
  }

}
