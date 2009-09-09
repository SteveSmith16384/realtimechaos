package chaosrt.client;

import java.io.FileNotFoundException;
import java.io.IOException;

import ssmith.io.*;

public class ClientSettings {
	
	private static final String FILENAME = "client_settings.dat";
	
	public String ip_address = "", player_name = "";

	public ClientSettings() {
		super();
		TextFile tf = new TextFile();
		try {
			tf.openFile(FILENAME, TextFile.READ);
			this.ip_address = tf.readLine();
			this.player_name = tf.readLine();
			tf.close();
		} catch (FileNotFoundException e) {
			// Nothing
		} catch (IOException e) {
			// Nothing
		}
		
	}
	
	public void saveSettings(String ip, String name) {
		TextFile tf = new TextFile();
		try {
			tf.openFile(FILENAME, TextFile.WRITE);
			tf.writeLine(ip);
			tf.writeLine(name);
			tf.close();
		} catch (FileNotFoundException e) {
			// Nothing
		} catch (IOException e) {
			// Nothing
		}
	}

}
