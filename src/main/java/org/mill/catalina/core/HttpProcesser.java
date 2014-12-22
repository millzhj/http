package org.mill.catalina.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.servlet.ServletException;

import org.mill.http.catalina.ServletProcesser;

public class HttpProcesser {

	private HttpRequest request;
	private HttpResponse response;
	private HttpConnector connector;
	private HttpRequestLine requestline = new HttpRequestLine();

	public HttpProcesser(HttpConnector connector) {
		this.connector = connector;
	}

	public void process(Socket socket) {
		SocketInputStream input = null;
		OutputStream output = null;
		try {
			input = new SocketInputStream(socket.getInputStream(), 2048);
			output = socket.getOutputStream();
			request = new HttpRequest(input);
			response = new HttpResponse(output);
			response.setRequest(request);
			parseRequest(input, output);
			parseResponse(input);
			response.setHeader("server", "toy servlet container.");
			if (request.getRequestUri().startsWith("/servlet/")) {
				ServletProcesser processer = new ServletProcesser();
				processer.process(request, response);

			} else {
				StaticResourceProcesser staticResourceProcesser = new StaticResourceProcesser();
				staticResourceProcesser.process(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void parseResponse(SocketInputStream input) {
		// TODO Auto-generated method stub

	}

	private void parseRequest(SocketInputStream input, OutputStream output) throws IOException, ServletException {
		//解析HTTP请求方法，协议 ，请求参数，请求地址,请求的sessionId
		input.readRequestLine(requestline);
		String method = new String(requestline.method, 0, requestline.methodEnd);
		String uri = null;
		String protocol = new String(requestline.protocol, 0, requestline.protocolEnd);
		if (method.length() < 1) {
			throw new ServletException("Miss Http Request Method");
		}

		if (requestline.uriEnd < 1) {
			throw new ServletException("Miss Http Request Uri");
		}
		int question = requestline.indexOf("?");
		if (question >= 0) {
			request.setQueryString(new String(requestline.uri, question + 1, requestline.uriEnd - question - 1));
			uri = new String(requestline.uri, 0, question);
		} else {
			request.setQueryString(null);
			uri = new String(requestline.uri, 0, requestline.uriEnd);
		}

		// 检查是否是绝对路径
		if (!uri.startsWith("/")) {
			int pos = uri.indexOf("://");
			if (pos != -1) {
				pos = uri.indexOf("/", pos + 3);
				if (pos == -1) {
					uri = "";
				} else {
					uri = uri.substring(pos);
				}
			}
		}

		String match = ";jsessionid=";
		int semicolon = uri.indexOf(match);
		if (semicolon >= 0) {
			//
			String rest = uri.substring(semicolon + match.length());
			int semicolon2 = rest.indexOf(";");
			if (semicolon2 >= 0) {
				request.setReqestSessionId(uri.substring(0, semicolon2));
				rest = rest.substring(semicolon2);
			} else {
				request.setReqestSessionId(rest);
				rest = "";
			}

			uri = uri.substring(0, semicolon) + rest;
		} else {
			request.setReqestSessionId(null);
			request.setReqestSessionURL(false);
		}

		request.setMethod(method);
		request.setProtocol(protocol);

		String nomalizedUrl = normalize(uri);
		if (nomalizedUrl != null) {
			request.setRequestURI(nomalizedUrl);
		} else {
			throw new ServletException("Invalid URL:" + uri);
		}

	}

	protected String normalize(String path) {
		if (path == null)
			return null;
		// Create a place for the normalized path
		String normalized = path;

		// Normalize "/%7E" and "/%7e" at the beginning to "/~"
		if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
			normalized = "/~" + normalized.substring(4);

		// Prevent encoding '%', '/', '.' and '\', which are special reserved
		// characters
		if ((normalized.indexOf("%25") >= 0) || (normalized.indexOf("%2F") >= 0) || (normalized.indexOf("%2E") >= 0)
				|| (normalized.indexOf("%5C") >= 0) || (normalized.indexOf("%2f") >= 0) || (normalized.indexOf("%2e") >= 0)
				|| (normalized.indexOf("%5c") >= 0)) {
			return null;
		}

		if (normalized.equals("/."))
			return "/";

		// Normalize the slashes and add leading slash if necessary
		if (normalized.indexOf('\\') >= 0)
			normalized = normalized.replace('\\', '/');
		if (!normalized.startsWith("/"))
			normalized = "/" + normalized;

		// Resolve occurrences of "//" in the normalized path
		while (true) {
			int index = normalized.indexOf("//");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 1);
		}

		// Resolve occurrences of "/./" in the normalized path
		while (true) {
			int index = normalized.indexOf("/./");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 2);
		}

		// Resolve occurrences of "/../" in the normalized path
		while (true) {
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return (null); // Trying to go outside our context
			int index2 = normalized.lastIndexOf('/', index - 1);
			normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
		}

		// Declare occurrences of "/..." (three or more dots) to be invalid
		// (on some Windows platforms this walks the directory tree!!!)
		if (normalized.indexOf("/...") >= 0)
			return (null);

		// Return the normalized path that we have completed
		return (normalized);
	}

}
