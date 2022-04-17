package jansegety.urlshortener.controller;

import static jansegety.urlshortener.error.message.ClientApplicationMessage.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jansegety.urlshortener.entity.ClientApplication;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.error.exception.InvalidClientException;
import jansegety.urlshortener.error.exception.InvalidSecretException;
import jansegety.urlshortener.interceptor.AuthClientApplicationInterceptor;
import jansegety.urlshortener.repository.ClientApplicationRepository;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.service.encoding.Encoder;
import jansegety.urlshortener.util.UrlMaker;

@SpringBootTest
class UrlPackJsonControllerTest {
	
	@Autowired
	private UrlPackJsonController jsonController;
	
	@Autowired
	private UrlPackRepository urlPackRepository;
	
	@Autowired
	private ClientApplicationRepository clientApplicationRepository;
	
	@Autowired
	private AuthClientApplicationInterceptor authClientApplicationInterceptor;
	
	@Autowired
	private Encoder<Long, String> encoder;

	private MockMvc mvc;
	private MediaType jsonType = new MediaType(MediaType.APPLICATION_JSON.getType(),	
										MediaType.APPLICATION_JSON.getSubtype(),
										Charset.forName("utf8"));
	
	private final String LONG_URL = "http://www.long.www.long.www.long.wwwlong";
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(jsonController)
			.addInterceptors(authClientApplicationInterceptor)
			.build();
	
		urlPackRepository.deleteAll();
		clientApplicationRepository.deleteAll();
	}

	@Test
	@DisplayName("/urlpack/util/shorturl로 요청이 오면 client-id와 client-secret가 검증이 통과되야 "
			+ "원래url 단축url 정보 등이 포함된 createShortUrlDto객체 형태로 응답이 되어야 한다.")
	void when_requestShortUrlClientIdAndClientSecretMustPassVerification_then_responseInTheFormOfCreateShortUrlDto() 
			throws Exception {
		
		//clientApplication 설정
		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setName("테스트용 클라이언트");
		clientApplication.setClientSecret(UUID.randomUUID()); //id는 영속화 될 때 할당된다.
		
		//clientApplication에 유저 할당
		User user = new User();
		clientApplication.setUser(user);
		
		//clientApplication 영속화
		clientApplicationRepository.save(clientApplication); 
		
		mvc.perform(post("/urlpack/util/shorturl").param("url",LONG_URL).accept(jsonType)
				.header("urlshortener-client-id", clientApplication.getId().toString())
				.header("urlshortener-client-secret", clientApplication.getClientSecret().toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.orginalUrl", is(LONG_URL)))
			.andExpect(jsonPath("$.result.shortenedUrl", is(UrlMaker.makeUrlWithDomain(encoder.encoding(1L)))));
		
	}
	
	@Test
	@DisplayName("/urlpack/util/shorturl로 요청이 오면 client-id와 client-secret가 없으면 "
			+ "IllegalArgumentException이 발생해야 한다.")
	void when_ClientIdAndClientSecretDoNotPassVerification_then_throwIllegalArgumentException() 
			throws Exception
	{
		// and set null 'urlshortener-client-secret' header value
		assertThatThrownBy(()-> mvc.perform(post("/urlpack/util/shorturl")
				.param("url",LONG_URL).accept(jsonType)
				.header("urlshortener-client-id", "")))
			.hasCause(
				new IllegalArgumentException(
					CLIENT_ID_OR_SECRET_IS_REQUIRED.getMessage()));
	}
	
	
	@Test
	@DisplayName("/urlpack/util/shorturl로 요청이 오면 일치하는 클라이언트가 없다면 "
			+ "InvalidClientException이 발생해야 한다.")
	void when_thereIsNotMachingClient_then_throwIllegalClientException() 
			throws Exception
	{
		//clientApplication 설정
		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setName("테스트용 클라이언트");
		clientApplication.setId(UUID.randomUUID()); // 저장소로 부터 할당받지 않은 임의의 UUID를 할당
		clientApplication.setClientSecret(UUID.randomUUID());
		
		//clientApplication에 유저 할당
		User user = new User();
		clientApplication.setUser(user);
		
		//clientApplication을 영속화 하지 않음
	
		assertThatThrownBy(()-> mvc.perform(post("/urlpack/util/shorturl")
				.param("url",LONG_URL).accept(jsonType)
				.header("urlshortener-client-id", clientApplication.getId().toString())
				.header("urlshortener-client-secret", 
					clientApplication.getClientSecret().toString())))
			.hasCause(
				new InvalidClientException(
					NO_MATCHING_CLIENT_FOUND.getMessage()));

	}
	
	
	@Test
	@DisplayName("/urlpack/util/shorturl로 요청이 오면 client-secret이 일치하지 않으면 "
			+ "InvalidSecretException이 발생해야 한다.")
	void when_thereIsNotMachingSecret_then_throwInvalidSecretException() 
			throws Exception
	{
		//clientApplication 설정
		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setName("테스트용 클라이언트");
		clientApplication.setClientSecret(UUID.randomUUID()); //id는 영속화 될 때 할당된다.
		
		//clientApplication에 유저 할당
		User user = new User();
		clientApplication.setUser(user);
		
		//clientApplication 영속화
		clientApplicationRepository.save(clientApplication);
		
		assertThatThrownBy(()-> mvc.perform(post("/urlpack/util/shorturl")
				.param("url",LONG_URL).accept(jsonType)
				.header("urlshortener-client-id", clientApplication.getId().toString())
				.header("urlshortener-client-secret", UUID.randomUUID().toString())))//임의의 UUID를 할당
			.hasCause(
				new InvalidSecretException(
					NO_MATCHING_SECRET_FOUND.getMessage()));
	}
	
	@Test
	@DisplayName("/urlpack/util/shorturl로 요청이 오면 client를 소유한 user 정보가 없으면 예외가 발생해야 한다.")
	void when_thereIsNotMachingUserOwnsClient_then_throwsIllegalStateException() 
			throws Exception
	{
		//clientApplication 설정
		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setName("테스트용 클라이언트");
		clientApplication.setClientSecret(UUID.randomUUID()); //id는 영속화 될 때 할당된다.
		
		//clientApplication에 user 정보를 set 하지 않음
		
		//clientApplication 영속화
		clientApplicationRepository.save(clientApplication); 
		
		User user = new User();
		user.setEmail("good@morning");
		
		assertThatThrownBy(() -> mvc.perform(post("/urlpack/util/shorturl")
				.param("url",LONG_URL).accept(jsonType)
				.header("urlshortener-client-id", clientApplication.getId().toString())
				.header("urlshortener-client-secret", clientApplication.getClientSecret().toString())))
			.hasCause(new IllegalStateException(CLIENT_HAS_NO_USER.getMessage()));
	}

}
