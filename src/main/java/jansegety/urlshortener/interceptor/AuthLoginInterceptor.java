package jansegety.urlshortener.interceptor;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class AuthLoginInterceptor implements HandlerInterceptor {
	
	@Value("${path.login.inEssential}")
	public List<String> loginInEssential;
	
	private final UserService userService;
	
	
	@Override
    public boolean preHandle(HttpServletRequest request, 
    		HttpServletResponse response, 
    		Object handler) throws Exception {
		
		Long userId = (Long)request.getSession().getAttribute("userId");
		
		//세션에 사용자 id가 있다면
		if(userId != null) {	
			Optional<User> userOp = userService.findById(userId);
			//세션 사용자 id가 등록된 사용자라면
			if(userOp.isPresent()) {
				//loginUser를 반환받아 request에 넣어준다
				request.setAttribute("loginUser", userOp.get());
				return true;
			}
		}
		
		//세션에 사용자 id가 없다면
        String destUri = request.getRequestURI();
        String destQuery = request.getQueryString();
        String dest = (destQuery == null) ? destUri : destUri+"?"+destQuery;
        request.getSession().setAttribute("dest", dest);
    
        response.sendRedirect("/user/login");
        return false;
    }

}
