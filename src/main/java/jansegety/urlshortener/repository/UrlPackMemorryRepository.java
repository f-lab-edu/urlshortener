package jansegety.urlshortener.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;

@Repository
public class UrlPackMemorryRepository implements UrlPackRepository{
	
    private List<UrlPack> urlPackList = new ArrayList<>();
    private Long lastIndex = 1L;

	@Override
	synchronized public void save(UrlPack urlPack) {
		urlPack.setId(lastIndex++);
		urlPackList.add(urlPack);
	}
	
	@Override
	public List<UrlPack> findAll() {
		return urlPackList;
	}

	@Override
	public void deleteAll() {
		this.urlPackList = new ArrayList<>();
		lastIndex = 1L;
	}

	@Override
	public List<UrlPack> findByUser(User user) {
		return urlPackList.stream()
			.filter(e->e.getUser()==null ? false : e.getUser().equals(user))
			.collect(Collectors.toList());
	}

}
