package chaosrt.client;

import java.awt.*;

import chaosrt.Client;

public class ClientSmoke extends ClientObject {

  private int max_size=20;
  private double size=0;

  public ClientSmoke(Client client, int x, int y) {
    super(client, x, y);
    //super(client, 0, 0, 0, 0, Server.SMOKE, false, "", x, y, false);
  }

  public void draw(Graphics g) {
    g.setColor(Color.lightGray);
    int x = this.rect.x - Client.view.x_pos - (int)size;
    int y = this.rect.y - Client.view.y_pos - (int)size;
    for(int i=0 ; i<3 ; i++) {
      g.drawOval(x-i, y-i, ((int)size * 3)+i, ((int)size * 3)+i);
    }

    size += 0.5f;
    if (this.size > max_size) {
      client.other_graphics.remove(this);
    }
  }

}
