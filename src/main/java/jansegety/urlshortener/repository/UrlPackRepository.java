package jansegety.urlshortener.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;

@Repository
public interface UrlPackRepository {

	public void save(UrlPack urlPack);
	public List<UrlPack> findAll();
	public List<UrlPack> findByUser(User user);
	public void deleteAll();
	
}
