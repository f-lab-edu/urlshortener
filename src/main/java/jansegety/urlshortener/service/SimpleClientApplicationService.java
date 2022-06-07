package jansegety.urlshortener.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.entity.ClientApplication;
import jansegety.urlshortener.repository.ClientApplicationRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SimpleClientApplicationService implements ClientApplicationService {

	private final ClientApplicationRepository clientApplicationRepository;
	
	@Override
	public void regist(ClientApplication clientApplication) {
		UUID randomUUID = UUID.randomUUID();
		clientApplication.setId(randomUUID); //DB에 들어가기 전에 어플리케이션에서 UUID를 할당해준다.
		clientApplicationRepository.save(clientApplication);
	}

	@Override
	public Optional<ClientApplication> findById(UUID id) {
		return clientApplicationRepository.findById(id.toString());
	}
	
}
