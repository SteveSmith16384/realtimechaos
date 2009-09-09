package ssmith.net;

import java.net.*;
import java.io.*;

public class NetworkClient2 { //extends Thread {
	private int port;
	public Socket sckClient;
	public boolean connected;
	private DataInputStream bis;
	private DataOutputStream bos;

    public NetworkClient2(int port) {
	this.port = port;
	connected = false;
    }

	public NetworkClient2(Socket sck) throws IOException {
	    sckClient = sck;
	    bis = new DataInputStream(sckClient.getInputStream());
	    bos = new DataOutputStream(sckClient.getOutputStream());
	    connected = true;
	}

    public void connect(String host) throws UnknownHostException, IOException {
	    sckClient = new Socket(host, this.port);
	    bis = new DataInputStream(sckClient.getInputStream());
	    bos = new DataOutputStream(sckClient.getOutputStream());
	    connected = true;
    }

	public void close() throws IOException {
		bis.close();
		bos.flush();
		bos.close();
		sckClient.close();
		connected = false;
	}

    public boolean isConnected() {
	return connected;
    }

	public DataInputStream getInput() {
		return bis;
	}

	public DataOutputStream getOutput() {
		return bos;
	}
    public String getString(int len) throws EOFException, IOException {
	String text = "";
	int pos;
	for (pos=0;pos<len;pos++) {
	    text = text + (char)bis.readByte();
	}
	return text;
    }

    public String getString() throws IOException {
		return getString(bis.available());
    }

	public InetAddress getINetAddress() {
		return sckClient.getInetAddress();
	}

}
