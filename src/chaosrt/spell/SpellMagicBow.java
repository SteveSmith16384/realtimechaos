package chaosrt.spell;

import java.io.*;

import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerPlayer;

public class SpellMagicBow extends Spell {

  public static final long DURATION = 10000;

  public SpellMagicBow() {
    super(Spell.MAGIC_BOW, 500, "Magic Bow", 0);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
    player.wizard.setupShooter(10 * TileMapView.SQUARE_SIZE, 100, 1, 5f);
    player.wizard.lightening_timer += 1000;
    S2CCommunication.sendSmokeToAll(player.wizard);
    player.mana -= this.cost;
    return true;
  }

  public void process() throws IOException {
  }

}
