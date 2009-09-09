package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerOrc;
import chaosrt.server.ServerPlayer;

public class SpellOrc extends Spell {

  public SpellOrc() {
    super(Spell.ORC, 300, "Orc", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
/*    x = (int)player.wizard.rect.x;
    y = (int)player.wizard.rect.y;*/
// Check range
int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
if (dist <= radius) {
    ServerOrc orc = new ServerOrc(x, y, player.side, illusion);
    orc.addToMap();
/*    while (orc.isLandClearOfObjects() == false) {
       orc.rect.x = orc.rect.x + Functions.rnd(-2, 2);
       orc.rect.y = orc.rect.y + Functions.rnd(-2, 2);
    }
    orc.stopMoving();
    orc.sendSmokeToAll();*/
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

/*  public String toString() {
    return "Orc";
  }

  public int getCost() {
    return 750;
  }
*/
  public void process() throws IOException {
  }

}
