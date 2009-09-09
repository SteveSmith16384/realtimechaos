package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;
import java.awt.*;

import chaosrt.Server;
import chaosrt.client.ClientZoneOfDarkness;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerPlayer;

public class SpellZoneOfDarkness extends Spell {

  private static final int RANGE=10 * TileMapView.SQUARE_SIZE;

  public SpellZoneOfDarkness() {
    super(Spell.ZONE_OF_DARKNESS, 200, "Zone of Darkness", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
	  // Adjust to the centre
	  x = x - (ClientZoneOfDarkness.FX_RADIUS/2);
	  y = y - (ClientZoneOfDarkness.FX_RADIUS/2);
	  // Check range
	  int dist = (int)Functions.distance(player.wizard.rect.x, player.wizard.rect.y, x, y);
	  if (dist <= radius) {
		  S2CCommunication.sendZoneOfDarknessToAll(player.wizard, x, y);
		  player.mana -= this.cost;
		  return true;
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
