package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerDragon extends ServerPerson {

  public ServerDragon(double x, double y, int side, boolean illusion) throws IOException {
    super("Dragon", Server.DRAGON, x, y, side, illusion);
  }

}
