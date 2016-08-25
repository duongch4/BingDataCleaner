package com.g2minhle.bingdatacleaner.exception;

/**
 * Exception there is database connectivity issue
 */
public class DatabaseConnectivityException extends Exception {
	public DatabaseConnectivityException() {
		super();
	}

	public DatabaseConnectivityException(String s) {
		super(s);
	}
}
