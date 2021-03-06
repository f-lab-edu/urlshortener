package jansegety.urlshortener.controller;

import static jansegety.urlshortener.error.message.ClientApplicationMessage.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.MatchesPattern.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jansegety.urlshortener.entity.ClientApplication;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.error.exception.InvalidClientException;
import jansegety.urlshortener.error.exception.InvalidSecretException;
import jansegety.urlshortener.interceptor.AuthClientApplicationInterceptor;
import jansegety.urlshortener.repository.ClientApplicationRepository;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.repository.UserRepository;
import jansegety.urlshortener.repository.memoryrepository.ClientApplicationMemoryRepository;
import jansegety.urlshortener.repository.memoryrepository.UrlPackMemoryRepository;
import jansegety.urlshortener.service.ClientApplicationService;
import jansegety.urlshortener.service.UserService;
import jansegety.urlshortener.service.encoding.Encoder;
import jansegety.urlshortener.testutil.constant.MockUserField;
import jansegety.urlshortener.testutil.constant.RegularExpression;
import jansegety.urlshortener.testutil.constant.URL;
import jansegety.urlshortener.util.UrlMaker;

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
class UrlPackJsonControllerTest {
	
	@Autowired
	private UrlPackJsonController jsonController;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UrlPackRepository urlPackRepository;
	
	@Autowired
	private ClientApplicationRepository clientApplicationRepository;
	
	@Autowired
	private ClientApplicationService clientApplicationService;
	
	@Autowired
	private AuthClientApplicationInterceptor authClientApplicationInterceptor;
	
	@Autowired
	private Encoder<Long, String> encoder;

	private MockMvc mvc;
	private MediaType jsonType = new MediaType(MediaType.APPLICATION_JSON.getType(),	
										MediaType.APPLICATION_JSON.getSubtype(),
										Charset.forName("utf8"));
	
	private final String ORIGINAL_URL = URL.MOCK_ORIGINAL_URL;
	
	private User mockUser;
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(jsonController)
			.addInterceptors(authClientApplicationInterceptor)
			.build();
	
		if(urlPackRepository instanceof UrlPackMemoryRepository) {
			UrlPackMemoryRepository urlPackMemoryRepository = (UrlPackMemoryRepository)urlPackRepository;
			urlPackMemoryRepository.deleteAll();
		}
		
		if(clientApplicationRepository instanceof ClientApplicationMemoryRepository) {
			ClientApplicationMemoryRepository clientApplicationMemoryRepository = 
				(ClientApplicationMemoryRepository)clientApplicationRepository;
			
			clientApplicationMemoryRepository.deleteAll();
		}
		
		//clientApplication??? ?????? ??????
		mockUser = new User(MockUserField.EMAIL, MockUserField.PASSWORD);
		userService.regist(mockUser); //????????? ??? id ??????
		
	}

	@Test
	@DisplayName("/urlpack/util/shorturl??? ????????? ?????? client-id??? client-secret??? ????????? ???????????? "
			+ "??????url ??????url ?????? ?????? ????????? createShortUrlDto?????? ????????? ????????? ????????? ??????.")
	void when_requestShortUrlClientIdAndClientSecretMustPassVerification_then_responseInTheFormOfCreateShortUrlDto() 
			throws Exception {
		
		//clientApplication ??????
		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setName("???????????? ???????????????");
		clientApplication.setClientSecret(UUID.randomUUID().toString()); //id??? ????????? ??? ??? ????????????.
		
		//clientApplication??? ?????? ??????
		clientApplication.setUserId(mockUser.getId());
		
		//clientApplication ?????????
		clientApplicationService.regist(clientApplication); 
		
		final String regex = RegularExpression.VALUE_COMPRESSED_FORMAT_WITH_PRIFIX;
		
		System.out.println("id = " + clientApplication.getId());
		System.out.println("secret = " + clientApplication.getClientSecret());
		
		mvc.perform(post("/urlpack/util/shorturl").param("url",ORIGINAL_URL).accept(jsonType)
				.header("urlshortener-client-id", clientApplication.getId().toString())
				.header("urlshortener-client-secret", clientApplication.getClientSecret().toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.orginalUrl", is(ORIGINAL_URL)))
			.andExpect(jsonPath("$.result.shortenedUrl").exists())
			.andExpect(jsonPath("$.result.shortenedUrl", matchesPattern(regex)))
			.andDo(print());
			
	}
	
	@Test
	@DisplayName("/urlpack/util/shorturl??? ????????? ?????? client-id??? client-secret??? ????????? "
			+ "IllegalArgumentException??? ???????????? ??????.")
	void when_ClientIdAndClientSecretDoNotPassVerification_then_throwIllegalArgumentException() 
			throws Exception
	{
		// and set null 'urlshortener-client-secret' header value
		assertThatThrownBy(()-> mvc.perform(post("/urlpack/util/shorturl")
				.param("url",ORIGINAL_URL).accept(jsonType)
				.header("urlshortener-client-id", "")))
			.hasCause(
				new IllegalArgumentException(
					CLIENT_ID_OR_SECRET_IS_REQUIRED.getMessage()));
	}
	
	
	@Test
	@DisplayName("/urlpack/util/shorturl??? ????????? ?????? ???????????? ?????????????????? ????????? "
			+ "InvalidClientException??? ???????????? ??????.")
	void when_thereIsNotMachingClient_then_throwIllegalClientException() 
			throws Exception
	{
		//clientApplication ??????
		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setName("???????????? ???????????????");
		clientApplication.setId(UUID.randomUUID()); // ???????????? ?????? ???????????? ?????? ????????? UUID??? ??????
		clientApplication.setClientSecret(UUID.randomUUID().toString());
		
		//clientApplication??? ?????? ??????
		User user = new User(MockUserField.EMAIL, MockUserField.PASSWORD);
		clientApplication.setUserId(user.getId());
		
		//clientApplication??? ????????? ?????? ??????
	
		assertThatThrownBy(()-> mvc.perform(post("/urlpack/util/shorturl")
				.param("url",ORIGINAL_URL).accept(jsonType)
				.header("urlshortener-client-id", clientApplication.getId().toString())
				.header("urlshortener-client-secret", 
					clientApplication.getClientSecret().toString())))
			.hasCause(
				new InvalidClientException(
					NO_MATCHING_CLIENT_FOUND.getMessage()));
	}
	
	
	@Test
	@DisplayName("/urlpack/util/shorturl??? ????????? ?????? client-secret??? ???????????? ????????? "
			+ "InvalidSecretException??? ???????????? ??????.")
	void when_thereIsNotMachingSecret_then_throwInvalidSecretException() 
			throws Exception
	{
		//clientApplication ??????
		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setName("???????????? ???????????????");
		clientApplication.setClientSecret(UUID.randomUUID().toString()); //id??? ????????? ??? ??? ????????????.
		
		clientApplication.setUserId(mockUser.getId());
		
		clientApplicationService.regist(clientApplication);//?????????
		
		assertThatThrownBy(()-> mvc.perform(post("/urlpack/util/shorturl")
				.param("url",ORIGINAL_URL).accept(jsonType)
				.header("urlshortener-client-id", clientApplication.getId())
				.header("urlshortener-client-secret", UUID.randomUUID().toString())))//????????? UUID??? ??????
			.hasCause(
				new InvalidSecretException(
					NO_MATCHING_SECRET_FOUND.getMessage()));
	}

}
