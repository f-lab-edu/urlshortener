package jansegety.urlshortener.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jansegety.urlshortener.controller.viewdto.UrlPackInfo;
import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.encoding.Encoder;
import jansegety.urlshortener.util.UrlMaker;

@SpringBootTest
class UrlPackControllerTest {

	@Autowired
	private UrlPackController urlPackController;
	
	@Autowired
	private UrlPackRepository urlPackRepository;
	
	@Autowired
	private UrlPackService urlPackService;
	
	@Autowired
	private Encoder<Long, String> encoder;
	
	private MockMvc mock;
	private User testUser;
	private final String LONG_URL =  "WWW.ABCDEFG.HIJKLMNOP";
	
	
	@BeforeEach
	public void setup() {
		
		mock = MockMvcBuilders
			.standaloneSetup(urlPackController)
			.setViewResolvers(getViewResolver())
			.build();
		
		urlPackRepository.deleteAll();
		
		//user 생성
		testUser = new User();
		
		UrlPack
		.makeUrlPackRegisteredAndHavingValueCompressed(
				testUser, 
				LONG_URL, 
				urlPackService, 
				encoder);

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
		
		mock.perform(post("/urlpack/registform")
				.param("originalUrl", LONG_URL)
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser))
			.andExpect(status().isOk())
			.andExpect(view().name("/urlpack/registconfirmation"));
	
	}
	
	@Test
	@DisplayName("create 함수는 입력받은 originalUrl과 단축된 shortUrl을 가지는 RegistFormDto를 모델에 포함")
	void when_requestCreateNewEntityWithNoException_then_modelHasRegistFormDtoObjectWithoriginalUrlAndShortUrl() 
			throws Exception {
		
		urlPackRepository.deleteAll();
		    
		mock.perform(post("/urlpack/registform")
				.param("originalUrl", LONG_URL)
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser))
			.andExpect(model()
				.attribute("urlPackRegistConfirmationDto", 
					hasProperty("originalUrl", equalTo(LONG_URL))))
			.andExpect(model()
				.attribute("urlPackRegistConfirmationDto", 
					hasProperty("shortUrl", 
						equalTo(UrlMaker.makeUrlWithDomain(encoder.encoding(1L)))))); 
						//B is Encoded Value by base62 with 1L
	
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
		
		urlPackRepository.deleteAll();
		
		mock.perform(post("/urlpack/registform")
				.param("originalUrl", "AAA.AAA.AAA")
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser));
		
		mock.perform(post("/urlpack/registform")
				.param("originalUrl", "BBB.BBB.BBB")
				.sessionAttr("userId", 1L)
				.requestAttr("loginUser", testUser));
		
		UrlPackInfo urlInfo1 = new UrlPackInfo();
		urlInfo1.setOriginalUrl("AAA.AAA.AAA");
		urlInfo1.setShortenedUrl(UrlMaker.makeUrlWithDomain(encoder.encoding(1L)));
		urlInfo1.setRequstNum(0);
		
		UrlPackInfo urlInfo2 = new UrlPackInfo();
		urlInfo2.setOriginalUrl("BBB.BBB.BBB");
		urlInfo2.setShortenedUrl(UrlMaker.makeUrlWithDomain(encoder.encoding(2L)));
		urlInfo2.setRequstNum(0);
		
		mock.perform(get("/urlpack/list").requestAttr("loginUser", testUser))
			.andExpect(model().attribute("urlPackListDto", 
				hasProperty("urlPackInfoList", 
					hasItems(urlInfo1, urlInfo2))));
		
	}

}
