package com.g2minhle.bingdatacleaner.exception;

/**
 * Exception there is so such job
 */
public class JobNotFoundException extends Exception {
	public JobNotFoundException() {
		super();
	}

	public JobNotFoundException(String s) {
		super(s);
	}
}
