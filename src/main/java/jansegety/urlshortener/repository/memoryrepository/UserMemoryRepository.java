package jansegety.urlshortener.repository.memoryrepository;

import static jansegety.urlshortener.error.message.UserMessage.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.error.message.SimpleEntityMessage;
import jansegety.urlshortener.repository.UserRepository;

@Repository
@Profile("concurrent-test")
public class UserMemoryRepository implements UserRepository {
	
	private final ThreadLocal<Map<Long, User>> localMap = ThreadLocal.withInitial(HashMap::new);
	private final ThreadLocal<Long> localSequence = ThreadLocal.withInitial(()->1L);
	
	@Override
	public void save(User user) {
		Map<Long, User> map = localMap.get();
		if(user.getId()!=null) {
			throw new IllegalStateException(
					SimpleEntityMessage.ID_HAS_ALREADY_BEEN_ASSIGNED.getMessage());
		}
		
		Long nextSequence = localSequence.get();
		PrivateSetter.setId(user, nextSequence);
		
		map.put(nextSequence, user);
		
		nextSequence++;
		localSequence.set(nextSequence);
	}
	
	@Override
	public Optional<User> findById(Long id) {
		Map<Long, User> map = localMap.get();
		User menu = map.get(id);
		return Optional.ofNullable(menu);
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
		Map<Long, User> map = localMap.get();
		
		return map.entrySet().stream()
				.map(e->e.getValue())
				.filter(e->{
					if(e.getEmail()==null) {
						throw new IllegalStateException(NO_EMAIL_ASSIGNED_TO_USER.getMessage());}
					return e.getEmail().equals(email);})
				.findAny();
	}
	
	public void deleteAll() {
		localMap.set(new HashMap<Long, User>());
		localSequence.set(1L);
	}

}
