package jansegety.urlshortener.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.entity.ClientApplication;
import jansegety.urlshortener.repository.ClientApplicationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimpleClientApplicationService implements ClientApplicationService {

	private final ClientApplicationRepository clientApplicationRepository;
	
	@Override
	public void save(ClientApplication clientApplication) {
		clientApplicationRepository.save(clientApplication);
	}

	@Override
	public Optional<ClientApplication> findById(UUID id) {
		return clientApplicationRepository.findById(id);
	}
	
}
