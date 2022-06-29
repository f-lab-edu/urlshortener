package jansegety.urlshortener.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;

@Mapper
public interface UrlPackRepository {

	public void save(UrlPack urlPack);
	public void update(UrlPack urlPack);
	public List<UrlPack> findAll();
	public List<UrlPack> findByUser(User user);
	public Optional<UrlPack> findByValueCompressed(String valueCompressed);
	
}
