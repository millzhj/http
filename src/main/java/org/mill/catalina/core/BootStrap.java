package org.mill.catalina.core;

public class BootStrap {

	public static void main(String[] args) {
		HttpConnector connector = new HttpConnector();
		connector.start();
	}
}
