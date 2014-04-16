package com.bodycloud.server.rest.application;

public class UrlHelper {
	
	public static boolean hasExtension(String url) {
		return url.matches(".*\\.\\w+$");
	}
	
	public static String replaceId(String url, String id) {
		return url.replace("{id}", id);
	}

}
