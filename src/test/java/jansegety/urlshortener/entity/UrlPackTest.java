package jansegety.urlshortener.entity;

import static jansegety.urlshortener.entity.UrlPack.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jansegety.urlshortener.error.exception.ValueCompressedException;
import jansegety.urlshortener.repository.UrlPackMemorryRepository;
import jansegety.urlshortener.service.SimpleUrlPackService;
import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.compressing.SHA1Base58ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;
import jansegety.urlshortener.service.compressing.sourceprovider.UUIDCompressingSourceProvider;
import jansegety.urlshortener.service.encoding.Base58Encoder;
import jansegety.urlshortener.service.hashing.SHA1Hasher;
import jansegety.urlshortener.testutil.constant.URL;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {ConfigForUrlPackTest.class})
@ActiveProfiles({"test"})
class UrlPackTest {
	
	UrlPackService urlPackService = 
		new SimpleUrlPackService(new UrlPackMemorryRepository());
	
	
	private ValueCompressedMaker<String, String> valueCompressedMaker = 
		new SHA1Base58ValueCompressedMaker(new SHA1Hasher(), new Base58Encoder());
	
	private final String ORIGINAL_URL = URL.MOCK_ORIGINAL_URL;
	
	@Test
	@DisplayName("id가 할당된 상태에서 다시 할당하면 IllegalStateException 발생")
	void when_idReAssigned_then_throwIllegalStateException() {
		UrlPack urlPack = new UrlPack();
		urlPack.setId(1L);
		assertThrows(IllegalStateException.class, ()->{
				urlPack.setId(2L);
			});
	}
	
	@Test
	@DisplayName("id가 할당되면 자동으로 shortUrl이 생성되어야 한다.")
	void when_idAssigned_then_shortUrlMustbeCreated() {
		
		CompressingSourceProvider<String> mockCompressiongSourceProvider =
			mock(CompressingSourceProvider.class);
		
		String mockUUID = UUID.randomUUID().toString().replace("-", "");
		when(mockCompressiongSourceProvider.getSource()).thenReturn(mockUUID);
		
		UrlPack urlPackRegisteredAndHavingValueCompressed = 
			makeUrlPackRegisteredAndHavingValueCompressed(
				mock(User.class), 
				ORIGINAL_URL, 
				urlPackService, 
				mockCompressiongSourceProvider, 
				valueCompressedMaker);
		
		assertThat(
			urlPackRegisteredAndHavingValueCompressed
				.getValueCompressed(), is(notNullValue()));
		
	}
	
	@Test
	@DisplayName("일치하지 않는 originalUrl로 shortUrl을 요청하면 IllegalArgumentException 발생")
	void when_requestShortUrlWithMisMatchedoriginalUrl_then_throwIllegalArgumentException() {
		
		UrlPack urlPack = new UrlPack();
		urlPack.setOriginalUrl(ORIGINAL_URL);
		
		assertThrows(IllegalArgumentException.class, 
				()->urlPack.requestShortUrlWithOriginalUrl("BBB.long.url"));
	}
	
	@Test
	@DisplayName("압축된 값을 가지는 등록된 urlPack을 만들때, urlPack이 url을 압축할 때 사용한 알고리즘이 제대로 작동해야 한다.")
	void when_makeUrlPackRegisteredAndHavingValueCompressed_then_theAlgorithmUsedByUrlPackForUrlCompressionMustWorkProperly() {
		
		User mockLoginUser = mock(User.class);
		
		CompressingSourceProvider<String> mockCompressiongSourceProvider =
				mock(CompressingSourceProvider.class);
			
		String mockUUID = UUID.randomUUID().toString().replace("-", "");
		when(mockCompressiongSourceProvider.getSource()).thenReturn(mockUUID);
		
		UrlPack urlPackRegisteredAndHavingValueCompressed = 
			makeUrlPackRegisteredAndHavingValueCompressed(
				mockLoginUser, 
				ORIGINAL_URL, 
				urlPackService,
				mockCompressiongSourceProvider,
				valueCompressedMaker);
		
		String valueCompressed = 
			urlPackRegisteredAndHavingValueCompressed
				.getValueCompressed();
		
		//10자 이하여야 하고
		assertThat(valueCompressed.length(), org.hamcrest.Matchers.lessThanOrEqualTo(10));
		
		//Entity 내부의 알고리즘 제대로 동작해서 압축값을 생성해야 한다.
		assertThat(
				urlPackRegisteredAndHavingValueCompressed
					.getValueCompressed(), is(equalTo(valueCompressedMaker.compress(mockUUID))));
		
	}


}
