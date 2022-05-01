package jansegety.urlshortener.error.message;

public enum UrlPackMessage {
	
	ORIGiNAL_URL_DOES_NOT_MATCH("original url이 일치하지 않습니다."),
	NUMBER_OF_SHORTENED_ALGORITHM_ITERATIONS_EXCEEDED("단축 알고리즘 반복 횟수를 초과했습니다.");

	private String message;
	
	private UrlPackMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
