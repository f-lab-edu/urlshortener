package jansegety.urlshortener.service.encoding;

import org.springframework.stereotype.Component;

/**
 * 사용자 입장에서 쉽게 헷갈릴 수 있는 대문자 아이I와 소문자 엘l을
 * Base62 인코더의 단축 문자 목록에서 삭제한 인코더입니다.
 */
@Component
public class Base60Encoder implements Encoder<Long, String>{

	final int RADIX = 60;
	final String CODEC = 
		"ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz0123456789";
	
	@Override
	public String encoding(Long source) {
		StringBuffer sb = new StringBuffer();
		while(source > 0) {
			sb.append(CODEC.charAt((int) (source % RADIX)));
			source /= RADIX;
		}
		return sb.toString();
	}

	@Override
	public Long decoding(String encoded) {
		long sum = 0;
		long power = 1;
		for (int i = 0; i < encoded.length(); i++) {
			sum += CODEC.indexOf(encoded.charAt(i)) * power;
			power *= RADIX;
		}
		return sum;
	}

}
