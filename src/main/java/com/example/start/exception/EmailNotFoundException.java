package com.example.start.exception;

public class EmailNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
