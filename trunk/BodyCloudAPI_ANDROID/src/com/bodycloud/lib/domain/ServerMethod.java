package com.bodycloud.lib.domain;

public enum ServerMethod {
	GET,
	PUT,
	POST,
	DELETE;
	
	@Override
	public String toString() {
		switch (this) {
		case GET: return "GET";
		case PUT: return "PUT";
		case POST: return "POST";
		case DELETE: return "DELETE";
		default: return super.toString();
		}
	}

	public static ServerMethod getByString(String method) {
		if (method != null) {
			if (method.equalsIgnoreCase("GET")) return GET;
			if (method.equalsIgnoreCase("PUT")) return PUT;
			if (method.equalsIgnoreCase("POST")) return POST;
			if (method.equalsIgnoreCase("DELETE")) return DELETE;
		}
		return null;
	}
	
}
