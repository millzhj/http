package org.mill.http.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	/**
	 * 只处理文本消息
	 */
	public void run() {
		InputStream in = null;
		try {
			in = socket.getInputStream();
			byte[] buffer = new byte[512];
			int size = -1;
			StringBuilder sb = new StringBuilder();
			while ((size = in.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, size));
			}
			System.out.println(Thread.currentThread().getName() + " received:" + sb.toString());
			Thread.sleep(2000);
			in.close();
		} catch (Exception e) {
		} finally {
			try {
				in.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
