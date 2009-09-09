package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerPlayer;
import chaosrt.server.ServerWizardClone;

public class SpellCloneWizard extends Spell {

  public SpellCloneWizard() {
    super(Spell.CLONE_WIZARD, 500, "Clone Wizard", Spell.CREATURE_RANGE);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    // Check range
    int dist = (int)Functions.distance(x, y, player.wizard.rect.x, player.wizard.rect.y);
    if (dist <= radius) {
      ServerWizardClone wiz = new ServerWizardClone(x, y, player.side, illusion);
      wiz.addToMap();
      player.mana -= this.cost;
      return true;
    } else {
      Server.SendMessage(player.side, "Out of range ("+dist+"/"+radius+")");
      return false;
    }
  }

  public void process() throws IOException {
  }

}
