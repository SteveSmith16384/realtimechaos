package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerMountedSkeleton;
import chaosrt.server.ServerPlayer;

public class SpellMountedSkeleton extends Spell {

  public SpellMountedSkeleton() {
    super(Spell.MOUNTED_SKELETON, 900, "Mounted Skeleton", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
  /*  x = (int)player.wizard.rect.x;
    y = (int)player.wizard.rect.y;*/
  // Check range
  int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
  if (dist <= radius) {
    ServerMountedSkeleton skel = new ServerMountedSkeleton(x, y, player.side, illusion);
    skel.addToMap();
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
