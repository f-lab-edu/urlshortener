package jansegety.urlshortener.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.controller.form.LoginForm;
import jansegety.urlshortener.entity.User;

@Service
public interface UserService {
	
	public void save(User user);

	public Optional<User> findById(Long id);

	public Optional<User> findByLoginForm(LoginForm loginForm);
	
}
