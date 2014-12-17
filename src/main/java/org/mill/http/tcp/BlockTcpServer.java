package org.mill.http.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockTcpServer {

	private ExecutorService threadPool;
	private AtomicInteger treadNum = new AtomicInteger(1);

	public void start() {
		threadPool = Executors.newCachedThreadPool(new ThreadFactory() {

			public Thread newThread(Runnable r) {
				Thread t = new Thread(r, "Block-Thread-" + treadNum.getAndIncrement());
				return t;
			}
		});
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9999);
			while (true) {
				Socket client = serverSocket.accept();
				ClientHandler cl = new ClientHandler(client);
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
}
