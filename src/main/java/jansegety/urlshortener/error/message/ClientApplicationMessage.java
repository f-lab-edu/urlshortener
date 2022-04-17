package jansegety.urlshortener.error.message;

public enum ClientApplicationMessage {

	CLIENT_ID_OR_SECRET_IS_REQUIRED("클라이언트 ID 또는 Secret이 필요합니다."),
	NO_MATCHING_CLIENT_FOUND("일치하는 Client가 없습니다."),
	NO_MATCHING_SECRET_FOUND("Secret이 일치하지 않습니다."),
	CLIENT_HAS_NO_USER("Client에 유저 정보가 없습니다");
	
	private String message;
	
	private ClientApplicationMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
