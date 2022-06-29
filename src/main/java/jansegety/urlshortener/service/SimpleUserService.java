package jansegety.urlshortener.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.controller.form.LoginForm;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SimpleUserService implements UserService {
	
	private final UserRepository userRepository;
	
	@Override
	public void regist(User user) {
		userRepository.save(user);
	}
	
	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> findByLoginForm(LoginForm loginForm) {
		return userRepository.findByEmail(loginForm.getEmail());
	}

}
