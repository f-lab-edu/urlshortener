package jansegety.urlshortener.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("concurrent-test")
@EnableAutoConfiguration(exclude= { 
		DataSourceAutoConfiguration.class, 
		DataSourceTransactionManagerAutoConfiguration.class, 
		HibernateJpaAutoConfiguration.class,
		MybatisAutoConfiguration.class})
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
