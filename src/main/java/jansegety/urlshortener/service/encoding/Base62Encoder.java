package jansegety.urlshortener.service.encoding;

import static jansegety.urlshortener.error.message.EncoderMessage.*;

public class Base62Encoder implements Encoder<Long, String>{
	
	final int RADIX = 62;
	final String CODEC = 
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

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
