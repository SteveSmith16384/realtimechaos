package ssmith.awt;

import java.awt.*;
import java.io.*;
import java.util.*;

public class ImageCache {

  private static Toolkit tk = Toolkit.getDefaultToolkit();
  private Hashtable hashImages = new Hashtable();
  private MediaTracker mt;
  //private Component comp;

  public ImageCache() {
    // No mediatracker!
  }

  public ImageCache(Component c) {
    //this.comp = c;
    mt = new MediaTracker(c);
  }

  public Image getImage(String filename, boolean wait) {
    Image img = (Image) hashImages.get(filename);
    if (img == null) {
      //System.out.println("Loading image "+filename);
      if (new File(filename).canRead() == false) {
	System.err.println("Cannot find " + filename);
      }
      img = tk.getImage(filename);
      if (mt != null && wait == true) {
	mt.addImage(img, 1);
	try {
	  mt.waitForID(1);
	  //System.out.println("Waiting for "+filename);
	}
	catch (InterruptedException e) {
	  System.err.println("Error loading images: " + e.getMessage());
	}
	mt.removeImage(img);
      }
      hashImages.put(filename, img);
    }
    return img;
  }

  public Image getImage(String filename) {
    /*		if (new File(filename).canRead() == false) {
       throw new Exception("Cannot find "+filename);
      }*/
    return this.getImage(filename, true);
  }

}

