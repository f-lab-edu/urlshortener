package jansegety.urlshortener.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.User;

@Repository
public class UserMemorryRepository implements UserRepository {
	
	List<User> userList = new ArrayList<User>(); 
	
	private Long nextIndex = 1L;

	@Override
	public void save(User user) {
		user.setId(nextIndex);
		nextIndex++;
		userList.add(user);
	}
	
	@Override
	public Optional<User> findById(Long id){
		Optional<User> userOp = userList.stream()
			.filter(e->e.getId() == id)
			.findAny();
		
		return userOp;
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
		Optional<User> userOp = userList.stream()
			.filter(e->e.getEmail().equals(email))
			.findAny();
		
		return userOp;
	}
	
	@Override
	public void deleteAll() {
		userList = new ArrayList<User>();
		nextIndex = 1L;
	}

}
