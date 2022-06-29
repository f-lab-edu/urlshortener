package jansegety.urlshortener.error.message;

public enum UrlPackMessage {
	
	
	ORIGiNAL_URL_DOES_NOT_MATCH("original url이 일치하지 않습니다."),
	NUMBER_OF_SHORTENED_ALGORITHM_ITERATIONS_EXCEEDED("단축 알고리즘 반복 횟수를 초과했습니다."),
	NO_USER_ASSIGNED_TO_URLPACK("UrlPack에 User가 할당되지 않았습니다."),
	NO_VALUE_COMPRESSED_ASSIGNED_TO_URLPACK("UrlPack에 ValueCompressed가 할당되지 않았습니다."),
	URL_PACK_ENTITY_CORRESPONDING_TO_ID_DOES_NOT_EXIST("해당 id를 가진 UrlPack Entity가 존재하지 않습니다."),
	URL_PACK_ENTITY_CORRESPONDING_TO_VALUE_COMPRESSED_DOES_NOT_EXIST("해당 valueCompressed를 가진 UrlPack Entity가 존재하지 않습니다.");

	private String message;
	
	private UrlPackMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
