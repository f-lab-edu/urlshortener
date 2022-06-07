package jansegety.urlshortener.service;

import static jansegety.urlshortener.error.message.UrlPackMessage.*;
import static javax.servlet.http.HttpServletResponse.*;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.error.message.UrlPackMessage;
import jansegety.urlshortener.repository.UrlPackRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SimpleUrlPackService implements UrlPackService {

	private final UrlPackRepository urlPackRepository;
	
	@Override
	public void regist(UrlPack urlPack) {
		urlPackRepository.save(urlPack);
	}
	
	@Override
	public void update(UrlPack urlPack) {
		urlPackRepository.update(urlPack);
	}

	@Override
	public List<UrlPack> findAll() {
		return urlPackRepository.findAll();
	}

	@Override
	public UrlPack findByValueCompressed(String valueCompressed) {
		return urlPackRepository
			.findByValueCompressed(valueCompressed)
			.orElseThrow(()->
				new IllegalArgumentException(
					URL_PACK_ENTITY_CORRESPONDING_TO_VALUE_COMPRESSED_DOES_NOT_EXIST.getMessage()));
	}

	@Override
	public List<UrlPack> findByUser(User user) {
		return urlPackRepository.findByUser(user);
	}
	
	@Override
	public void increaseByOneTheNumberOfRequestForShortenedUrlCorrespondingToValueCompressed(
			String valueEncoded, 
			HttpServletResponse response) {
		UrlPack urlPack = findByValueCompressed(valueEncoded);
		
		urlPack.setRequestNum(urlPack.getRequestNum()+1);
		//MyBatis를 사용하면 영속성 컨텍스트가 존재하지 않기 때문에 상태를 변경한 entity를 업데이트 해줘야한다.
		update(urlPack);
		
		response.setStatus(SC_MOVED_PERMANENTLY);
		response.setHeader("Location", urlPack.getOriginalUrl());
	}

	@Override
	public boolean isPresentUrlPackWithValueCompressed(String valueCompressed) {
		return urlPackRepository
			.findByValueCompressed(valueCompressed)
			.isPresent();
	}

}
