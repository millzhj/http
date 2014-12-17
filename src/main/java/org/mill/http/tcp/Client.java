package org.mill.http.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

	public void start() {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 9999);
			socket.setReuseAddress(true);
			OutputStream os = socket.getOutputStream();
			byte[] content = ("this is a message from " + socket.getLocalPort()).getBytes(Charset.forName("UTF-8"));
			os.write(content);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 1000; i++) {
			threadPool.submit(new Runnable() {

				public void run() {
					Client client = new Client();
					client.start();
				}
			});
		}

	}
}
