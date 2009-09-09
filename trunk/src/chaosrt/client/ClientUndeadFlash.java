package chaosrt.client;

import java.awt.*;

import chaosrt.Client;

public class ClientUndeadFlash extends ClientObject {

  private static final int DURATION=500;

  private long start_time;

  public ClientUndeadFlash(Client c, ClientObject undead) {
    super(c, undead.rect.x, undead.rect.y);
    start_time = System.currentTimeMillis();
  }

  public void draw(Graphics g) {
    if (System.currentTimeMillis() - start_time > DURATION) {
      client.other_graphics.remove(this);
    } else {
      g.setColor(Color.white);
      g.drawLine(this.rect.x-Client.view.x_pos, this.rect.y-Client.view.y_pos, this.rect.x-Client.view.x_pos+40, this.rect.y-Client.view.y_pos+40);
    }
  }

}
