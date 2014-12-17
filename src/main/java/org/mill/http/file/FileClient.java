package org.mill.http.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class FileClient {

	public void start() {

		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			socket = new Socket("localhost", 9999);
			in = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
			String content = br.readLine();
			if (content.equals("OK")) {
				System.out.println("connect server ok...\r\n");
				System.out.println("input file name:\r\n");
				Scanner scanner = new Scanner(System.in);
				String fileName = scanner.nextLine();
				File file = new File(fileName);
				String name = file.getName();
				scanner.close();
				out = socket.getOutputStream();
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")));
				bw.write(name + "\r\n");
				bw.flush();
				FileInputStream fis = new FileInputStream(fileName);
				BufferedOutputStream bos = new BufferedOutputStream(out);
				BufferedInputStream bis = new BufferedInputStream(fis);
				byte[] buffer = new byte[512];
				int length = -1;
				while ((length = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, length);
					bos.flush();
				}

				bis.close();
				bos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		FileClient client = new FileClient();
		client.start();
	}
}
