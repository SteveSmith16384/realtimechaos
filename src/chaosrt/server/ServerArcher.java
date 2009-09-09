package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerArcher extends ServerPerson {

  public ServerArcher(int x, int y, int side, boolean ill) throws IOException {
    super("Archer", Server.ARCHER, x, y, side, ill);
  }

}
