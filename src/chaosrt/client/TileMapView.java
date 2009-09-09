package chaosrt.client;

import javax.swing.*;


import java.awt.*;
import java.util.*;

public class TileMapView extends JWindow {

  protected static final int START_OBJECTS = 40;

public static final int SQUARE_SIZE=40;

  public int x_pos, y_pos;
  public ClientObject objects[] = new ClientObject[START_OBJECTS];
  private ClientMapSquare map[][];
  private int map_width, map_height;
  private JFrame owner;
  private ArrayList tmp_selected_objs = new ArrayList();

  public TileMapView(JFrame frame) {
    super(frame);
    this.owner = frame;
  }

  public void draw(Graphics g) {
	  if (this.getWidth() != owner.getWidth() || this.getHeight() != owner.getHeight()) {
		  this.setSize(owner.getWidth(), owner.getHeight());
	  }
	  
	  g.setColor(Color.black);
	  g.fillRect(0, 0, this.getWidth(), this.getHeight());
	  
	  // Draw map first
	  int start_x = Math.max(this.x_pos / SQUARE_SIZE, 0);
	  int start_y = Math.max(this.y_pos / SQUARE_SIZE, 0);
	  for (int y = start_y; y < map_height; y++) {
		  for (int x = start_x; x < map_width; x++) {
			  if (map[x][y] != null) {
				  map[x][y].draw(g);
			  }
		  }
	  }
	  
	  // Draw under-objects
	  for (int i = 0; i < objects.length; i++) {
		  if (objects[i] != null) {
			  if (objects[i].under_object) {
				  objects[i].draw(g);
			  }
		  }
	  }
	  
	  // Draw over-objects
	  for (int i = 0; i < objects.length; i++) {
		  if (objects[i] != null) {
			  if (objects[i].under_object == false) {
				  objects[i].draw(g);
			  }
		  }
	  }
  }
  
  public void setMapSize(int w, int h) {
    map = new ClientMapSquare[w][h];
    map_width = w;
    map_height = h;
  }

  public void setMapSquare(int x, int y, ClientMapSquare sq) {
    map[x][y] = sq;
  }

  public ClientObject getObject(int o) {
    return objects[o];
  }

  public void addObject(ClientObject o, int pos) {
    while (pos >= objects.length) {
      this.increaseObjectsArray(pos);
    }
    objects[pos] = o;
  }

  public void removeObject(int o) {
    objects[o] = null;
  }

  public void hideObject(int o, boolean hide) {
    if (objects[o] != null) {
      objects[o].do_not_draw = hide;
    } else {
      System.err.println("Error: Trying to hide null object.");
    }
  }

  public ClientObject getObjectAtPoint(int x, int y) {
    x += this.x_pos;
    y += this.y_pos;

    for(int i=0 ; i < objects.length ; i++) {
      if (objects[i] != null) {
	if (objects[i].selectable) {
	  if (objects[i].isInPoint(x, y)) {
	    return objects[i];
	  }
	}
      }
    }
    return null;
  }

  public ArrayList getObjectsInRect(int x, int y, int w, int h) {
	  //ArrayList v = new ArrayList();
	  tmp_selected_objs.removeAll(tmp_selected_objs);
	  // Select objects
	  Rectangle area = new Rectangle(x, y, w, h);
	  for (int o = 0; o < objects.length; o++) {
		  if (objects[o] != null) {
			  ClientObject obj = getObject(o);
			  if (obj.selectable) {
				  if (obj.rect.intersects(area)) {
					  tmp_selected_objs.add(obj);
				  }
			  }
		  }
	  }
	  return tmp_selected_objs;
  }
  
  private void increaseObjectsArray(int at_least) {
    //System.out.println("Increasing array size.");
    ClientObject new_array[] = new ClientObject[at_least + START_OBJECTS];
    System.arraycopy(objects, 0, new_array, 0, objects.length);
    objects = new_array;
  }

/*  private void increaseUnderObjectsArray(int at_least) {
    //System.out.println("Increasing array size.");
    ClientObject new_array[] = new ClientObject[at_least + START_OBJECTS];
    System.arraycopy(under_objects, 0, new_array, 0, under_objects.length);
    under_objects = new_array;
  }*/

}
