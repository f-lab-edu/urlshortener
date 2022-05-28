package jansegety.urlshortener.service.compressing.sourceprovider;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jansegety.urlshortener.error.exception.ValueCompressedException;

class UUIDCompressingSourceProviderTest {

	UUIDCompressingSourceProvider compressingSourceProvider = new UUIDCompressingSourceProvider();
	
	@Test
	@DisplayName("소스를 제공할 수 있는 제한된 수를 초과해서 요청하면 예외가 발생합니다.")
	void when_ifRequestMoreThanTheLimitedNumber_then_throwException() {
		int limitedNumberOfOffers = compressingSourceProvider.DEFAULT_LIMITED_NUMBER_OF_OFFERS;
		
		assertThrows(ValueCompressedException.class, ()->{
			for(int i = 1; i <= limitedNumberOfOffers + 1; ++i) {
				compressingSourceProvider.getSource();
			}
		});
	}

}
