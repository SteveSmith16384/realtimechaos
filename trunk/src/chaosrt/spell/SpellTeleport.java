package chaosrt.spell;

import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerPlayer;

public class SpellTeleport extends Spell {

  public SpellTeleport() {
    super(Spell.TELEPORT, 400, "Teleport", 0);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    int old_x = (int)player.wizard.rect.x;
    int old_y = (int)player.wizard.rect.y;

    player.wizard.rect.x = x;
    player.wizard.rect.y = y;
    if (player.wizard.isTraversable() && player.wizard.isLandClearOfObjects()) {
      player.wizard.stopMoving();
      player.wizard.sendUpdateToAll();
      player.mana -= this.cost;
      return true;
    } else {
      player.wizard.rect.x = old_x;
      player.wizard.rect.y = old_y;
      Server.SendMessage(player.wizard.side, "Area not clear.");
    }
    return false;
  }

  public void process() throws IOException {
  }

}
