package com.bodycloud.server.rest.application;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

public class ConvertHelper {

	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> clazz, Representation rep, Schema schema) throws ResourceException {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller u = context.createUnmarshaller();
			u.setSchema(schema);
			return (T) u.unmarshal(rep.getStream());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}

	public static byte[] toByteArray(Representation rep) throws ResourceException {
		try {
			InputStream in = rep.getStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int next = in.read();
			while (next > -1) {
				bos.write(next);
				next = in.read();
			}
			return bos.toByteArray();
		} catch (Exception e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}

}
