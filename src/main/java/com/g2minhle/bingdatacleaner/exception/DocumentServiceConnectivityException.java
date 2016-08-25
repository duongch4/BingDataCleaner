package com.g2minhle.bingdatacleaner.exception;

/**
 * Exception there is connectivity issue with document service
 */
public class DocumentServiceConnectivityException extends Exception {
	public DocumentServiceConnectivityException() {
		super();
	}

	public DocumentServiceConnectivityException(String s) {
		super(s);
	}
}
