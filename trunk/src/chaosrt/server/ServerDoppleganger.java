package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerDoppleganger extends ServerPerson {

  public ServerDoppleganger(double x, double y, int side, boolean illusion) throws IOException {
    super("Doppleganger", Server.DOPPLEGANGER, x, y, side, illusion);
    // Give it a random filename
    this.changeForm();
  }

}
