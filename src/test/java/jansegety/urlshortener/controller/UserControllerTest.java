package jansegety.urlshortener.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.interceptor.AuthLoginInterceptor;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.repository.UserRepository;
import jansegety.urlshortener.testutil.constant.URL;


@SpringBootTest
public class UserControllerTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UrlPackRepository urlPackRepository;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private UrlPackController urlPackController;
	
	@Autowired
	private AuthLoginInterceptor authLoginInterceptor;
	
	private MockMvc mock;
	
	@BeforeEach
	public void setup() {
		
		mock = MockMvcBuilders
			.standaloneSetup(userController, urlPackController)
			.addInterceptors(authLoginInterceptor)
			.setViewResolvers(getViewResolver())
			.build();
		
		userRepository.deleteAll();
		urlPackRepository.deleteAll();
		
		//테스트용 유저
		User testUser = new User();
		testUser.setEmail("testUser@gmail.com");
		testUser.setPassword("1234abcd!@?~");
		userRepository.save(testUser);
		
	}
	
	  public ViewResolver getViewResolver() {
	        InternalResourceViewResolver resolver = 
        		new InternalResourceViewResolver();
	        
	        resolver.setPrefix("classpath:/templates/");
	        resolver.setSuffix(".html");
	        return resolver;
	 }
	  
	  
	@Test
	@DisplayName("로그인 하지 않고 urlpack/registform에 get 요청하면 login 페이지로 redirect 된다.")
	public void when_requestGetUrlPackRegistFrom_then_redirectToLoginPage() 
			throws Exception {
		
		mock.perform(get("/urlpack/registform"))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "/user/login"));
			
	}
	
	@Test
	@DisplayName("로그인 하지 않고 urlpack/regist에 post 요청하면 login 페이지로 redirect 된다.")
	public void when_requestPostUrlPackRegist_then_redirectToLoginPage() 
			throws Exception {
		
		mock.perform(post("/urlpack/regist")
				.param("originalUrl",URL.MOCK_ORIGINAL_URL))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "/user/login"));
		
	}
	
	
	@Test
	@DisplayName("로그인하지 않고 url 리스트를 보려고 하면 로그인 페이지로 redirect 된다.")
	public void when_tryToViewTheUrlListWithoutLogin_then_RedirectedToTheLoginPage()
			throws Exception {
	
		mock.perform(get("/urlpack/list"))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "/user/login"));
	}
	
	
	@Test
	@DisplayName("로그인하고 url 리스트를 보려고 하면 list 뷰를 반환한다.")
	public void when_tryToViewTheUrlListWithLogin_then_returnTheListView() 
			throws Exception {
		
		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute("userId", 1L);
		
		mock.perform(get("/urlpack/list").session(httpSession))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("/urlpack/list"));
		
	}
		
	@Test
	@DisplayName("세션에 잘못된 사용자 id를 가지고 있다면 login 페이지로 redirect 된다.")
	public void when_haveTheWrongUserIdInTheSession_then_RedirectedToTheloginPage() 
			throws Exception {
		
		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute("userId", 2L); //<- uesr repository에 저장되지 않은 user id
		
		mock.perform(get("/urlpack/list").session(httpSession))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "/user/login"));	
	}
	
}
