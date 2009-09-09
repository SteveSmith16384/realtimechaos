package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerShadowWood;

public class SpellShadowWood extends Spell {

  public SpellShadowWood() {
    super(Spell.SHADOW_WOOD, 300, "Shadow Wood", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Adjust to the centre
/*    x = x - (ClientShadowWood.IMG_WIDTH/2);
    y = y - (ClientShadowWood.IMG_HEIGHT/2);*/
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerShadowWood wood = new ServerShadowWood(x, y, player.side);
      if (wood.isTraversable() && wood.isLandClearOfObjects() == true) {
    	  S2CCommunication.sendSmokeToAll(wood);
	player.mana -= this.cost;
	return true;
      } else {
	wood.remove();
	Server.SendMessage(player.side, "Area not clear.");
	return false;
      }
    } else {
      Server.SendMessage(player.side, "Out of range ("+dist+"/"+radius+")");
      return false;
    }
  }

  public void process() throws IOException {
  }

}
