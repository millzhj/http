package org.mill.http.catalina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleHttpRequest {

	public static void main(String[] args) throws Exception {

		String host = "www.baidu.com";
		int port = 80;

		Socket socket = new Socket(host, port);
		OutputStream os = socket.getOutputStream();
		boolean autoflash = true;
		PrintWriter pw = new PrintWriter(os, autoflash);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw.println("GET / HTTP/1.1");
		// pw.println("HOST:localhost");
		pw.println("Connection:close");// 表示知连接 默认是长连接 keep-alive
		pw.println();

		boolean loop = true;
		StringBuilder sb = new StringBuilder();
		while (loop) {
			if (br.ready()) {
				int i = 0;
				while ((i = br.read()) != -1) {
					sb.append((char) i);
					// System.out.println(sb.toString());
				}
				loop = false;
			}
			// Thread.sleep(50);
		}
		System.out.println(sb.toString());
		socket.close();
	}
}
