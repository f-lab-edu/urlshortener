package jansegety.urlshortener.error.message;

public enum SimpleEntityMessage {
	
	ID_HAS_ALREADY_BEEN_ASSIGNED("id가 이미 할당되었습니다."),
	NO_ID_ASSIGNED("id가 할당되지 않았습니다.");
	
	private String message;
	
	private SimpleEntityMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
