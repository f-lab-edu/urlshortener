package jansegety.urlshortener.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jansegety.urlshortener.controller.form.LoginForm;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(LoginForm loginForm, BindingResult bindingResult) { 
    	return "/user/loginForm";
	}

    /*
     * 세션에 사용자 id가 없으면 들어온다.
     * 이 함수의 역할은 세션에 id를 할당해서 redirect 해주는 것이다.
     */ 
    @PostMapping("/login")
    public String login(LoginForm loginForm, BindingResult bindingResult, 
    		HttpSession session) {
    	
    	//입력을 잘못하는 경우
    	if(bindingResult.hasErrors()) {
    		return "/user/loginForm";
    	}
    	
    	//loginForm 정보로 사용자가 도메인에 등록되어 있는지 확인한다.
    	Optional<User> userOptional = userService.findByLoginForm(loginForm);
    	
    	//도메인에 사용자가 등록되지 않은 경우
    	if(userOptional.isEmpty()) {
    		//TODO 등록되지 않은 사용자 입니다 라는 메세지를 출력해줘야 한다.
    		return "/user/loginForm";
    	}
    		
    	//로그인 전에 다른 페이지를 요청한 경우 로그인 후 리다이렉트
    	//다른 페이지를 요청하지 않았다면 /main으로
        User loginUser = userOptional.get();
        
        session.setAttribute("userId", loginUser.getId());
        String dest = (String)session.getAttribute("dest");
        String redirect = (dest == null) ? "/main" : dest;
        return "redirect:"+redirect;  
    }
    
}


