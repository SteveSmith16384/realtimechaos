package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerOrc extends ServerPerson {

  public ServerOrc(double x, double y, int side, boolean illusion) throws IOException {
    super("Orc", Server.ORC, x, y, side, illusion);
  }

}
