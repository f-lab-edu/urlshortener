package jansegety.urlshortener.error.message;

public enum EncoderMessage {

	ENCODING_VALUE_TOO_LARGE("인코딩 값이 너무 큽니다.");
	
	private String message;
	
	private EncoderMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
