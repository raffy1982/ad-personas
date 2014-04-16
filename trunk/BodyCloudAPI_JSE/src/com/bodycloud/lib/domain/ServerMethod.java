package com.bodycloud.lib.domain;

public enum ServerMethod {
	GET,
	PUT,
	POST,
	DELETE;
	
	@Override
	public String toString() {
		switch (this) {
		case GET:
			return "GET";
		case PUT:
			return "PUT";
		case POST:
			return "POST";
		case DELETE:
			return "DELETE";
		default:
			return super.toString();
		}
	}
	
}
