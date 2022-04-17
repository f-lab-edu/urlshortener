package jansegety.urlshortener.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;

@Service
public interface UrlPackService {
	
	public void regist(UrlPack urlPack);
	
	public List<UrlPack> findAll();
	
	public List<UrlPack> findByUser(User user);
	
	public Optional<UrlPack> findByValueEncoded(String shortUrl);

}
