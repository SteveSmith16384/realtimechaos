package chaosrt.spell;

import java.io.*;

import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellHeal extends Spell {

  private static final int RANGE=10 * TileMapView.SQUARE_SIZE;

  public SpellHeal() {
    super(Spell.HEAL, 100, "Heal", RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    ServerObject obj = Server.GetObjectAtPosition(x, y, true);
    if (obj != null) {
      if (obj.current_health < obj.max_health) {
	obj.current_health+=10;
	obj.sendUpdateToAll();
	Server.SendMessage(player.side, "Healed.");
	player.mana -= this.cost;
      } else {
	Server.SendMessage(player.side, "Cannot heal any more.");
      }
    }
    return true;
  }

  public void process() throws IOException {

  }

}
