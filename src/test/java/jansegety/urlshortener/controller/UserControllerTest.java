package jansegety.urlshortener.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.interceptor.AuthLoginInterceptor;
import jansegety.urlshortener.repository.UrlPackRepository;
import jansegety.urlshortener.repository.UserRepository;
import jansegety.urlshortener.repository.memoryrepository.UrlPackMemoryRepository;
import jansegety.urlshortener.repository.memoryrepository.UserMemoryRepository;
import jansegety.urlshortener.testutil.constant.MockUserField;
import jansegety.urlshortener.testutil.constant.URL;

//mybatis dao를 테스트하려면 아래의 
//@ActiveProfiles("concurrent-test")와 
//@EnableAutoConfiguration을 주석처리하고
//아래의 주석된 것을 풀어서 실행하면 된다.
@ActiveProfiles("dev")
@Execution(ExecutionMode.SAME_THREAD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@EnableAutoConfiguration(exclude= {HibernateJpaAutoConfiguration.class})
//@ActiveProfiles("concurrent-test")
//@EnableAutoConfiguration(exclude= { 
//		DataSourceAutoConfiguration.class, 
//		DataSourceTransactionManagerAutoConfiguration.class, 
//		HibernateJpaAutoConfiguration.class,
//		MybatisAutoConfiguration.class})
@SpringBootTest
public class UserControllerTest {

	@Autowired
	private UserRepository userMapper;
	
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
		
		if(urlPackRepository instanceof UrlPackMemoryRepository) {
			UrlPackMemoryRepository urlPackMemoryRepository = (UrlPackMemoryRepository)urlPackRepository;
			urlPackMemoryRepository.deleteAll();
		}
		
		if(userMapper instanceof UserMemoryRepository) {
			UserMemoryRepository userMemoryRepository = (UserMemoryRepository)userMapper;
			userMemoryRepository.deleteAll();
		}
		
		//테스트용 유저
		User testUser = new User(MockUserField.EMAIL, MockUserField.PASSWORD);
		userMapper.save(testUser);
		
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
