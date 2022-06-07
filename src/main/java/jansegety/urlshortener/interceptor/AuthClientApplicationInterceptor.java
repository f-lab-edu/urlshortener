package jansegety.urlshortener.interceptor;

import static jansegety.urlshortener.error.message.ClientApplicationMessage.*;
import static jansegety.urlshortener.error.message.UserMessage.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jansegety.urlshortener.entity.ClientApplication;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.error.exception.InvalidClientException;
import jansegety.urlshortener.error.exception.InvalidSecretException;
import jansegety.urlshortener.error.exception.entity.clientapplication.AuthClientApplicationException;
import jansegety.urlshortener.service.ClientApplicationService;
import jansegety.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;

/*
 * rest 통신을 하는 client application을 검증합니다.
 */
@RequiredArgsConstructor
public class AuthClientApplicationInterceptor implements HandlerInterceptor {
	
	@Value("${path.authClient.essential}")
	public List<String> authClientEssential;
	
	private final UserService userService;

	private final ClientApplicationService clientApplicationService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler) throws Exception {
		
		//application 검증 로직
	    String clientId = request.getHeader("urlshortener-client-id");
	    String clientSecret = request.getHeader("urlshortener-client-secret");
	
		//urlshortener-client-id 혹은 urlshortener-client-secret가 없다면
		if(!StringUtils.hasText(clientId)||!StringUtils.hasText(clientSecret)) {
			throw new IllegalArgumentException(
					CLIENT_ID_OR_SECRET_IS_REQUIRED.getMessage());
		}
		
		UUID clientIdUUID = UUID.fromString(clientId);
		UUID clientSecretUUID = UUID.fromString(clientSecret);
		
		Optional<ClientApplication> clientApplicationOp = 
			clientApplicationService.findById(clientIdUUID);
		
		//일치하는 client가 없다면
		if(clientApplicationOp.isEmpty()) {
			throw new InvalidClientException(
					NO_MATCHING_CLIENT_FOUND.getMessage());
		}
		
		//secret이 일치하지 않는다면
		ClientApplication clientApplication = clientApplicationOp.get();
		System.out.println("clientApplication = " + clientApplication);
		if(!clientApplication.equalsClientSecret(clientSecretUUID)) {
			throw new InvalidSecretException(
					NO_MATCHING_SECRET_FOUND.getMessage());
		}
			
		//user 검증 로직
		//ClientApplication의 user_id는 not null 제약조건이 있으므로 일반적으로 이 예외가 발생할 가능성은 없다.
		Long clientUserId = clientApplication.getUserId();
		if(clientUserId == null) {
			throw new IllegalStateException(
					CLIENT_HAS_NO_USER.getMessage());
		}
		
		//client를 등록한 유저 세션에 등록
		HttpSession session = request.getSession();
		User clientUser = userService.findById(clientUserId)
			.orElseThrow(
				()->new AuthClientApplicationException(USER_NOT_REGISTERED.getMessage()));
		
		session.setAttribute("clientUser", clientUser);		
		
		return true;
	}
	
}
