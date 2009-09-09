package chaosrt.server;

import java.awt.Color;
import java.io.IOException;

import chaosrt.Client;
import chaosrt.Server;
import chaosrt.client.ClientLighteningBolt;
import chaosrt.client.ClientObject;
import chaosrt.client.ClientWizard;

public class S2CCommunication {

	private S2CCommunication() {
	}

	public static synchronized void SendGameData() throws IOException {
	    // Send it to all players
	    for (byte p = 0; p < Server.players.length ; p++) {
	    	  Server.net_server.getConnection(p).getOutput().writeByte(Server.NO_OF_PLAYERS);
	    	  Server.net_server.getConnection(p).getOutput().writeByte(Server.players.length);
	    	  Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
	      }
	  }

	  public static synchronized void DecodeGameData(Client client) throws IOException {
		  client.player_names = new String[client.net_client.getInput().readByte()];
		  
		  byte check = client.net_client.getInput().readByte();
		  if (check != Server.CHECK_BYTE) { 
			  System.err.println("Error on players nos receive.");
		  }
	  }
	
	public static synchronized void SendNameToAllClients(ServerPlayer pl) throws IOException {
		    // Send it to all players
		    for (byte p = 0; p < Server.players.length ; p++) {
		    	  Server.net_server.getConnection(p).getOutput().writeByte(Server.PLAYERS_NAME);
		    	  Server.net_server.getConnection(p).getOutput().writeByte(pl.side);
		    	  Server.net_server.getConnection(p).getOutput().writeByte(pl.name.length());
		    	  Server.net_server.getConnection(p).getOutput().writeBytes(pl.name);
		    	  Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		      }
		  }

	  public static synchronized void DecodeNameFromServer(Client client) throws IOException {
		  byte side = client.net_client.getInput().readByte(); 
		  byte length = client.net_client.getInput().readByte();
		  byte ch[] = new byte[length];
		  while (client.net_client.getInput().available() < length) {}
		  client.net_client.getInput().read(ch, 0, length);
		  client.player_names[side] = new String(ch);
		  System.out.println("Player " + client.player_names[side] + " joined.");
		  
		  for(int o=0 ; o<Client.view.objects.length ; o++) {
			  ClientObject obj = (ClientObject)Client.view.objects[o];
			  if (obj instanceof ClientWizard) {
				  if (obj.side == side) {
					  ClientWizard wiz = (ClientWizard)obj;
					  wiz.name = client.player_names[side];
					  break;
				  }
			  }
		  }
		  
		  byte check = client.net_client.getInput().readByte();
		  if (check != Server.CHECK_BYTE) { 
			  System.err.println("Error on players name receive.");
		  }
	  }

	  public static synchronized void sendExplosionToAll(ServerObject o, int size) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.EXPLOSION_IIIC);
		      Server.net_server.getConnection(p).getOutput().writeInt((int)o.rect.x);
		      Server.net_server.getConnection(p).getOutput().writeInt((int)o.rect.y);
		      Server.net_server.getConnection(p).getOutput().writeInt(size);
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		    }
		  }

	  public static synchronized  void sendSmokeToAll(ServerObject o) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CMD_SMOKE);
		      Server.net_server.getConnection(p).getOutput().writeInt((int)(o.rect.x + (o.rect.width/2)));
		      Server.net_server.getConnection(p).getOutput().writeInt((int)(o.rect.y + (o.rect.height/2)));
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		    }
		  }

	  public static synchronized  void sendBulletFlashToAll(ServerObject o) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.BULLET_FLASH);
		      Server.net_server.getConnection(p).getOutput().writeInt((int)o.rect.x);
		      Server.net_server.getConnection(p).getOutput().writeInt((int)o.rect.y);
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		    }
		  }

	  public static synchronized  void sendAttackFlashToAll(ServerObject o, ServerObject obj) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.ATTACK_FLASH_IIC);
		      Server.net_server.getConnection(p).getOutput().writeInt(o.no);
		      Server.net_server.getConnection(p).getOutput().writeInt(obj.no);
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		    }
		  }

		  /**
		   * Use this if a creature has changed side.
		   */
	  public static synchronized  void sendSideToAll(ServerObject o) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.NEW_SIDE_II);
		      Server.net_server.getConnection(p).getOutput().writeInt(o.no);
		      Server.net_server.getConnection(p).getOutput().writeInt(o.side);
		    }
		  }

	  public static synchronized void sendLighteningBoltToAll(ServerObject o, int x, int y, Color col) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.LIGHTENING_BOLT_IIIIIIIC);
		      Server.net_server.getConnection(p).getOutput().writeInt((int)(o.rect.x + (o.rect.width/2)));
		      Server.net_server.getConnection(p).getOutput().writeInt((int)(o.rect.y + (o.rect.height/2)));
		      Server.net_server.getConnection(p).getOutput().writeInt(x);
		      Server.net_server.getConnection(p).getOutput().writeInt(y);
		      Server.net_server.getConnection(p).getOutput().writeInt(col.getRed());
		      Server.net_server.getConnection(p).getOutput().writeInt(col.getGreen());
		      Server.net_server.getConnection(p).getOutput().writeInt(col.getBlue());
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		    }
		  }

	  public static synchronized void DecodeLighteningBolt(Client client) throws IOException {
		int x1 = client.net_client.getInput().readInt();
		int y1 = client.net_client.getInput().readInt();
		int x2 = client.net_client.getInput().readInt();
		int y2 = client.net_client.getInput().readInt();
		int r = client.net_client.getInput().readInt();
		int g = client.net_client.getInput().readInt();
		int blue = client.net_client.getInput().readInt();
		client.other_graphics.add(new ClientLighteningBolt(client, x1, y1,
				x2, y2, r, g, blue));
		byte check = client.net_client.getInput().readByte();
		if (check != Server.CHECK_BYTE) {
			System.err.println("Error on checksum on lightening bolt.");
		}
	  }
	  
		public static synchronized  void sendZoneOfDarknessToAll(ServerObject o, int x, int y) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.ZONE_OF_DARKNESS_IIC);
		      Server.net_server.getConnection(p).getOutput().writeInt(x);
		      Server.net_server.getConnection(p).getOutput().writeInt(y);
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		    }
		  }

	  public static synchronized  void sendUndeadFlashToAll(ServerObject o) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.UNDEAD_FLASH_IC);
		      Server.net_server.getConnection(p).getOutput().writeInt(o.no);
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
		    }
		  }

		  public static synchronized  void sendUndeadToAll(ServerObject o) throws IOException {
		    for (byte p = 0; p < Server.players.length ; p++) {
		      Server.net_server.getConnection(p).getOutput().writeByte(Server.UNDEAD_IB);
		      Server.net_server.getConnection(p).getOutput().writeInt(o.no);
		      Server.net_server.getConnection(p).getOutput().writeBoolean(o.undead);
		    }
		  }

}
