package jansegety.urlshortener.controller;

import static jansegety.urlshortener.error.message.UrlPackMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.repository.memoryrepository.UrlPackMemoryRepository;
import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.UserService;
import jansegety.urlshortener.service.compressing.ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;
import jansegety.urlshortener.testutil.constant.MockUserField;
import jansegety.urlshortener.testutil.constant.URL;

@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.SAME_THREAD)
@EnableAutoConfiguration(exclude= {HibernateJpaAutoConfiguration.class})
//@ActiveProfiles("concurrent-test")
//@EnableAutoConfiguration(exclude= { 
//		DataSourceAutoConfiguration.class, 
//		DataSourceTransactionManagerAutoConfiguration.class, 
//		HibernateJpaAutoConfiguration.class,
//		MybatisAutoConfiguration.class})
@SpringBootTest
class RedirectControllerTest {
	
	@Autowired
	private RedirectController redirectController;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UrlPackController urlController;
	
	@Autowired
	private UrlPackRepository urlPackRepository;
	
	@Autowired
	private UrlPackService urlPackService;
	
	@Autowired
	private ValueCompressedMaker<String, String> valueCompressedMaker;
	
	private MockMvc mock;
	UrlPack urlPackRegisteredAndHavingValueCompressed;
	
	private final String ORIGINAL_URL = URL.MOCK_ORIGINAL_URL;
	
	@BeforeEach
	public void setup() {
		mock = MockMvcBuilders.standaloneSetup(urlController, redirectController).build();
		
		if(urlPackRepository instanceof UrlPackMemoryRepository) {
			UrlPackMemoryRepository urlPackMemoryRepository = (UrlPackMemoryRepository)urlPackRepository;
			urlPackMemoryRepository.deleteAll();
		}
		
		
		User mockLoginUser = new User(MockUserField.EMAIL, MockUserField.PASSWORD);
		userService.regist(mockLoginUser); //user를 영속화 해야 url_pack의 외래키 제약조건에 걸리지 않는다.
		
		CompressingSourceProvider<String> mockCompressiongSourceProvider =
				mock(CompressingSourceProvider.class);
			
		String mockUUID = UUID.randomUUID().toString().replace("-", "");
		when(mockCompressiongSourceProvider.getSource()).thenReturn(mockUUID);
		
		
		urlPackRegisteredAndHavingValueCompressed = 
			UrlPack.makeUrlPackRegisteredAndHavingValueCompressed(
					mockLoginUser, 
					ORIGINAL_URL, 
					urlPackService, 
					mockCompressiongSourceProvider, 
					valueCompressedMaker);
	}

	@Test
	@DisplayName("등록된 shortUrl로 요청시 redirect 응답")
	void when_requestShortUrlRegistered_then_responseRedirectTooriginalUrl() throws Exception {
		
		String shortUrl = urlPackRegisteredAndHavingValueCompressed.getValueCompressed();
		mock.perform(get("/"+shortUrl))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl(ORIGINAL_URL));

	}
	
	@Test
	@DisplayName("등록된 shortUrl로 요청시 요청횟수 1 증가")
	void when_requestShortUrlRegistered_then_requestNumPlusOne() throws Exception {
		
		String valueCompressed = urlPackRegisteredAndHavingValueCompressed.getValueCompressed();
		
		UrlPack urlPack;
		mock.perform(get("/"+valueCompressed)); //request 1
		urlPack = urlPackService.findByValueCompressed(valueCompressed);
		assertThat(urlPack.getRequestNum(), equalTo(1));
		
		mock.perform(get("/"+valueCompressed)); //request 2
		urlPack = urlPackService.findByValueCompressed(valueCompressed);
		assertThat(urlPack.getRequestNum(), equalTo(2));
		
	}
	
	@Test
	@DisplayName("등록되지 않은 shortUrl로 요청시 IllegalArgumentException 예외 발생")
	void when_requestShortUrlNotRegistered_then_throwIllegalArgumentException() throws Exception {
		String compressedUrl = "c2asdTIid";
		
		 assertThatThrownBy(()->mock.perform(get("/"+compressedUrl)))
		 	.hasCause(
	 			new IllegalArgumentException(
 					URL_PACK_ENTITY_CORRESPONDING_TO_VALUE_COMPRESSED_DOES_NOT_EXIST.getMessage()));
		
	}

}
