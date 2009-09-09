package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerMummy extends ServerPerson {

  public ServerMummy(double x, double y, int side, boolean ill) throws IOException {
    super("Mummy", Server.MUMMY, x, y, side, ill);
  }

}
