package jansegety.urlshortener.testutil.constant;

public interface RegularExpression {
	
	//l,I,0,O,-,_ 를 제외한 모든 ASCII 알파벳과 숫자 문자로 이루어져 있고, 0이상 10 이하의 문자열인지 확인하는 정규식
	//-를 escape 문자 처리할 때 \를 2번 사용야 한다. 문자열 파싱할 때도 -특수 문자로 인식되므로 \한 개가 소모되기 때문이다.
	public static final String VALUE_COMPRESSED_FORMAT = "[a-zA-Z0-9&&[^lI0O\\-_]]{0,10}";
	public static final String VALUE_COMPRESSED_FORMAT_WITH_PRIFIX = 
		"(https://urlshortener/)" + VALUE_COMPRESSED_FORMAT;
	
	// \w : 알파벳 단어 문자(word 문자): [a-zA-Z_0-9]
	public static final String LENGTH_10_FORMAT = "\\w{10}";
	// X+ : X가 한번 이상 나온다는 뜻
	public static final String BASE58_CHARACTERS_FORMAT = "[a-zA-Z0-9&&[^lI0O\\-_]]+";
}
