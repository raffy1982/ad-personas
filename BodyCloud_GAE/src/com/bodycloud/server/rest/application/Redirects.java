package com.bodycloud.server.rest.application;

import java.util.HashMap;
import java.util.Map;

public class Redirects {
	
	private static final int SOURCE = 0;
	private static final int DESTINATION = 1;
	
	private static final String[][] redirects = {
		
		
		
	};
	
	private static Map<String, String> getRedirects(int k, int v) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < redirects.length; i++) {
			map.put(redirects[i][k], redirects[i][v]);
		}
		return map;
	}
	
	public static Map<String, String> getRedirects() {
		return getRedirects(SOURCE, DESTINATION);
	}
	
	public static Map<String, String> getSources() {
		return getRedirects(DESTINATION, SOURCE);
	}
	
	private static String fixUrl(String url, Map<String, String> mapping) {
		for (String k : mapping.keySet()) {
			if (url.endsWith(k))
				return url.replace(k, mapping.get(k));
		}
		return url;
	}
	
	public static String getDestinationUrl(String source) {
		return fixUrl(source, getRedirects());
	}
	
	public static String getSourceUrl(String destination) {
		return fixUrl(destination, getSources());
	}

}
