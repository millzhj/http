package org.mill.catalina.core;

public class StaticResourceProcesser {

	public void process(HttpRequest request, HttpResponse response) {
		response.sendStaticResource();
	}

}
