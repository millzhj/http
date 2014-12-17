package org.mill.http.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScanHost {

	public void start() {

		for (int i = 1; i <= 65535; i++) {
			try {
				Socket socket = new Socket("localhost", i);
				System.out.println("there is server on " + i);
				socket.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ScanHost scan = new ScanHost();
		scan.start();
	}
}
