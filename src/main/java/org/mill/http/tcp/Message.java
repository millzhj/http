package org.mill.http.tcp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Message<T> {

	private Map<String, String> headers = new HashMap<String, String>();
	private T payload;

	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public void addHeaders(Map<String, String> headers) {
		headers.putAll(headers);
	}

	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

}
