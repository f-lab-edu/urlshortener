package jansegety.urlshortener.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.User;

@Repository
public interface UserRepository {
	
	public void save(User user);
	public Optional<User> findById(Long id);
	public Optional<User> findByEmail(String email);
	public void deleteAll();
	
}
