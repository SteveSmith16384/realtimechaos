package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerWizard;

public class SpellLighteningBolt extends Spell{

  private static final int RANGE=10 * TileMapView.SQUARE_SIZE;
  private static final int MAX_DAMAGE=200;

  public SpellLighteningBolt() {
    super(Spell.LIGHTENING_BOLT, 500, "Lightening Bolt", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
	  int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
	  if (dist <= radius) {
		  ServerObject obj = Server.GetObjectAtPosition(x, y, true);
		  if (obj != null) {
			  if (obj.side != player.side) {
				  if (obj instanceof ServerWizard == false) {
					  S2CCommunication.sendSmokeToAll(obj);
					  if (obj.testMagicResistance() == false) {
						  int dam = Functions.rnd(MAX_DAMAGE/2, MAX_DAMAGE);
						  obj.damaged(dam, false);
						  Server.SendMessage(player.side, dam + " damage done.");
					  } else {
						  Server.SendMessage(player.side, "Failed.");
					  }
					  player.mana -= this.cost;
					  return true;
				  } else {
					  Server.SendMessage(player.side, "You cannot select a Wizard.");
				  }
			  } else {
				  Server.SendMessage(player.side, "You must select an enemy.");
			  }
		  } else {
			  Server.SendMessage(player.side, "No creature selected!");
		  }
	  } else {
		  Server.SendMessage(player.side, "Out of range ("+dist+"/"+RANGE+")");
	  }
	  return false;
  }
  
  public void process() throws IOException {
  }

}
