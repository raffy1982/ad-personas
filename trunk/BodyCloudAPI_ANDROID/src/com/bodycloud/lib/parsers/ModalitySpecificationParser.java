package com.bodycloud.lib.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bodycloud.lib.domain.DataSpecification;
import com.bodycloud.lib.domain.ModalitySpecification;
import com.bodycloud.lib.domain.ServerAction;
import com.bodycloud.lib.domain.ServerMethod;
import com.bodycloud.lib.domain.ServerParameter;

import android.util.Xml;

public class ModalitySpecificationParser {

	private String strToParse = null;
	
	public ModalitySpecificationParser(String toParse) {
		this.strToParse = toParse;
	}
	   
	public ModalitySpecification parse(String modalityName) throws XmlPullParserException, IOException {
		
		ModalitySpecification spec = new ModalitySpecification();
		spec.setName(modalityName);
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			
		InputStream is = new ByteArrayInputStream(this.strToParse.getBytes());
		
		parser.setInput(is, null);
		parser.nextTag();

		parser.require(XmlPullParser.START_TAG, null, "modality");
		
		while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("inputSpecification")) {
                    parser.require(XmlPullParser.START_TAG, null, "inputSpecification");
                    DataSpecification inputSpecification = new DataSpecification();
                    while (parser.next() != XmlPullParser.END_TAG) {
    	                if (parser.getEventType() != XmlPullParser.START_TAG) {
    	                    continue;
    	                }
    	                name = parser.getName();
    	                if (name.equals("column")) {
    	                	parser.require(XmlPullParser.START_TAG, null, "column");

    	                	String colName = null, colType = null, colSource = null;
    	                	while (parser.next() != XmlPullParser.END_TAG) {
    	    	                if (parser.getEventType() != XmlPullParser.START_TAG) {
    	    	                    continue;
    	    	                }
    	    	                name = parser.getName();
    	    	                if (name.equals("name")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "name");
    	    	                    colName = readText(parser);
    	    	                    parser.require(XmlPullParser.END_TAG, null, "name");
    	    	                }
    	    	                else if (name.equals("type")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "type");
    	    	                    colType = readText(parser);
    	    	                    parser.require(XmlPullParser.END_TAG, null, "type");
    	    	                }
    	    	                else if (name.equals("source")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "source");
    	    	                    colSource = readText(parser);
    	    	                    parser.require(XmlPullParser.END_TAG, null, "source");
    	    	                }
    	    	                else {
    	    	                    skip(parser);
    	    	                }    	    	    	                
    	                    }
    	                	inputSpecification.addColumn(colName, DataSpecification.DataType.getByString(colType), DataSpecification.InputSource.getByString(colSource));
    	                }
    	                else if(name.equals("view")){
    	                	parser.require(XmlPullParser.START_TAG, null, "view");
    	                    String view = readText(parser);
    	                    parser.require(XmlPullParser.END_TAG, null, "view");
    	                	inputSpecification.setView(view);
    	                }
    	                else {
    	                    skip(parser);
    	                }
    	                
    	            }
                    spec.setInputSpecification(inputSpecification);
                    parser.require(XmlPullParser.END_TAG, null, "inputSpecification");
                	
                } else if (name.equals("init-action")) {
                    parser.require(XmlPullParser.START_TAG, null, "init-action");
                    ServerAction initAction = new ServerAction();
                    String uri = null, method = null;
                    while (parser.next() != XmlPullParser.END_TAG) {
    	                if (parser.getEventType() != XmlPullParser.START_TAG) {
    	                    continue;
    	                }
    	                name = parser.getName();
    	                if (name.equals("uri")) {
	    	                	parser.require(XmlPullParser.START_TAG, null, "uri");
	    	                	uri = readText(parser);
	    	                    parser.require(XmlPullParser.END_TAG, null, "uri");
    	                }
    	                else if (name.equals("method")) {
	    	                	parser.require(XmlPullParser.START_TAG, null, "method");
	    	                	method = readText(parser);
	    	                    parser.require(XmlPullParser.END_TAG, null, "method");
    	                }
	    	            else {
	    	            	skip(parser);
	    	            }
                    }
                    initAction.setMethod(ServerMethod.getByString(method));
                    initAction.setResourceIdentifier(uri);
                    spec.setInitAction(initAction);
                    
                } 
                else if (name.equals("action")) {

                	parser.require(XmlPullParser.START_TAG, null, "action");
                	ServerAction action = new ServerAction();
                    String uri = null, method = null; 
                    boolean repeat = false;
                    int triggerAfter = 0;
                    while (parser.next() != XmlPullParser.END_TAG) {
    	                if (parser.getEventType() != XmlPullParser.START_TAG) {
    	                    continue;
    	                }
    	                name = parser.getName();
    	                if (name.equals("uri")) {
	    	               	parser.require(XmlPullParser.START_TAG, null, "uri");
	    	               	uri = readText(parser);
	    	               	parser.require(XmlPullParser.END_TAG, null, "uri");
    	                }
    	                else if (name.equals("method")) {
	    	                parser.require(XmlPullParser.START_TAG, null, "method");
	    	                method = readText(parser);
	    	                parser.require(XmlPullParser.END_TAG, null, "method");
    	                }
    	                else if (name.equals("repeat")) {
    	                	parser.require(XmlPullParser.START_TAG, null, "repeat");
    	                	repeat = readText(parser).equalsIgnoreCase("true")? true : false;    	                	
    	                    parser.require(XmlPullParser.END_TAG, null, "repeat");
    	                }
    	                else if (name.equals("trigger")) {
    	                	parser.require(XmlPullParser.START_TAG, null, "trigger");
    	                	triggerAfter = Integer.valueOf(parser.getAttributeValue(null, "after"));  
    	                	parser.nextTag();
    	                    parser.require(XmlPullParser.END_TAG, null, "trigger");
    	                }
    	                else if (name.equals("parameter")) {
    	                	parser.require(XmlPullParser.START_TAG, null, "parameter");
    	                	String nameParam = null, valueParam = null, xpath = null, type = null;
    	                	while (parser.next() != XmlPullParser.END_TAG) {
    	    	                if (parser.getEventType() != XmlPullParser.START_TAG) {
    	    	                    continue;
    	    	                }
    	    	                name = parser.getName();
    	    	                if (name.equals("name")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "name");
    	    	                	nameParam = readText(parser);
    	 	    	                parser.require(XmlPullParser.END_TAG, null, "name");
    	    	                }
    	    	                else if(name.equals("reference")) {
    	    	                	xpath = parser.getAttributeValue(null, "xpath");
    	    	                	parser.nextTag();
    	    	                	type = parser.getAttributeValue(null, "type");
    	    	                }
    	    	                else if(name.equals("value")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "value");
    	    	                	valueParam = readText(parser);
    	 	    	                parser.require(XmlPullParser.END_TAG, null, "value");
    	    	                }
    	    	                else {
    		    	            	skip(parser);
    		    	            }
    	                	}
    	                	action.getPostParams().add(new ServerParameter(nameParam, valueParam, xpath, type));
    	                }
	    	            else {
	    	            	skip(parser);
	    	            }
                    }
                    action.setMethod(ServerMethod.getByString(method));
                    action.setResourceIdentifier(uri);
                    action.setRepeat(repeat);
                    action.setTrigger(triggerAfter);
                    spec.setAction(action);
                	
                } 
                else if (name.equals("outputSpecification")) {
                	parser.require(XmlPullParser.START_TAG, null, "outputSpecification");
                    DataSpecification outputSpecification = new DataSpecification();
                    while (parser.next() != XmlPullParser.END_TAG) {
    	                if (parser.getEventType() != XmlPullParser.START_TAG) {
    	                    continue;
    	                }
    	                name = parser.getName();
    	                if (name.equals("column")) {
    	                	parser.require(XmlPullParser.START_TAG, null, "column");

    	                	String colName = null, colType = null, colSource = null;
    	                	while (parser.next() != XmlPullParser.END_TAG) {
    	    	                if (parser.getEventType() != XmlPullParser.START_TAG) {
    	    	                    continue;
    	    	                }
    	    	                name = parser.getName();
    	    	                if (name.equals("name")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "name");
    	    	                    colName = readText(parser);
    	    	                    parser.require(XmlPullParser.END_TAG, null, "name");
    	    	                }
    	    	                else if (name.equals("type")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "type");
    	    	                    colType = readText(parser);
    	    	                    parser.require(XmlPullParser.END_TAG, null, "type");
    	    	                }
    	    	                else if (name.equals("source")) {
    	    	                	parser.require(XmlPullParser.START_TAG, null, "source");
    	    	                    colSource = readText(parser);
    	    	                    parser.require(XmlPullParser.END_TAG, null, "source");
    	    	                }
    	    	                else {
    	    	                    skip(parser);
    	    	                }    	    	    	                
    	                    }
    	                	outputSpecification.addColumn(colName, DataSpecification.DataType.getByString(colType), DataSpecification.InputSource.getByString(colSource));
    	                }
    	                else if(name.equals("view")){
    	                	parser.require(XmlPullParser.START_TAG, null, "view");
    	                    String view = readText(parser);
    	                    parser.require(XmlPullParser.END_TAG, null, "view");
    	                    outputSpecification.setView(view);
    	                }
    	                else {
    	                    skip(parser);
    	                }
    	                
    	            }
                    spec.setOutputSpecification(outputSpecification);
                    parser.require(XmlPullParser.END_TAG, null, "outputSpecification");
                } else {
                    skip(parser);
                }
            }
	
			is.close();
	 		
			return spec;
		
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
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
}
