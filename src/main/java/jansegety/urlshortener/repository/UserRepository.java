package jansegety.urlshortener.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import jansegety.urlshortener.entity.User;

@Mapper
public interface UserRepository {
	
	public void save(User user);
	public Optional<User> findById(Long id);
	public Optional<User> findByEmail(String email);
	
}
