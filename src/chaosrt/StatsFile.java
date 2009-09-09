package chaosrt;

import ssmith.io.*;
import java.io.*;

import chaosrt.client.ClientObject;
import chaosrt.client.TileMapView;
import chaosrt.server.ServerObject;

public class StatsFile {

  private StatsFile() {
  }

  public static void LoadServerStats(ServerObject o) throws IOException {
	  TextFile tf = new TextFile();
	  tf.openFile(Statics.STATS_FILE, TextFile.READ);
	  String line;
	  boolean found = false;
	  while (tf.isEOF() == false) {
		  line = tf.readLine();
		  if (ssmith.lang.Functions.GetParam(2, line, ",").equals(new Integer(o.type).toString())) {
			  found = true;
			  o.att = new Integer(ssmith.lang.Functions.GetParam(3, line, ",")).intValue();
			  o.def = new Integer(ssmith.lang.Functions.GetParam(4, line, ",")).intValue();
			  o.max_health = new Integer(ssmith.lang.Functions.GetParam(5, line, ",")).intValue();
			  //o.max_health = o.max_health * 2; // Make battles last longer
			  o.accuracy = new Integer(ssmith.lang.Functions.GetParam(6, line, ",")).intValue();
			  o.speed = new Double(ssmith.lang.Functions.GetParam(7, line, ",")).doubleValue();
			  o.shot_range = new Integer(ssmith.lang.Functions.GetParam(8, line, ",")).intValue();
			  o.shot_range = o.shot_range * TileMapView.SQUARE_SIZE;
			  o.shot_power = new Integer(ssmith.lang.Functions.GetParam(9, line, ",")).intValue();
			  o.time_takes_to_reload = new Integer(ssmith.lang.Functions.GetParam(10, line, ",")).intValue();
			  o.can_shoot = o.shot_range > 0;
			  o.undead = ssmith.lang.Functions.GetParam(11, line, ",").equals("Y");
			  o.magic_resistance = new Integer(ssmith.lang.Functions.GetParam(12, line, ",")).intValue();
			  o.rect.width = new Integer(ssmith.lang.Functions.GetParam(13, line, ",")).intValue();
			  o.rect.height = new Integer(ssmith.lang.Functions.GetParam(14, line, ",")).intValue();
		  }
	  } if (!found) {
		  System.err.println("Object " + o.type + " not found in stats file.");
	  }
  }
  
  public static void LoadClientStats(ClientObject o, int type) throws IOException {
	  TextFile tf = new TextFile();
	  tf.openFile(Statics.STATS_FILE, TextFile.READ);
	  String line;
	  boolean found = false;
	  while (tf.isEOF() == false) {
		  line = tf.readLine();
		  if (ssmith.lang.Functions.GetParam(2, line, ",").equals(new Integer(type).toString())) {
			  found = true;
			  o.rect.width = new Integer(ssmith.lang.Functions.GetParam(13, line, ",")).intValue();
			  o.rect.height = new Integer(ssmith.lang.Functions.GetParam(14, line, ",")).intValue();
			  o.selectable = ssmith.lang.Functions.GetParam(15, line, ",").equals("Y");
			  o.filename = ssmith.lang.Functions.GetParam(16, line, ",");
			  o.under_object = ssmith.lang.Functions.GetParam(17, line, ",").equals("Y");
		  }
	  } if (!found) {
		  System.err.println("Object " + type + " not found in stats file.");
	  }
  }
  
  public static String GetFilename(int type) throws IOException {
	  TextFile tf = new TextFile();
	  tf.openFile(Statics.STATS_FILE, TextFile.READ);
	  String line, filename = "";
	  boolean found = false;
	  while (tf.isEOF() == false) {
		  line = tf.readLine();
		  if (ssmith.lang.Functions.GetParam(2, line, ",").equals(new Integer(type).toString())) {
			  found = true;
			  filename = ssmith.lang.Functions.GetParam(16, line, ",");
		  }
	  } if (!found) {
		  System.err.println("Object " + type + " not found in stats file.");
	  }
	  return filename;
  }
  
}
