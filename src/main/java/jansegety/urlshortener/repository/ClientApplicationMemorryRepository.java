package jansegety.urlshortener.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.ClientApplication;


@Repository
public class ClientApplicationMemorryRepository implements ClientApplicationRepository{
	
	private List<ClientApplication> clientApplicationList = new ArrayList<>(); 
	
	@Override
	public void save(ClientApplication clientApplication) {
		clientApplication.setId(UUID.randomUUID());
		clientApplicationList.add(clientApplication);
	}

	@Override
	public void deleteAll() {
		clientApplicationList = new ArrayList<>(); 
	}

	@Override
	public Optional<ClientApplication> findById(UUID id) {		
		return clientApplicationList.stream()
			.filter(e->e.getId().equals(id))
			.findAny();
	}
	
}
