package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerWizard extends ServerPerson {

  public ServerWizard(double x, double y, int side) throws IOException {
    super("Wizard", Server.WIZARD, x, y, side, false);
  }

  public void remove() throws IOException {
    // Remove all their creatures as well.
    Server.SendMessageToAll("Wizard has been killed!", (byte)2);
    S2CCommunication.sendExplosionToAll(this, 200);
    super.remove(); // So we don't FIND ourselves below
    ServerObject obj;
    for(int o=0 ; o < Server.objects.length ; o++) {
      obj = (ServerObject) Server.objects[o];
      if (obj != null) {
	if (obj.side == this.side) {
		S2CCommunication.sendSmokeToAll(obj);
	  obj.remove();
	}
      }
    }
  }

}
