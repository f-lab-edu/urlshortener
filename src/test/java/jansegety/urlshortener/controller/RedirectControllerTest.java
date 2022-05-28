package jansegety.urlshortener.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.compressing.ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;
import jansegety.urlshortener.service.encoding.Encoder;
import jansegety.urlshortener.service.hashing.Hasher;
import jansegety.urlshortener.testutil.constant.URL;


@SpringBootTest
class RedirectControllerTest {
	
	@Autowired
	private RedirectController redirectController;
	
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
		urlPackRepository.deleteAll();
		
		User mockLoginUser = mock(User.class);
		
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
		UrlPack urlPack = urlPackService.findByValueCompressed(valueCompressed).get();
		
		mock.perform(get("/"+valueCompressed)); //request 1
		assertThat(urlPack.getRequestNum(), equalTo(1));
		
		mock.perform(get("/"+valueCompressed)); //request 2
		assertThat(urlPack.getRequestNum(), equalTo(2));
		
	}
	
	@Test
	@DisplayName("등록되지 않은 shortUrl로 요청시 400 bad request 응답")
	void when_requestShortUrlNotRegistered_then_responseBadRequest400() throws Exception {
		String shortUrl = "NOT.REGISTED.URL";
		mock.perform(get("/"+shortUrl)).andExpect(status().is4xxClientError());
		
	}

}
