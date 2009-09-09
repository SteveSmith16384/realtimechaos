package chaosrt.client;

import java.awt.*;

import chaosrt.Client;

public class ClientExplosion extends ClientObject {

  private int size=0, max_size;

  public ClientExplosion(Client client, int x, int y, int max) {
    super(client, x, y);
    //super(client, -1, 0, 0, 0, Server.EXPLOSION, false, "", x, y, false);
    this.max_size = max;
  }

  public void draw(Graphics g) {
    g.setColor(Color.yellow);
    int x = this.rect.x - Client.view.x_pos - size;
    int y = this.rect.y - Client.view.y_pos - size;
    for(int i=0 ; i<10 ; i++) {
      g.drawOval(x-i, y-i, (size * 2)+i, (size * 2)+i);
    }

    size += 2;
    if (this.size > max_size) {
      client.other_graphics.remove(this);
    }
  }

}
