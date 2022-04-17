package jansegety.urlshortener.service.encoding;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Base60EncoderTest {

	Base60Encoder encoder = new Base60Encoder();
	
	@Test
	@DisplayName("인코딩 한 값을 다시 디코딩하면 소스와 같아야 한다.")
	void when_encodeSource_then_theReturnValueDecodedIsSameAsTheSource() {
		String encodedValue = encoder.encoding(1L);
		assertThat(encoder.decoding(encodedValue), equalTo(1L));
		
		String encodedValue2 = encoder.encoding(10034524342L);
		assertThat(encoder.decoding(encodedValue2), equalTo(10034524342L));
	}
	
	@Test
	@DisplayName("단축 URL의 id타입인 Long의 최대값을 Base60Encoder 압축했을 때 12자리 이하여야 한다.")
	void when_encodneLongMaxValue_then_returnValueLessThen12Length() {
		String valueCompressed = encoder.encoding(Long.MAX_VALUE);
		assertTrue(valueCompressed.length() < 12);	
	}
	
}
