package com.bodycloud.ext.rehab.user;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLUtils {

	/**
	 * I take a xml element and the tag name, look for the tag and get the text
	 * content i.e for <employee><name>John</name></employee> xml snippet if the
	 * Element points to employee node and tagName is 'name' I will return John
	 */
	public static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	public static int getIntValue(Element ele, String tagName) {
		// in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele, tagName));
	}

	public static Document createXMLResult(String rootName,
			Map<String, String> map) {

		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement(rootName);
			doc.appendChild(root);
			for (String elementName : map.keySet()) {
				Element child = doc.createElement(elementName);
				Text text = doc.createTextNode(map.get(elementName));
				child.appendChild(text);
				root.appendChild(child);
			}

			return doc;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static Document createXMLResult(String rootName,
			Map<String, String> map, Document d) {

		try {

			Element r = d.createElement(rootName);
			d.appendChild(r);

			for (String elementName : map.keySet()) {
				Element eltName = d.createElement(elementName);
				eltName.appendChild(d.createTextNode(map.get(elementName)));
				r.appendChild(eltName);
			}

			d.normalizeDocument();

			return d;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static Document createXMLError(Document d, String message,
			String exception) {

		Element r = d.createElement("rehabtutorerror");
		d.appendChild(r);

		Element eltName = d.createElement("message");
		eltName.appendChild(d.createTextNode(message));
		r.appendChild(eltName);

		Element eltName2 = d.createElement("exception");
		eltName2.appendChild(d.createTextNode(exception));
		r.appendChild(eltName2);

		d.normalizeDocument();

		return d;

	}

	public static DomRepresentation createXMLError(String message,
			String exception) {

		DomRepresentation result = null;
		Document d = null;
		try {
			result = new DomRepresentation(MediaType.TEXT_XML);
			d = result.getDocument();
		} catch (IOException e) {
		}
		Element r = d.createElement("rehabtutorerror");
		d.appendChild(r);

		Element eltName = d.createElement("message");
		eltName.appendChild(d.createTextNode(message));
		r.appendChild(eltName);

		Element eltName2 = d.createElement("exception");
		eltName2.appendChild(d.createTextNode(exception));
		r.appendChild(eltName2);

		d.normalizeDocument();

		return result;

	}

}
