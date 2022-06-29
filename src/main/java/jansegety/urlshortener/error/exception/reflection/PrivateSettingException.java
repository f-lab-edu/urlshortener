package jansegety.urlshortener.error.exception.reflection;

public class PrivateSettingException extends RuntimeException{
	public PrivateSettingException(Exception cause) {
		super(cause);
	}
}