package jansegety.urlshortener.service.compressing;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.text.MatchesPattern.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;
import jansegety.urlshortener.testutil.constant.RegularExpression;

@ActiveProfiles("concurrent-test")
@EnableAutoConfiguration(exclude= { 
		DataSourceAutoConfiguration.class, 
		DataSourceTransactionManagerAutoConfiguration.class, 
		HibernateJpaAutoConfiguration.class,
		MybatisAutoConfiguration.class})
@SpringBootTest
class ValueCompressedMakerTest {
	
	@Autowired
	CompressingSourceProvider<String> compressingSourceProvider;
	
	@Autowired
	ValueCompressedMaker<String, String> valueCompressedMaker;
	
	
	@BeforeEach
	void init() {
		//제한수 초기화
		compressingSourceProvider.init();
	}
 
	@Test
	@DisplayName("압축된 값은 언제나 일관된 10자리여야 합니다.")
	void when_returnValueCompressed_then_theValueShouldBe10Digits() {
		
		String source = compressingSourceProvider.getSource();
		String valueCompressed = valueCompressedMaker.compress(source);
		
		assertThat(valueCompressed, matchesPattern((RegularExpression.LENGTH_10_FORMAT)));
	}
	
	@Test
	@DisplayName("압축된 값은 언제나 ASCII코드에서 base58에 대응되는 숫자와 알파벳으로만 이루어져 있어야 합니다.")
	void  when_returnValueCompressed_then_theValueShouldBeBase58Characters() {
		
		String source = compressingSourceProvider.getSource();
		String valueCompressed = valueCompressedMaker.compress(source);
		
		assertThat(valueCompressed, matchesPattern((RegularExpression.BASE58_CHARACTERS_FORMAT)));
	}
	
	@Test
	@DisplayName("압축된 값은 언제나 10자리의 ASCII코드에서 base58에 대응되는 숫자와 알파벳으로만 이루어져 있어야 합니다.")
	void  when_returnValueCompressed_then_theValueShouldBe10DigitsBase58Characters() {
		
		String source = compressingSourceProvider.getSource();
		String valueCompressed = valueCompressedMaker.compress(source);
		
		assertThat(valueCompressed, matchesPattern((RegularExpression.VALUE_COMPRESSED_FORMAT)));
	}

}
