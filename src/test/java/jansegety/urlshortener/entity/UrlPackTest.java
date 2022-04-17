package jansegety.urlshortener.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jansegety.urlshortener.service.encoding.Encoder;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigForUrlPackTest.class})
@ActiveProfiles({"test"})
class UrlPackTest {
	
	@Autowired
	private Encoder<Long, String> encoder;
	
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
		
		UrlPack urlPack = new UrlPack();
		
		urlPack.setId(12345L);
		urlPack.createValueEncoded(encoder);
		
		String encodedValue = encoder.encoding(12345L);
		
		assertThat(urlPack.getValueCompressed(), is(equalTo(encodedValue)));
		
	}
	
	@Test
	@DisplayName("일치하지 않는 originalUrl로 shortUrl을 요청하면 IllegalArgumentException 발생")
	void when_requestShortUrlWithMisMatchedoriginalUrl_then_throwIllegalArgumentException() {
		
		UrlPack urlPack = new UrlPack();
		urlPack.setOriginalUrl("AAA.long.url");
		
		assertThrows(IllegalArgumentException.class, 
				()->urlPack.requestShortUrlWithOriginalUrl("BBB.long.url"));
	}
	

}
