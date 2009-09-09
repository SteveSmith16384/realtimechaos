package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;
import java.awt.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerVortex;

public class SpellVortex extends Spell {

  private static final int RANGE=10 * TileMapView.SQUARE_SIZE;

  public SpellVortex() {
    super(Spell.VORTEX, 500, "Vortex", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerVortex vortex = new ServerVortex(x, y, player.side);
      if (vortex.isTraversable() && vortex.isLandClearOfObjects() == true) {
    	  S2CCommunication.sendSmokeToAll(vortex);
	player.mana -= this.cost;
	return true;
      } else {
	vortex.remove();
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

  public Color getColour() {
    return Color.black;
  }

}
