package jansegety.urlshortener.error.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidClientException extends RuntimeException{
	public InvalidClientException(String message) {super(message);}
}
