package jansegety.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jansegety.urlshortener.interceptor.AuthClientApplicationInterceptor;
import jansegety.urlshortener.interceptor.AuthLoginInterceptor;
import jansegety.urlshortener.service.ClientApplicationService;
import jansegety.urlshortener.service.UserService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
		//로그인 검증
        AuthLoginInterceptor authloginIntercepter = 
    		getAuthloginIntercepter();
        registry.addInterceptor(authloginIntercepter)
            .excludePathPatterns(authloginIntercepter.loginInEssential);
        
        //클라이언트 검증
        AuthClientApplicationInterceptor authClientApplicationInterceptor = 
    		getAuthClientApplicationInterceptor();
        registry.addInterceptor(authClientApplicationInterceptor)
    		.addPathPatterns(authClientApplicationInterceptor.authClientEssential);
    }
   
    @Autowired
    private UserService userService;
    
    @Autowired
    private ClientApplicationService clientApplicationService;
    
	@Bean
	public AuthLoginInterceptor getAuthloginIntercepter(){
		return new AuthLoginInterceptor(userService);
	}
    
    @Bean
    public AuthClientApplicationInterceptor getAuthClientApplicationInterceptor() {
    	return new AuthClientApplicationInterceptor(userService, clientApplicationService);
    }
    
}