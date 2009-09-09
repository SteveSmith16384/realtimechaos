package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerKnight extends ServerPerson {

  public ServerKnight(int x, int y, int side, boolean illusion) throws IOException {
    super("Knight", Server.KNIGHT,x, y, side, illusion);
  }

}
