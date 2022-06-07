package jansegety.urlshortener.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;

@Service
public interface UrlPackService {
	
	public void regist(UrlPack urlPack);
	
	void update(UrlPack urlPack);
	
	public List<UrlPack> findAll();
	
	public List<UrlPack> findByUser(User user);
	
	public UrlPack findByValueCompressed(String shortUrl);
	
	public boolean isPresentUrlPackWithValueCompressed(String valueCompressed);

	public void increaseByOneTheNumberOfRequestForShortenedUrlCorrespondingToValueCompressed(
			String valueEncoded,
			HttpServletResponse response);

}
