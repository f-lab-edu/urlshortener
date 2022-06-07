package jansegety.urlshortener.repository.memoryrepository;

import static jansegety.urlshortener.error.message.SimpleEntityMessage.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.ClientApplication;
import jansegety.urlshortener.repository.ClientApplicationRepository;

@Profile("concurrent-test")
@Repository
public class ClientApplicationMemoryRepository implements ClientApplicationRepository{
	
	private final ThreadLocal<Map<String, ClientApplication>> localMap = ThreadLocal.withInitial(HashMap::new); 
	
	@Override
	public void save(ClientApplication clientApplication) {
		Map<String, ClientApplication> map = localMap.get();
		if(clientApplication.getId()==null) {
			throw new IllegalStateException(NO_ID_ASSIGNED.getMessage());
		}

		map.put(clientApplication.getId().toString(), clientApplication);
	}
	
	@Override
	public Optional<ClientApplication> findById(String uuid) {		
		Map<String, ClientApplication> map = localMap.get();
		ClientApplication clientApplication = map.get(uuid);
		return Optional.ofNullable(clientApplication);
	}

	public void deleteAll() {
		localMap.set(new HashMap<String, ClientApplication>());
	}
	
}
