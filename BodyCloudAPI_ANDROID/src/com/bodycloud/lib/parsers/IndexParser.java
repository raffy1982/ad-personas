package com.bodycloud.lib.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bodycloud.lib.domain.Index;

import android.util.Xml;

public class IndexParser {

	private String strToParse = null;
	
	public IndexParser(String toParse) {
		this.strToParse = toParse;
	}
	   
	public Index parse() throws XmlPullParserException, IOException {
		
		Index idx = new Index();
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			
		InputStream is = new ByteArrayInputStream(this.strToParse.getBytes());
		
		parser.setInput(is, null);
		parser.nextTag();
		
		parser.require(XmlPullParser.START_TAG, null, "index");
		
		while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the 'item' tag
	        if (name.equals("item")) {
	            
	        	String referenceUrl = null;
	        	String metadataUrl = null;
	        	
	        	parser.require(XmlPullParser.START_TAG, null, "item");
	            while (parser.next() != XmlPullParser.END_TAG) {
	                if (parser.getEventType() != XmlPullParser.START_TAG) {
	                    continue;
	                }
	                name = parser.getName();
	                if (name.equals("reference")) {
	                    parser.require(XmlPullParser.START_TAG, null, "reference");
	                    referenceUrl = parser.getAttributeValue(null, "href");
	                    parser.nextTag();
	                    //parser.require(XmlPullParser.END_TAG, null, "reference");
	                	
	                } else if (name.equals("metadata")) {
	                	parser.require(XmlPullParser.START_TAG, null, "metadata");
	                    metadataUrl = parser.getAttributeValue(null, "href");
	                    parser.nextTag();
	                    //parser.require(XmlPullParser.END_TAG, null, "metadata");
	                } else {
	                    skip(parser);
	                }
	            }
	        	
	        	idx.add(referenceUrl, metadataUrl);
	        	
	        } else {
	            skip(parser);
	        }
	    }  
			
		is.close();
		 
		
		return idx;
	} 
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
	
	
}
