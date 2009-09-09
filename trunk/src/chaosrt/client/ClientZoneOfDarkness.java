package chaosrt.client;

import java.awt.*;

import chaosrt.Client;

public class ClientZoneOfDarkness extends ClientObject {

  public static final int DURATION=40000, FX_RADIUS = 500;

  private long start_time;
  //private int x2, y2;

  public ClientZoneOfDarkness(Client client, int x, int y) {
    super(client, x, y);
    start_time = System.currentTimeMillis();
  }

  public void draw(Graphics g) {
    if (System.currentTimeMillis() - start_time > DURATION) {
      client.other_graphics.remove(this);
    } else {
      g.setColor(Color.black);
      g.fillOval(this.rect.x-Client.view.x_pos, this.rect.y-Client.view.y_pos, FX_RADIUS, FX_RADIUS);
    }
  }

}
