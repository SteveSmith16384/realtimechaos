package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellWind extends Spell {

  private static final long DURATION = 20000;

  private long start_time;
  private int x_dir=0, y_dir=0, side;

  public SpellWind() {
    super(Spell.WIND, 750, "Wind", 0);
    while (x_dir == 0) {
      x_dir = Functions.rnd(-1, 1);
      y_dir = Functions.rnd(-1, 1);
    }
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) {
    start_time = System.currentTimeMillis();
    player.mana -= this.cost;
    Server.spells_in_effect.add(this);
    this.side = player.side;
    return true;
  }

  public void process() throws IOException {
	  if (System.currentTimeMillis() - start_time < DURATION) {
		  ServerObject obj;
		  for (int o=0 ; o<Server.objects.length ; o++) {
			  obj = Server.objects[o];
			  if (obj != null) {
				  if (obj.side != this.side) {
					  obj.move(x_dir, y_dir);
				  }
			  }
		  }
	  } else {
		  Server.spells_in_effect.remove(this);
	  }
	  
  }
  
}
