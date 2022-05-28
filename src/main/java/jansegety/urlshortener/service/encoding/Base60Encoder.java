package jansegety.urlshortener.service.encoding;

import static jansegety.urlshortener.error.message.EncoderMessage.*;

/**
 * 사용자 입장에서 쉽게 헷갈릴 수 있는 대문자 아이I와 소문자 엘l을
 * Base62 인코더의 단축 문자 목록에서 삭제한 인코더입니다.
 */
public class Base60Encoder implements Encoder<Long, String>{

	final int RADIX = 60;
	final String CODEC = 
		"ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz0123456789";
	
	@Override
	public String encode(Long source) {
		StringBuffer sb = new StringBuffer();
		 do {
			sb.append(CODEC.charAt((int) (source % RADIX)));
			source /= RADIX;
		} while(source > 0);
		return sb.toString();
	}

	@Override
	public Long decode(String encoded) {
		long sum = 0;
		long power = 1;
		for (int i = 0; i < encoded.length(); i++) {
			sum += CODEC.indexOf(encoded.charAt(i)) * power;
			power *= RADIX;
		}
		return sum;
	}
	
	@Override
	public String encodeWithLengths(Long source, int length) {
		String valueEncoded = encode(source);
		
		if(valueEncoded.length() < length) {
			int emptySpaceLength = length - valueEncoded.length();
			
			StringBuilder stringBuilderValueEncoded = new StringBuilder(valueEncoded);
			
			//빈 공간만큼 0을 의미하는 A를 뒤에 붙여줍니다
			for(int i = 1; i <= emptySpaceLength; ++i) {
				stringBuilderValueEncoded.append("A");
			}
			
			valueEncoded = stringBuilderValueEncoded.toString();
			
		} else if (valueEncoded.length() > length) {
			throw new IllegalArgumentException(ENCODING_VALUE_TOO_LARGE.toString());
		}
		
		return valueEncoded;
	}

}
