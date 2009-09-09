package chaosrt.spell;

import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellAlterReality extends Spell {

  public SpellAlterReality() {
    super(Spell.ALTER_REALITY, 750, "Alter Reality", 0);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    for (int o=0 ; o< Server.objects.length ; o++) {
      ServerObject obj = Server.objects[o];
      if (obj != null) {
    	  obj.changeForm();
      }
    }
    return true;
  }

  public void process() throws IOException {
  }


}