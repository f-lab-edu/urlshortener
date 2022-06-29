package jansegety.urlshortener.error.exception.compression;

public class CompressionAlgorithmException extends RuntimeException{
	
	public CompressionAlgorithmException(String message, Exception causeException) {
		super(message, causeException);
	}
	
}
