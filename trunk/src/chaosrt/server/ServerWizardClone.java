package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerWizardClone extends ServerPerson {

  public ServerWizardClone(double x, double y, int side, boolean ill) throws IOException {
    super("Wizard", Server.WIZARD, x, y, side, ill);
  }

}
