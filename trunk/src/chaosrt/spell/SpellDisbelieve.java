package chaosrt.spell;

import java.awt.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellDisbelieve extends Spell {

  public SpellDisbelieve() {
    super(Spell.DISBELIEVE, 0, "Disbelieve", 0);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
	  ServerObject obj = Server.GetObjectAtPosition(x, y, true);
	  if (obj != null) {
		  if (obj.illusion == true) {
			  S2CCommunication.sendSmokeToAll(obj);
			  obj.remove();
			  Server.SendMessage(player.side, "Illusion destroyed.");
		  } else {
			  Server.SendMessage(player.side, "That is not an Illusion.");
		  }
		  player.mana -= this.cost;
	  } else {
		  Server.SendMessage(player.side, "No creature selected!");
	  }
	  return true;
  }
  
  public Color getColour() {
    return Color.white;
  }

  public void process() throws IOException {
  }

}
