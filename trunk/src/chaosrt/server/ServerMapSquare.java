package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerMapSquare {

  public int x, z, strength, type;
  public boolean blocks_view;

  public ServerMapSquare(int x_pos, int z_pos, int t, int n) throws IOException {
    x = x_pos;
    z = z_pos;
    setType(t, false);
  }

  public void setType(int t, boolean update) throws IOException {
    type = t;
    if (t == Server.WALL) {
      strength = 5;
      blocks_view = true;
    } else if (t == Server.TREE) {
      strength = 3;
      blocks_view = true;
    } else if (t == Server.ROAD) {
      strength = 1;
      blocks_view = false;
    } else if (t == Server.MUD) {
      strength = 1;
      blocks_view = false;
    } else if (t == Server.WATER) {
      strength = 1;
      blocks_view = false;
    } else if (t == Server.GRASS) {
      strength = 1;
      blocks_view = false;
    } else {
      System.err.println("Unknown map square for strength ("+t+")");
      strength = 1;
    }
    if (update) {
      sendUpdate();
    }
  }

  public void damaged(int amt) throws IOException {
    strength -= amt;
    if (strength <= 0) {
      setType(Server.GRASS, true);
    }
  }

  private void sendUpdate() throws IOException {
    for(byte p=0 ; p<Server.players.length ; p++) {
	Server.net_server.getConnection(p).getOutput().writeByte(Server.MAPSQUARE_UPDATE);
	Server.net_server.getConnection(p).getOutput().writeInt(x);
	Server.net_server.getConnection(p).getOutput().writeInt(z);
	Server.net_server.getConnection(p).getOutput().writeInt(type);
	Server.net_server.getConnection(p).getOutput().writeByte(Server.CHECK_BYTE);
    }

  }

  // todo - this
  public double getMoveSpeed(double speed) {
      return speed;
  }

}
