package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerHydra;
import chaosrt.server.ServerPlayer;

public class SpellHydra extends Spell {

  public SpellHydra() {
    super(Spell.HYDRA, 1850, "Hydra", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerHydra hydra = new ServerHydra(x, y, player.side, illusion);
      hydra.addToMap();
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
