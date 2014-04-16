package com.bodycloud.server.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable
public class View extends Describable {
	
	Blob content;


	public void setContent(Blob content) {
		this.content = content;
	}
	
	public boolean setSpecification(Document dom) throws TransformerFactoryConfigurationError, TransformerException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.transform(new DOMSource(dom), new StreamResult(bos));
		content = new Blob(bos.toByteArray());
		return true;
	}
	
	public Document getSpecification() throws SAXException, IOException, ParserConfigurationException {
		ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(is);
	}

}
