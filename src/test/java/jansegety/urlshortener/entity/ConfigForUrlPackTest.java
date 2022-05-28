package jansegety.urlshortener.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jansegety.urlshortener.service.encoding.Base62Encoder;
import jansegety.urlshortener.service.encoding.Encoder;

@Configuration
@Profile({"test"})
public class ConfigForUrlPackTest {
	
	@Bean
	public Encoder<Long, String> encoder() {
		return new Base62Encoder();
	}
	
}
