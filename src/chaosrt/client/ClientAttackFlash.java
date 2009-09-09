package chaosrt.client;

import java.awt.*;

import chaosrt.Client;

public class ClientAttackFlash extends ClientObject {

  private static final int IMG_DELAY_TIME=50;

  private int img_no = 1;
  private long start_time;

  public ClientAttackFlash(Client c, int x, int y) {
    super(c, x, y);
    start_time = System.currentTimeMillis();
  }

  public void draw(Graphics g) {
    if (System.currentTimeMillis() - start_time > IMG_DELAY_TIME) {
      start_time = System.currentTimeMillis();
      img_no++;
    }
    if (img_no > 0 && img_no < 4) {
      g.drawImage(Client.GetImage("Bullet_flash" + img_no + ".gif"), this.rect.x - Client.view.x_pos, this.rect.y - Client.view.y_pos, client);
    } else {
      client.other_graphics.remove(this);
    }
  }

}

