package chaosrt.client;

import java.io.IOException;

import chaosrt.Client;
import chaosrt.Server;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerPlayer;
import chaosrt.spell.Spell;

public class C2SCommunication {

	private C2SCommunication() {
	}

	  public static synchronized void castCurrentSpell(Client client) throws IOException {
		    client.net_client.getOutput().writeByte(Server.CAST_SPELL_IIIB);
		    client.net_client.getOutput().writeInt(client.curr_spell);
		    client.net_client.getOutput().writeInt(client.mouse_end_x);
		    client.net_client.getOutput().writeInt(client.mouse_end_y);
		    client.net_client.getOutput().writeBoolean(client.illusion);
		  }
	  
	  public static synchronized void DecodeCastCurrentSpell(ServerPlayer pl) throws IOException {
		  int spell_no = Server.net_server.getConnection(pl.side).getInput().readInt();
		  Spell spl = (Spell)pl.spellbook.get(spell_no);
		  int x = Server.net_server.getConnection(pl.side).getInput().readInt();
		  int y = Server.net_server.getConnection(pl.side).getInput().readInt();
		  boolean illusion = Server.net_server.getConnection(pl.side).getInput().readBoolean();
		  // Check mana
		  if (pl.mana >= spl.cost) {
			  S2CCommunication.sendLighteningBoltToAll(pl.wizard, x, y, spl.getColour());
			  Server.SendMessage(pl.side, "Casting "+spl.name+"...");
			  spl.cast(pl, x, y, illusion);
		  } else {
			  Server.SendMessage(pl.side, (byte)2, "You do not have enough mana!");
		  }
	  }
}
