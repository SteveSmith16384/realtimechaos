package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerArcher;
import chaosrt.server.ServerPlayer;

public class SpellArcher extends Spell {

  public SpellArcher() {
    super(Spell.ARCHER, 350, "Archer", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerArcher archer = new ServerArcher(x, y, player.side, illusion);
      archer.addToMap();
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
