package org.mill.http.catalina;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

	public static final String WEB_ROOT = System.getProperty("user.idr") + File.separator + "webroot";

	public static final String SHUTDOWN_COMMAND = "/shutdown";

	private boolean shutdown = false;

	public void await() {
		ServerSocket serverSocket = null;
		int port = 8080;
		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("localhost"));
			while (!shutdown) {
				Socket socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				Request request = new Request(in);
				request.parse();
				Response response = new Response(out);
				response.setRequest(request);
				response.sendStaticResource();
				socket.close();
				shutdown = request.getUri().equalsIgnoreCase(SHUTDOWN_COMMAND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		server.await();
	}
}
