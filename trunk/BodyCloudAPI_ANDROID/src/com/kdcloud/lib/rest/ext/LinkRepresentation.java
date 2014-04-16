package com.kdcloud.lib.rest.ext;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

public class LinkRepresentation extends StringRepresentation {
	
	private static final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String pattern = "<%name href=\"%href\"/>";

	private static String buildXml(String name, String href) {
		return header + pattern.replace("%name", name).replace("%href", href);
	}
	
	public LinkRepresentation(String name, String href) {
		super(buildXml(name, href));
		setMediaType(MediaType.TEXT_XML);
	}

}
