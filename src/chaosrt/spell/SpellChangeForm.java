package chaosrt.spell;

import java.awt.*;
import java.io.*;

import chaosrt.Server;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellChangeForm extends Spell {

  public SpellChangeForm() {
    super(Spell.CHANGE_FORM, 300, "Change Form", 0);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException {
	  ServerObject obj = Server.GetObjectAtPosition(x, y, true);
	  if (obj != null) {
		  if (obj.side == player.wizard.side) {
			  S2CCommunication.sendSmokeToAll(obj);
			  obj.changeForm();
		  } else {
			  Server.SendMessage(player.side, "You must choose something on your side.");
		  }
		  player.mana -= this.cost;
	  } else {
		  Server.SendMessage(player.side, "No creature selected!");
	  }
	  return true;
  }
  
  public Color getColour() {
    return Color.green;
  }

  public void process() throws IOException {
  }

}
