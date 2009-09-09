package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;
import java.awt.*;
import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerCorpse;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellResurrect extends Spell {

  private static final int RANGE=3 * TileMapView.SQUARE_SIZE;

  public SpellResurrect() {
    super(Spell.RESURRECT, 1000, "Raise Dead", RANGE);
    if (Server.use_undead == false) {
      this.cost = this.cost / 2;
    }
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerObject obj = Server.GetObjectAtPosition(x, y, false);
      if (obj != null) {
	if (obj instanceof ServerCorpse) {
		S2CCommunication.sendSmokeToAll(obj);
	  if (Functions.rnd(1, 3) == 1) {
	    ServerCorpse corpse = (ServerCorpse) obj;
	    corpse.resurrect(player.side);
	    Server.SendMessage(player.side, "It worked!");
	  } else {
	    Server.SendMessage(player.side, "Failed.");
	  }
	  player.mana -= this.cost;
	  return true;
	} else {
	  Server.SendMessage(player.side, "You must select a corpse.");
	}
      }
    } else {
      Server.SendMessage(player.side, "Out of range ("+dist+"/"+RANGE+")");
    }
    return false;
  }

  public void process() throws IOException {
  }

  public Color getColour() {
    return Color.lightGray;
  }

}
