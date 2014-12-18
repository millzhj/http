package org.mill.http.catalina;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class Response {

	private OutputStream out;
	private Request request;

	public Response(OutputStream out) {
		this.out = out;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void sendStaticResource() {
		String fileName = HttpServer.WEB_ROOT + request.getUri();
		File file = new File(fileName);
		try {
			if (file.exists()) {
				StringBuilder message = new StringBuilder("HTTP/1.1 200 OK\r\n").append("Content-Type:text/html\r\n")
						.append("Content-Length:23/html\r\n")
						.append("\r\n");
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				byte[] buffer = new byte[1024];
				int size = bis.read(buffer);
				out.write(message.toString().getBytes());
				while (size != -1) {
					out.write(buffer, 0, size);
					size = bis.read(buffer);
				}
				bis.close();
				out.flush();
				out.close();
			} else {
				StringBuilder errorMessage = new StringBuilder("HTTP/1.1 404 File Not Found\r\n").append("Content-Type:text/html\r\n")
						.append("Content-Length:23/html\r\n")
						.append("\r\n")
						.append("<h1>File Not Found</h1>");
				out.write(errorMessage.toString().getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
