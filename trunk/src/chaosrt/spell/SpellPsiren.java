package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerPsiren;

public class SpellPsiren extends Spell {

  public SpellPsiren() {
    super(Spell.PSIREN, 500, "Psiren", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerPsiren psiren = new ServerPsiren(x, y, player.side, illusion);
      psiren.addToMap();
      if (illusion == false) {
	player.mana -= this.cost;
      } else {
	player.mana -= this.cost / Server.ILLUSION_DIVIDE;
      }
      return true;
    } else {
      Server.SendMessage(player.side, "Out of range ("+dist+"/"+radius+")");
      return false;
    }
  }

  public void process() throws IOException {
  }

}
