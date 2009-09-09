package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerPsiren extends ServerPerson {

  public ServerPsiren(double x, double y, int side, boolean illusion) throws IOException {
    super("Psiren", Server.PSIREN, x, y, side, illusion);
  }

}
