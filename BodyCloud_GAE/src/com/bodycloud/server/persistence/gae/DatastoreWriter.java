package com.bodycloud.server.persistence.gae;

import java.util.ConcurrentModificationException;

public class DatastoreWriter {
	
	public static final long CONTENTION_SLEEP_TIME = 100;
	
	static void write(Runnable operation) {
		boolean done = false;
		while (!done) {
			try {
				operation.run();
				done = true;
			} catch (ConcurrentModificationException e) {
				try {
					Thread.sleep(CONTENTION_SLEEP_TIME);
				} catch (InterruptedException e1) { }
			} catch (Exception e) {
				done = true;
			}
		}
	}

}
