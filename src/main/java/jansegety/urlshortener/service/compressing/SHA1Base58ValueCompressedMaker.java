package jansegety.urlshortener.service.compressing;

import org.springframework.stereotype.Component;

import jansegety.urlshortener.service.encoding.Encoder;
import jansegety.urlshortener.service.hashing.Hasher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SHA1Base58ValueCompressedMaker implements ValueCompressedMaker<String, String>{
	
	private final Hasher<String, String> hasher;
	private final Encoder<Long, String> encoder;

	@Override
	public String compress(String source) {
		
		//16진법으로 40자리의 문자가 출력됩니다, 즉 20 바이트 크키
		String hashText = hasher.hash(source);
		//14자리로 자릅니다. 16^14 즉 72,057,594,037,927,936, 약 7경의 크기
		String firs14DigitsHashText = hashText.substring(0, 14); 
		//16진법의 문자열을 Long 타입으로 변환합니다.
		long hashValue = Long.parseLong(firs14DigitsHashText, 16);
		/*
		 * 7경 * 6 = 42,  42경의 경우의 수로 만듭니다.
		 * 6을 곱하는 이유는 아래와 같습니다.
		 * 58 진법의 수 10자리로 표현하게 되면 58^10 = 430,804,206,899,405,824 약 43경의 경우의 수가 나옵니다.
		 * 즉, base58 인코딩 방식을 사용해서 10자리의 일관된 문자열 길이로 표현할 수 있는 경우의 수를 최대한 사용하기 위해서입니다.
		 */
		hashValue = hashValue * 6; 
		
		//인코딩 합니다.
		return encoder.encodeWithLengths(hashValue, 10);
	}

}
