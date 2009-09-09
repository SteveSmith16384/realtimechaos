package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerDwarf;
import chaosrt.server.ServerPlayer;

public class SpellDwarf extends Spell {

  public SpellDwarf() {
    super(Spell.DWARF, 250, "Dwarf", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerDwarf dwarf = new ServerDwarf(x, y, player.side, illusion);
      dwarf.addToMap();
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
