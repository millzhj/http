package org.mill.catalina.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpConnector implements Runnable {

	private boolean stopped = false;

	public static final String HTTP_SCHEMA = "http";

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	public void run() {
		ServerSocket serverSocket = null;
		int port = 8080;
		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));

		} catch (Exception e) {
			e.printStackTrace();
			try {
				serverSocket.close();
			} catch (IOException ioe) {
			}
			System.exit(1);
		}
		while (!stopped) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();

			} catch (Exception e) {
				continue;
			}

			HttpProcesser processer = new HttpProcesser(this);
			processer.process(socket);
		}
	}

	public String getSchema() {
		return HTTP_SCHEMA;
	}

}
