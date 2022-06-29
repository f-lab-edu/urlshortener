package jansegety.urlshortener.controller;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jansegety.urlshortener.controller.viewdto.UrlPackInfo;
import jansegety.urlshortener.controller.viewdto.UrlPackListDto;
import jansegety.urlshortener.controller.viewdto.UrlPackRegistConfirmationDto;
import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.repository.UserRepository;
import jansegety.urlshortener.repository.memoryrepository.UrlPackMemoryRepository;
import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.compressing.ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;
import jansegety.urlshortener.testutil.constant.MockUserField;
import jansegety.urlshortener.testutil.constant.RegularExpression;
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
class UrlPackControllerTest {

	@Autowired
	private UrlPackController urlPackController;
	
	@Autowired
	private UrlPackRepository urlPackRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UrlPackService urlPackService;
	
	@Autowired
	private ValueCompressedMaker<String, String> valueCompressedMaker;
	
	private MockMvc mock;
	private User testUser;
	private final String ORIGINAL_URL_1 =  URL.MOCK_ORIGINAL_URL;
	
	private final String regex = RegularExpression.VALUE_COMPRESSED_FORMAT_WITH_PRIFIX;
	
	
	@BeforeEach
	public void setup() {
		
		mock = MockMvcBuilders
			.standaloneSetup(urlPackController)
			.setViewResolvers(getViewResolver())
			.build();
		
		if(urlPackRepository instanceof UrlPackMemoryRepository) {
			UrlPackMemoryRepository urlPackMemoryRepository = (UrlPackMemoryRepository)urlPackRepository;
			urlPackMemoryRepository.deleteAll();
		}
		
		//user 생성
		testUser = new User(MockUserField.EMAIL, MockUserField.PASSWORD);
		
		CompressingSourceProvider<String> mockCompressiongSourceProvider =
				mock(CompressingSourceProvider.class);
			
		String mockUUID = UUID.randomUUID().toString().replace("-", "");
		when(mockCompressiongSourceProvider.getSource()).thenReturn(mockUUID);
		
		userRepository.save(testUser);//영속화와 함게 User id를 받아온다.
		
		UrlPack
		.makeUrlPackRegisteredAndHavingValueCompressed(
				testUser, 
				ORIGINAL_URL_1, 
				urlPackService,
				mockCompressiongSourceProvider,
				valueCompressedMaker);

	}
	
	 public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = 
    		new InternalResourceViewResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        return resolver;
	 }
	 
	@Test
	@DisplayName("createForm 함수는 특별한 예외가 없다면 문자열 urlpack/registform 반환")
	void when_requestFormWithNoException_then_createFormFuncReturnStringUrlPackRegistForm() 
			throws Exception {
		
		mock.perform(get("/urlpack/registform")
				.requestAttr("loginUser", testUser))
			.andExpect(status().isOk())
			.andExpect(view().name("/urlpack/registform")); 
		
	}
	
	@Test
	@DisplayName("create 함수는 특별한 예외가 없다면 문자열 urlpack/registconfirmation 반환")
	void when_requestCreateNewEntityWithNoException_then_createFuncReturnStringUrlPackRegistConfrimation() 
			throws Exception {
		
		mock.perform(post("/urlpack/regist")
				.param("originalUrl", ORIGINAL_URL_1)
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser))
			.andExpect(status().isOk())
			.andExpect(view().name("/urlpack/registconfirmation"));
	
	}
	
	@Test
	@DisplayName("create 함수는 입력받은 originalUrl과 단축된 shortUrl을 가지는 RegistFormDto를 모델에 포함")
	void when_requestCreateNewEntityWithNoException_then_modelHasRegistFormDtoObjectWithoriginalUrlAndShortUrl() 
			throws Exception {
	
		MvcResult mvcResult = mock.perform(post("/urlpack/regist")
				.param("originalUrl", ORIGINAL_URL_1)
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser))
				.andDo(print())
				.andReturn();
		
		UrlPackRegistConfirmationDto urlPackRegistConfirmationDto = 
			(UrlPackRegistConfirmationDto)mvcResult
				.getModelAndView()
				.getModel()
				.get("urlPackRegistConfirmationDto");
		
		String originalUrl = urlPackRegistConfirmationDto.getOriginalUrl();
		String shortUrl = urlPackRegistConfirmationDto.getShortUrl();
		
		assertThat(originalUrl, equalTo(ORIGINAL_URL_1));
		assertThat(shortUrl, matchesPattern(regex));
	}
	
	@Test
	@DisplayName("show 함수는 특별한 예외가 없다면 문자열 urlpack/list 반환")
	void when_requestShowListWithNoException_then_showFuncReturnStringUrlPackList() 
			throws Exception {
		
		mock.perform(get("/urlpack/list").requestAttr("loginUser", testUser))
			.andExpect(status().isOk())
			.andExpect(view().name("/urlpack/list"));
		
	}
	
	@Test
	@DisplayName("show 함수는 urlPack 저장소에 있던 list를 model에 dto로 넣어 반환")
	void when_requestShowListWithNoException_then_putTheListInTheUrlPackStorageAsDtoInTheModelAndReturnIt() 
			throws Exception {
		
		final String ORIGIANL_URL_2 = "AAA.AAA.AAA";
		final String ORIGIANL_URL_3 = "BBB.BBB.BBB";
		
		mock.perform(post("/urlpack/regist")
				.param("originalUrl", ORIGIANL_URL_2)
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser));
		
		mock.perform(post("/urlpack/regist")
				.param("originalUrl", ORIGIANL_URL_3)
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser));
		
		MvcResult mvcResult = 
			mock.perform(get("/urlpack/list")
				.requestAttr("loginUser", testUser))
				.andReturn();
		
		UrlPackListDto urlPackListDto = 
			(UrlPackListDto)mvcResult
				.getModelAndView()
				.getModel()
				.get("urlPackListDto");
		
		List<UrlPackInfo> urlPackInfoList = urlPackListDto.getUrlPackInfoList();
		
		for(UrlPackInfo e : urlPackInfoList) {
			System.out.println("UrlPackInfo : " + e.getShortenedUrl() );
		}
		
		UrlPackInfo urlPackInfo0 = urlPackInfoList.get(0);
		assertThat(urlPackInfo0.getOriginalUrl(), equalTo(ORIGINAL_URL_1));
		assertThat(urlPackInfo0.getRequstNum(),  equalTo(0));
		assertThat(urlPackInfo0.getShortenedUrl(), matchesPattern(regex));
		
		UrlPackInfo urlPackInfo1 = urlPackInfoList.get(1);
		assertThat(urlPackInfo1.getOriginalUrl(), equalTo(ORIGIANL_URL_2));
		assertThat(urlPackInfo1.getRequstNum(),  equalTo(0));
		assertThat(urlPackInfo1.getShortenedUrl(), matchesPattern(regex));
		
		UrlPackInfo urlPackInfo2 = urlPackInfoList.get(2);
		assertThat(urlPackInfo2.getOriginalUrl(), equalTo(ORIGIANL_URL_3));
		assertThat(urlPackInfo2.getRequstNum(),  equalTo(0));
		assertThat(urlPackInfo2.getShortenedUrl(), matchesPattern(regex));
	}

}
