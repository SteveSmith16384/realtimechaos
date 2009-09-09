package chaosrt.client;

import ssmith.lang.*;
import java.awt.*;

import chaosrt.Client;

public class ClientLighteningBolt extends ClientObject {

  private static final int DURATION=200, FX_RADIUS=4;

  private long start_time;
  private int x2, y2;
  private Color col;

  public ClientLighteningBolt(Client client, int x1, int y1, int x2, int y2, int r, int g, int b) {
    super(client, x1, y1);
    this.x2 = x2;
    this.y2 = y2;
    start_time = System.currentTimeMillis();
    col = new Color(r, g, b);

  }

  public void draw(Graphics g) {
    if (System.currentTimeMillis() - start_time > DURATION) {
      client.other_graphics.remove(this);
    } else {
      g.setColor(col);
      g.drawLine(this.rect.x-Client.view.x_pos, this.rect.y-Client.view.y_pos, x2-Client.view.x_pos+Functions.rnd(-FX_RADIUS, FX_RADIUS), y2-Client.view.y_pos+Functions.rnd(-FX_RADIUS, FX_RADIUS));
    }
  }

}
