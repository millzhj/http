package org.mill.http.file;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class FileServer {

	private ExecutorService threadPool;
	private AtomicInteger treadNum = new AtomicInteger(1);

	public void start() {
		threadPool = Executors.newFixedThreadPool(10, new ThreadFactory() {

			public Thread newThread(Runnable r) {
				Thread t = new Thread(r, "FileServer-Thread-" + treadNum.getAndIncrement());
				return t;
			}
		});
		ServerSocket serverSocket = null;
		try {
			System.out.println("server is starting...");
			serverSocket = new ServerSocket(9999);
			while (true) {
				Socket client = serverSocket.accept();
				FileClientHandler cl = new FileClientHandler(client);
				threadPool.submit(cl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {

			}
		}
	}

	public static void main(String[] args) {
		FileServer server = new FileServer();
		server.start();
	}
}
