package com.bodycloud.server.rest.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class StreamClassLoader extends ClassLoader {

	private Map<String, byte[]> classes = new HashMap<String, byte[]>();
	private Map<String, Class<?>> definedClasses = new HashMap<String, Class<?>>();
	
	public StreamClassLoader(InputStream stream) throws IOException {
		super(StreamClassLoader.class.getClassLoader());
		
		JarInputStream is = new JarInputStream(stream);
		JarEntry entry = is.getNextJarEntry();
		while (entry != null) {
			if (entry.getName().contains(".class")) {
				String className = entry.getName().replace(".class", "").replace('/', '.');
				byte [] classByte = bufferrizeStream(is);
				classes.put(className, classByte);
			}
			entry = is.getNextJarEntry();
		}
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		try {
			return super.loadClass(className);
		} catch (ClassNotFoundException e) {
			return findClass(className);
		}
	}
	

	public Class<?> findClass(String className) throws ClassNotFoundException {
		
		Class<?> result;
		result = definedClasses.get(className);
		if (result == null) {
			byte[] classByte = (byte[]) classes.remove(className);
			if (classByte == null)
				throw new ClassNotFoundException();
			result = defineClass(className, classByte, 0, classByte.length, null);
			definedClasses.put(className, result);
		}
		return result;
	}
	
	private byte[] bufferrizeStream(InputStream is) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int nextValue = is.read();
		while (-1 != nextValue) {
			byteStream.write(nextValue);
			nextValue = is.read();
		}
		return byteStream.toByteArray();
	}
	
}
