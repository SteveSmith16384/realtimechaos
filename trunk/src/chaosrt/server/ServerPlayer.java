package chaosrt.server;

import java.io.*;
import java.util.*;

import ssmith.util.Interval;

import chaosrt.Server;
import chaosrt.Statics;
import chaosrt.client.C2SCommunication;
import chaosrt.spell.Spell;
import chaosrt.spell.SpellDisbelieve;

public class ServerPlayer {

  public int side, mana=Statics.START_MANA;
  public String name;
  public ArrayList spellbook = new ArrayList();
  public ServerWizard wizard;
  private Interval send_mana_update = new Interval(1000);

  public ServerPlayer(byte side) throws IOException {
	  this.side = side;
	  Server.net_server.getConnection(side).getOutput().writeByte(Server.PLAYER_NO_IC);
	  Server.net_server.getConnection(side).getOutput().writeInt(side);
	  Server.net_server.getConnection(side).getOutput().writeByte(Server.CHECK_BYTE);
	  
	  this.sendMap();
	  // Give random spells
	  spellbook.add(new SpellDisbelieve());
	  for(int s=0 ; s<Statics.START_SPELLS ; s++) {
		  boolean already_have_it = true;
		  Spell spell = Spell.GetRandomSpell();
		  while (already_have_it) {
			  spell = Spell.GetRandomSpell();
			  
			  // Check we don't already have it
			  already_have_it = false;
			  for(int c=0 ; c<spellbook.size() ; c++) {
				  Spell splchk = (Spell)spellbook.get(c);
				  if (splchk.type == spell.type) {
					  already_have_it = true;
					  break;
				  }
			  }
		  }
		  spellbook.add(spell);
	  }
	  this.sendSpellUpdate();
	  
	  for(int p=0 ; p<Server.objects.length ; p++) {
		  if (Server.objects[p] != null) {
			  Server.objects[p].sendNewObject(this);
			  Server.objects[p].sendUpdate(this.side);
			  if (Server.objects[p].side == this.side) {
				  Server.objects[p].sendTextUpdate(this);
			  }
		  }
	  }
  }
  
  private void sendMap() throws IOException {
	  Server.net_server.getConnection(side).getOutput().writeByte(Server.NEW_MAP_II);
	  Server.net_server.getConnection(side).getOutput().writeInt(Server.map_height);
	  Server.net_server.getConnection(side).getOutput().writeInt(Server.map_width);
	  
	  for (int y = 0; y < Server.map_height; y++) {
		  for (int x = 0; x < Server.map_width; x++) {
			  Server.net_server.getConnection(side).getOutput().writeByte(Server.MAPSQUARE_UPDATE);
			  Server.net_server.getConnection(side).getOutput().writeInt(x);
			  Server.net_server.getConnection(side).getOutput().writeInt(y);
			  Server.net_server.getConnection(side).getOutput().writeInt(Server.map[x][y].type);
			  Server.net_server.getConnection(side).getOutput().writeByte(Server.CHECK_BYTE);
		  }
	  }
  }
  
  public void decodeData() throws IOException {
	  while (Server.net_server.getConnection(side).getInput().available() > 0) {
		  byte b = Server.net_server.getConnection(side).getInput().readByte();
		  if (b == Server.NEW_DESTINATION_IIIC) {
			  int no = Server.net_server.getConnection(side).getInput().readInt();
			  int x = Server.net_server.getConnection(side).getInput().readInt();
			  int y = Server.net_server.getConnection(side).getInput().readInt();
			  if (Server.objects[no] != null) {
				  Server.objects[no].setDest(x, y);
			  }
			  //System.out.println("Object " + no + " given new destination: "+x+"/"+y);
			  byte cb = Server.net_server.getConnection(side).getInput().readByte();
			  if (cb != Server.CHECK_BYTE) {
				  System.err.println("Error on set dest.");
			  }
		  } else if (b == Server.TARGET_ENEMY) {
			  int no = Server.net_server.getConnection(side).getInput().readInt();
			  int enemy_no = Server.net_server.getConnection(side).getInput().readInt();
			  if (Server.objects[no] != null && Server.objects[enemy_no] != null) {
				  Server.objects[no].shot_target = Server.objects[enemy_no];
				  Server.objects[no].dest_target = Server.objects[enemy_no];
			  }
		  } else if (b == Server.DISCONNECTING) {
			  Server.Quit();
		  } else if (b == Server.CAST_SPELL_IIIB) {
			  C2SCommunication.DecodeCastCurrentSpell(this);
		  } else if (b == Server.NAME) {
			  byte length = Server.net_server.getConnection(side).getInput().readByte();
			  byte ch[] = new byte[length];
			  while (Server.net_server.getConnection(side).getInput().available() < length) {}
			  Server.net_server.getConnection(side).getInput().read(ch, 0, length);
			  name = new String(ch);
			  System.out.println("Player " + name + " joined.");
			  S2CCommunication.SendNameToAllClients(this);
		  } else if (b == Server.INFORMATION_I) {
			  int no = Server.net_server.getConnection(side).getInput().readInt();
			  ServerObject obj = Server.objects[no];
			  if (obj != null) {
				  Server.SendMessage(this.side, obj.getDescriptionText(this.side));
			  }
		  } else {
			  System.err.println("Unknown data ("+b+")");
		  }
	  }
  }
  
  public void process() throws IOException {
    mana++;
    if (send_mana_update.hitInterval()) {
      sendUpdate();
    }
  }

  protected void sendUpdate() throws IOException {
      Server.net_server.getConnection(side).getOutput().writeByte(Server.PLAYER_UPDATE_IC);
      Server.net_server.getConnection(side).getOutput().writeInt(mana);
      Server.net_server.getConnection(side).getOutput().writeByte(Server.CHECK_BYTE);
  }

  protected void sendSpellUpdate() throws IOException {
      Server.net_server.getConnection(side).getOutput().writeByte(Server.SPELLBOOK_UPDATE_IIC);
      Server.net_server.getConnection(side).getOutput().writeInt(spellbook.size());
      for(int s=0 ; s<spellbook.size() ; s++) {
	Spell spl = (Spell)spellbook.get(s);
	Server.net_server.getConnection(side).getOutput().writeInt(spl.type);
      }
      Server.net_server.getConnection(side).getOutput().writeByte(Server.CHECK_BYTE);
  }

}
