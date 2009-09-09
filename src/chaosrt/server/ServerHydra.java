package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerHydra extends ServerPerson {

  public ServerHydra(double x, double y, int side, boolean illusion) throws IOException {
    super("Hydra", Server.HYDRA, x, y, side, illusion);
  }

}
