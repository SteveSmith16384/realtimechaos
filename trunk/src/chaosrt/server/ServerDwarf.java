package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerDwarf extends ServerPerson {

  public ServerDwarf(double x, double y, int side, boolean illusion) throws IOException {
    super("Dwarf", Server.DWARF, x, y, side, illusion);
  }

}
