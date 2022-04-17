package jansegety.urlshortener.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.repository.UrlPackRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimpleUrlPackService implements UrlPackService {

	private final UrlPackRepository urlPackRepository;
	
	@Override
	public void regist(UrlPack urlPack) {
		urlPackRepository.save(urlPack);
	}

	@Override
	public List<UrlPack> findAll() {
		return urlPackRepository.findAll();
	}

	@Override
	public Optional<UrlPack> findByValueEncoded(String valueEncoded) {
		UrlPack urlPackOrNull = findAll().stream()
			.filter(urlPack -> urlPack.getValueCompressed()
			.equals(valueEncoded))
			.findAny()
			.orElse(null);
		
		return Optional.ofNullable(urlPackOrNull);
	}

	@Override
	public List<UrlPack> findByUser(User user) {
		return urlPackRepository.findByUser(user);
	}

}
