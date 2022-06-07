package jansegety.urlshortener.error.message;

public enum UserMessage {

	NO_EMAIL_ASSIGNED_TO_USER("User Entity에 email이 할당되지 않았습니다."),
	USER_NOT_REGISTERED("유저가 등록되어 있지 않습니다.");
	
	private String message;
	
	private UserMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
