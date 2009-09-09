package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerTroll extends ServerPerson {

  public ServerTroll(double x, double y, int side, boolean illusion) throws IOException {
    super("Troll", Server.TROLL, x, y, side, illusion);
  }

}
