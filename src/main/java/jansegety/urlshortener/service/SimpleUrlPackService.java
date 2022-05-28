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
	public Optional<UrlPack> findByValueCompressed(String valueEncoded) {
		
		Optional<UrlPack> urlPackOrNull = findAll().stream()
			.filter(urlPack -> { 
					//getValueCompressed()가 null일 수 있기 때문에 반드시 체크해줘야 한다.
					//예를 들어서 urlPack이 아무것도 없을때 호출되면 NullPointerException이 발생한다.
					if(urlPack.getValueCompressed() == null) {
						return false;
					}
					return urlPack.getValueCompressed().equals(valueEncoded); 
				})
			.findAny();
		
		return urlPackOrNull;
	}

	@Override
	public List<UrlPack> findByUser(User user) {
		return urlPackRepository.findByUser(user);
	}

}
