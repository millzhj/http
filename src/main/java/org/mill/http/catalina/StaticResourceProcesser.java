package org.mill.http.catalina;

public class StaticResourceProcesser {

	public void process(Request request, Response response) {
		response.sendStaticResource();
	}

}
