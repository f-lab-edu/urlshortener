package jansegety.urlshortener.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UrlMakerTest {
	
	@Value("${domain.name}")
	String domainName;

	@Test
	@DisplayName("프로퍼티 파일의 도메인 주소 이름을 참조하여 Url을 만들어줘야 한다.")
	void when_invokeMakeUrlWithDomain_then_CreateAnUrlByReferringToTheDomainNameOfTheProperty() {
		String UrlWithDomain = UrlMaker.makeUrlWithDomain("hellow");
		assertThat(UrlWithDomain, equalTo("https://"+domainName+"/hellow"));
	}

}
