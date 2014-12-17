package org.mill.http.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class FileClientHandler implements Runnable {

	private Socket socket;

	public String dir = "D:/temp/";

	public FileClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {

		OutputStream os = null;
		InputStream is = null;
		try {
			os = socket.getOutputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
			bw.write("OK\r\n");
			bw.flush();
			is = socket.getInputStream();
			int size = 0;
			byte[] buffer = new byte[2048];
			// 第一行为文件名称
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String fileName = br.readLine();
			System.out.println("received file name:" + fileName);
			// 第二行为数字签名 TODO
			File outFile = new File(dir + fileName);
			outFile.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
			while ((size = is.read(buffer)) != -1) {
				bos.write(buffer, 0, size);
				bos.flush();
			}
			bos.close();
			System.out.println("finish receive file:" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
				socket.close();
			} catch (Exception e) {
			}
		}
	}
}
