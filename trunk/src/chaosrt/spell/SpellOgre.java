package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerOgre;
import chaosrt.server.ServerPlayer;

public class SpellOgre extends Spell {

  public SpellOgre() {
    super(Spell.OGRE, 2500, "Ogre", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
/*    x = (int)player.wizard.rect.x;
    y = (int)player.wizard.rect.y;*/
// Check range
int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
if (dist <= radius) {
    ServerOgre ogre = new ServerOgre(x, y, player.side, illusion);
    ogre.addToMap();
/*    while (ogre.isLandClearOfObjects() == false) {
       ogre.rect.x = ogre.rect.x + Functions.rnd(-2, 2);
       ogre.rect.y = ogre.rect.y + Functions.rnd(-2, 2);
    }
    ogre.stopMoving();
    ogre.sendSmokeToAll();*/
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
    return "Ogre";
  }

  public int getCost() {
    return 2000;
  }
*/
  public void process() throws IOException {
  }
}
