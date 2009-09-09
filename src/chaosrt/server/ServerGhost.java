package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerGhost extends ServerPerson {

  public ServerGhost(double x, double y, int side, boolean ill) throws IOException {
    super("Ghost", Server.GHOST, x, y, side, ill);
  }

}
