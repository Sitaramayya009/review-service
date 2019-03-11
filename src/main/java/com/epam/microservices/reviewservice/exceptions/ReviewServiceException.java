package com.epam.microservices.reviewservice.exceptions;

public class ReviewServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ReviewServiceException(String message) {
		super(message);
	}
}
