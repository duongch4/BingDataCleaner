package com.g2minhle.bingdatacleaner.exception;

/**
 * Exception there is connectivity issue with document service
 */
public class CannotAccessToDocumentException extends Exception {
	public CannotAccessToDocumentException() {
		super();
	}

	public CannotAccessToDocumentException(String s) {
		super(s);
	}
}
