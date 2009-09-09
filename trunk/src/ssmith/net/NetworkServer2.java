package ssmith.net;

import java.net.*;
import java.io.*;

public class NetworkServer2 {
	private boolean debug = false;

	private int connections;
	private int port;
	private ServerSocket sckListener;
	private NetworkClient2[] conn;

	/** Creates new Network 
	 * @throws IOException */
	public NetworkServer2(int port, int connections) throws IOException {
		this.port = port;
		this.connections = connections;
		this.conn = new NetworkClient2[connections];
		sckListener = new ServerSocket(this.port);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void acceptUsers() throws IOException {
		int cur;
		for(cur = 0 ; cur < connections ; cur++) {
			acceptUser(cur);
		}
		System.out.println("All clients connected.");
	}

	public void acceptUser(int user) throws IOException {
		if (debug) System.out.println("Server listening on port "+port+"...");
		conn[user] = new NetworkClient2(sckListener.accept());
		if (debug) System.out.println("Client "+user+" connected.");
		/*sckListener.close();
		sckListener = null;*/
	}

	public void closeAll() {
		int cur;
		for(cur = 0 ; cur < connections ; cur++) {
			close(cur);
		}
	}

	private void close(int user) {
		try {
			conn[user].close();
			sckListener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getConnections() {
		return connections;
	}

	public NetworkClient2 getConnection(int user) {
		return conn[user];
	}
}
