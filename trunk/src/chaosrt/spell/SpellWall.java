package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerWall;

public class SpellWall extends Spell {

  private static final int RANGE=5 * TileMapView.SQUARE_SIZE;

  public SpellWall() {
    super(Spell.WALL, 250, "Wall", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Adjust to the centre of the wall
/*    x = x - (ClientWall.IMG_WIDTH/2);
    y = y - (ClientWall.IMG_HEIGHT/2);*/
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerWall wall = new ServerWall(x, y, player.side);
      if (wall.isLandClearOfObjects() == true) {
    	  S2CCommunication.sendSmokeToAll(wall);
	player.mana -= this.cost;
	return true;
      } else {
	wall.remove();
	Server.SendMessage(player.side, "Area not clear.");
	return false;
      }
    } else {
      Server.SendMessage(player.side, "Out of range.");
      return false;
    }
  }

  public void process() throws IOException {
  }

}
