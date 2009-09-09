package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellSubversion extends Spell {

  private static final int RANGE=4 * TileMapView.SQUARE_SIZE;

  public SpellSubversion() {
    super(Spell.SUBVERSION, 1000, "Subversion", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
	  int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
	  if (dist <= RANGE) {
		  ServerObject obj = Server.GetObjectAtPosition(x, y, true);
		  if (obj != null) {
			  if (obj.side != player.side) {
				  S2CCommunication.sendSmokeToAll(obj);
				  if (obj.testMagicResistance() == false) {
					  Server.SendMessage(obj.side, (byte)2, "Your "+obj.name+" has been subverted!");
					  obj.side = player.side;
					  S2CCommunication.sendSideToAll(obj);
					  Server.SendMessage(player.side, "It worked!");
				  } else {
					  Server.SendMessage(player.side, "Failed.");
				  }
				  player.mana -= this.cost;
				  return true;
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
