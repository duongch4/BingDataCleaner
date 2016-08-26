package com.g2minhle.bingdatacleaner.exception;

/**
 * Exception there is database connectivity issue
 */
public class InvalidActionNameException extends Exception {
	public InvalidActionNameException() {
		super();
	}

	public InvalidActionNameException(String s) {
		super(s);
	}
}
