package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;
import java.awt.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerGooeyBlob;
import chaosrt.server.ServerPlayer;

public class SpellGooeyBlob extends Spell {

  private static final int RANGE=5 * TileMapView.SQUARE_SIZE;

  public SpellGooeyBlob() {
    super(Spell.GOOEY_BLOB, 500, "Gooey Blob", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Adjust to the centre
/*    x = x - (ClientGooeyBlob.IMG_WIDTH/2);
    y = y - (ClientGooeyBlob.IMG_HEIGHT/2);*/
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerGooeyBlob blob = new ServerGooeyBlob(x, y, player.side);
      if (blob.isLandClearOfObjects() == true) {
    	  S2CCommunication.sendSmokeToAll(blob);
	player.mana -= this.cost;
	return true;
      } else {
	blob.remove();
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
    return Color.green;
  }


}
