package jansegety.urlshortener.error.exception.entity.clientapplication;

public class AuthClientApplicationException extends RuntimeException{
	public AuthClientApplicationException(String message, Exception causeException) {
		super(message, causeException);
	}
	public AuthClientApplicationException(Exception causeException) {
		super(causeException);
	}
	public AuthClientApplicationException(String message) {
		super(message);
	}
}
