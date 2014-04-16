package com.bodycloud.engine;

import java.io.IOException;
import java.io.InputStream;



public interface KDEngine {

	public Worker getWorker(InputStream input) throws IOException;
	
}
