package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerTroll;

public class SpellTroll extends Spell {

  public SpellTroll() {
    super(Spell.TROLL, 1300, "Troll", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
 /*   x = (int)player.wizard.rect.x;
    y = (int)player.wizard.rect.y;*/
 // Check range
 int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
 if (dist <= radius) {
    ServerTroll troll = new ServerTroll(x, y, player.side, illusion);
    troll.addToMap();
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
