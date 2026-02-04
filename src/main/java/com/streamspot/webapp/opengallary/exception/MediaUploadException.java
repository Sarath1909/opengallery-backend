package com.streamspot.webapp.opengallary.exception;

public class MediaUploadException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public MediaUploadException(String message) {
        super(message);
    }
	public MediaUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
