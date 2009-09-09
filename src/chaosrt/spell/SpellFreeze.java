package chaosrt.spell;

import java.io.*;
import java.awt.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPerson;
import chaosrt.server.ServerPlayer;

public class SpellFreeze extends Spell {

  private static final int RANGE=5 * TileMapView.SQUARE_SIZE;

  public SpellFreeze() {
    super(Spell.FREEZE, 200, "Freeze", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    ServerObject obj = Server.GetObjectAtPosition(x, y, true);
    if (obj != null) {
      if (obj instanceof ServerPerson) {
	ServerPerson p = (ServerPerson)obj;
	S2CCommunication.sendSmokeToAll(p);
	//if (p.testMagicResistance() == false) {
	  p.frozen_timer += 500;
	  p.sendFrozenToAll();
	  Server.SendMessage(player.side, "You have frozen it!");
/*	} else {
	  Server.SendMessage(player.side, "Failed!");
	}*/
	player.mana -= this.cost;
      }
    }
    return true;
  }

  public void process() throws IOException {
  }

  public Color getColour() {
    return Color.cyan;
  }

}
