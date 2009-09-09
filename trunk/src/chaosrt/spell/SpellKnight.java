package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerKnight;
import chaosrt.server.ServerPlayer;

public class SpellKnight extends Spell {

  public SpellKnight() {
    super(Spell.KNIGHT, 675, "Knight", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
/*    x = (int)player.wizard.rect.x;
    y = (int)player.wizard.rect.y;*/
// Check range
int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
if (dist <= radius) {
    ServerKnight knight = new ServerKnight(x, y, player.side, illusion);
    knight.addToMap();
/*    while (knight.isLandClearOfObjects() == false) {
       knight.rect.x = knight.rect.x + Functions.rnd(-2, 2);
       knight.rect.y = knight.rect.y + Functions.rnd(-2, 2);
    }
    knight.stopMoving();
    knight.sendUpdateToAll();
    knight.sendSmokeToAll();*/
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
    return "Knight";
  }

  public int getCost() {
    return 1000;
  }
*/
  public void process() throws IOException {
  }

}
