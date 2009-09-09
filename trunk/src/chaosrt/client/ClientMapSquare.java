package chaosrt.client;

import java.awt.*;

import chaosrt.Client;

public class ClientMapSquare {

  private String filename;
  private int x, y;

  public ClientMapSquare(Client c, int map_x, int map_y, String filename) {
    this.filename = filename;
    this.x = map_x*TileMapView.SQUARE_SIZE;
    this.y = map_y*TileMapView.SQUARE_SIZE;
    //super(view, imgCache, map_x, map_y, view.square_size, view.square_size, filename, false, false, 0);
    //super(c, 0, 0, TileMapView.SQUARE_SIZE, TileMapView.SQUARE_SIZE, 0, false, (byte)99, filename, map_x*TileMapView.SQUARE_SIZE, map_y*TileMapView.SQUARE_SIZE, false);
  }

  public void draw(Graphics g) {
    int x = (this.x) - Client.view.x_pos;
    int y = (this.y) - Client.view.y_pos;

    if (x>=-TileMapView.SQUARE_SIZE && x <= Client.view.getWidth()) {
      if (y >=-TileMapView.SQUARE_SIZE && y <= Client.view.getHeight()) {
	g.drawImage(Client.GetImage(this.filename + ".gif"), x, y, Client.view);
      }
    }
  }

}
