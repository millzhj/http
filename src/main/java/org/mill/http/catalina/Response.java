package org.mill.http.catalina;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class Response implements ServletResponse {

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

	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public PrintWriter getWriter() throws IOException {
		PrintWriter writer = new PrintWriter(out);
		return writer;
	}

	public void setCharacterEncoding(String charset) {
		// TODO Auto-generated method stub

	}

	public void setContentLength(int len) {
		// TODO Auto-generated method stub

	}

	public void setContentType(String type) {
		// TODO Auto-generated method stub

	}

	public void setBufferSize(int size) {
		// TODO Auto-generated method stub

	}

	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reset() {
		// TODO Auto-generated method stub

	}

	public void setLocale(Locale loc) {
		// TODO Auto-generated method stub

	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}
}
