package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerOgre extends ServerPerson {

  public ServerOgre(double x, double y, int side, boolean illusion) throws IOException {
    super("Ogre", Server.OGRE, x, y, side, illusion);
  }

}
