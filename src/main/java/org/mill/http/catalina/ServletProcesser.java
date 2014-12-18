package org.mill.http.catalina;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ServletProcesser {

	public void process(ServletRequest servletRequest, ServletResponse servletResponse) {
		Request request = (Request) servletRequest;
		String uri = request.getUri();
		String servletName = uri.substring(uri.lastIndexOf("/") + 1);
		URLClassLoader loader = null;
		try {
			URL[] urls = new URL[1];
			File classPath = new File(HttpServer.WEB_ROOT);
			urls[0] = classPath.toURI().toURL();
			loader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Class<? extends Servlet> servletClass = (Class<? extends Servlet>) loader.loadClass(servletName);
			Servlet servlet = servletClass.newInstance();
			servlet.service(servletRequest, servletResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
