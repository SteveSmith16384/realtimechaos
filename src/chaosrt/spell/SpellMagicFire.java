package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerMagicFire;
import chaosrt.server.ServerPlayer;

public class SpellMagicFire extends Spell {

  private static final int RANGE=5 * TileMapView.SQUARE_SIZE;

  public SpellMagicFire() {
    super(Spell.MAGIC_FIRE, 500, "Magic Fire", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Adjust to the centre
/*    x = x - (ClientMagicFire.IMG_WIDTH/2);
    y = y - (ClientMagicFire.IMG_HEIGHT/2);*/
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerMagicFire fire = new ServerMagicFire(x, y, player.side);
      if (fire.isTraversable() && fire.isLandClearOfObjects() == true) {
    	  S2CCommunication.sendSmokeToAll(fire);
	player.mana -= this.cost;
	return true;
      } else {
	fire.remove();
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
