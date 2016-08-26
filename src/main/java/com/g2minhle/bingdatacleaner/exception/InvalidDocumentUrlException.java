package com.g2minhle.bingdatacleaner.exception;

/**
 * Exception there is connectivity issue with document service
 */
public class InvalidDocumentUrlException extends Exception {
	public InvalidDocumentUrlException() {
		super();
	}

	public InvalidDocumentUrlException(String s) {
		super(s);
	}
}
