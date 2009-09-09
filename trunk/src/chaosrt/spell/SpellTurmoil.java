package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;
import chaosrt.Server;
import chaosrt.client.TileMapView;
import chaosrt.server.S2CCommunication;
import chaosrt.server.ServerObject;
import chaosrt.server.ServerPlayer;

public class SpellTurmoil extends Spell {

  private int turmoil_timer=0;

  public SpellTurmoil() {
    super(Spell.TURMOIL, 1000, "Turmoil", 0);
  }

  public boolean cast(ServerPlayer player, int x, int y, boolean illusion) {
    turmoil_timer = Server.objects.length;
    player.mana -= this.cost;
    Server.spells_in_effect.add(this);
    return true;
  }

  public void process() throws IOException {
	  if (turmoil_timer > 0) {
		  turmoil_timer--;
		  // Move random object
		  int obj_no = Functions.rnd(0, Server.objects.length-1);
		  while (Server.objects[obj_no] == null) {
			  obj_no = Functions.rnd(0, Server.objects.length-1);
		  }
		  ServerObject obj = Server.objects[obj_no];
		  S2CCommunication.sendSmokeToAll(obj);
		  // Choose a new location
		  obj.rect.x = Functions.rnd(TileMapView.SQUARE_SIZE, TileMapView.SQUARE_SIZE*(Server.map_width-1));
		  obj.rect.y = Functions.rnd(TileMapView.SQUARE_SIZE, TileMapView.SQUARE_SIZE*(Server.map_height-1));
		  while (obj.isLandClearOfObjects() == false) {
			  obj.rect.x = Functions.rnd(TileMapView.SQUARE_SIZE, TileMapView.SQUARE_SIZE*(Server.map_width-1));
			  obj.rect.y = Functions.rnd(TileMapView.SQUARE_SIZE, TileMapView.SQUARE_SIZE*(Server.map_height-1));
		  }
		  /*      Server.objects[obj_no].rect.x = x;
		   Server.objects[obj_no].rect.y = y;*/
		  obj.stopMoving();
		  //Server.objects[obj_no].setDest(x, y);
		  obj.sendUpdateToAll();
		  S2CCommunication.sendSmokeToAll(obj);
	  } else {
		  Server.spells_in_effect.remove(this);
	  }
	  
  }
  
}
