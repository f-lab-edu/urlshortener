package jansegety.urlshortener.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.ClientApplication;

@Repository
public interface ClientApplicationRepository {
	
	public void save(ClientApplication ca);
	public void deleteAll();
	public Optional<ClientApplication> findById(UUID id);
	
}
