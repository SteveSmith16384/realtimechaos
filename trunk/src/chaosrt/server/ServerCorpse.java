package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerCorpse extends ServerObject {

  private ServerPerson original;

  public ServerCorpse(ServerPerson person) throws IOException {
    super("Corpse", Server.CORPSE, person.rect.x, person.rect.y, person.side, false);
    this.original = person;
    is_valid_target = false;
  }

  public void resurrect(int side) throws IOException {
    this.remove();
    int slot = Server.GetNextFreeSlot();
    original.no = slot;
    Server.objects[slot] = original;
    original.undead = true;
    original.side = side;
    original.destroyed = false;
    original.current_health = original.max_health;
    original.sendNewObjectToAll();
    original.addToMap();
//    original.sendUpdateToAll();
    S2CCommunication.sendUndeadToAll(original);
  }

  public void hasCollidedWith(ServerObject o) throws IOException {
  }

  public boolean collidedWith(ServerObject other) {
    return false;
  }

  public boolean canTraverse(int type) {
    return false;
  }

  protected String getUpdateText() {
    return "";
  }

}
