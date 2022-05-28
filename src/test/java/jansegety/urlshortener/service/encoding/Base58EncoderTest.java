package jansegety.urlshortener.service.encoding;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

class Base58EncoderTest {

	Base58Encoder encoder = new Base58Encoder();
	
	@Test
	@DisplayName("인코딩 한 값을 다시 디코딩하면 소스와 같아야 한다.")
	void when_encodeSource_then_theReturnValueDecodedIsSameAsTheSource() {
		String encodedValue = encoder.encode(1L);
		assertThat(encoder.decode(encodedValue), equalTo(1L));
		
		String encodedValue2 = encoder.encode(10034524342L);
		assertThat(encoder.decode(encodedValue2), equalTo(10034524342L));
	}
	
	@Test
	@DisplayName("단축 URL의 id타입인 Long의 최대값을 Base58Encoder 압축했을 때 12자리 이하여야 한다.")
	void when_encodeLongMaxValue_then_returnValueLessThen12Length() {
		String valueCompressed = encoder.encode(Long.MAX_VALUE);
		assertTrue(valueCompressed.length() < 12);	
	}
	
	@Test
	@DisplayName("입력된 Long 값이 0일 때도 문제없이 인코딩 되어야 한다.")
	void when_encode0L_then_thersIsNoException() {
		String encode = encoder.encode(0L);
		assertFalse(StringUtils.isBlank(encode));
	}

}
