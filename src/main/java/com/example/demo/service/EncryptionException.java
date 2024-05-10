package com.example.demo.service;

public class EncryptionException extends Exception {
	 /**
	 * 
	 */
		private static final long serialVersionUID = 1L;

	public EncryptionException(String message) {
	        super(message);
	    }

	    public EncryptionException(String message, Throwable cause) {
	        super(message, cause);
	    }

}
