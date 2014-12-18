package org.mill.http.catalina;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {

	private InputStream in;

	private String uri;

	private String method;

	private String protocol;

	public Request(InputStream in) {
		this.in = in;
	}

	public void parse() {
		StringBuilder sb = new StringBuilder();
		byte[] buffer = new byte[2048];
		int size = 0;
		try {
			size = in.read(buffer);
			while (size != -1) {
				for (int i = 0; i < size; i++) {
					sb.append((char) i);
				}
				if (size >= 2048) {
					size = in.read(buffer);
				} else {
					size = -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			size = -1;
		}
		String requestString = sb.toString();
		parseUri(requestString);
	}

	private void parseUri(String requestString) {
		String[] requestParts = requestString.split(" ");
		this.method = requestParts[0].trim();
		this.uri = requestParts[1].trim();
		this.protocol = requestParts[2].trim();
	}

	public String getMethod() {
		return method;
	}

	public String getUri() {
		if (uri == null)
			return "";
		return uri;
	}

}
