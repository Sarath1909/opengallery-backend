package com.streamspot.webapp.opengallary.exception;

public class PasswordResetRequiredException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordResetRequiredException(String message) {
        super(message);
    }
}
