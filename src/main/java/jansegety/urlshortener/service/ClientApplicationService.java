package jansegety.urlshortener.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.entity.ClientApplication;

@Service
public interface ClientApplicationService {

	public void save(ClientApplication clientApplication);
	
	public Optional<ClientApplication> findById(UUID id);
	
}
