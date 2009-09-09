package chaosrt.spell;

import java.io.*;

import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerPlayer;

public class SpellInvisible extends Spell {

  public SpellInvisible() {
    super(Spell.INVISIBLE, 500, "Invisibility", 0);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    player.wizard.invisible_timer += 1000;
    player.wizard.hideFromAllExceptOwner();
    S2CCommunication.sendSmokeToAll(player.wizard);
    player.wizard.sendInvisible();
    player.mana -= this.cost;
    return true;
  }

  public void process() throws IOException {
  }

}
