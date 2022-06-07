package jansegety.urlshortener.repository.memoryrepository;

import static jansegety.urlshortener.error.message.SimpleEntityMessage.*;
import static jansegety.urlshortener.error.message.UrlPackMessage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.error.message.SimpleEntityMessage;
import jansegety.urlshortener.error.message.UrlPackMessage;
import jansegety.urlshortener.repository.UrlPackRepository;

@Profile("concurrent-test")
@Repository
public class UrlPackMemoryRepository implements UrlPackRepository{
	
	private final ThreadLocal<Map<Long, UrlPack>> localMap = ThreadLocal.withInitial(HashMap::new);
	private final ThreadLocal<Long> localSequence = ThreadLocal.withInitial(()->1L);

	@Override
	synchronized public void save(UrlPack urlPack) {
		Map<Long, UrlPack> map = localMap.get();
		if(urlPack.getId()!=null) {
			throw new IllegalStateException(
				ID_HAS_ALREADY_BEEN_ASSIGNED.getMessage());
		}
		
		Long nextSequence = localSequence.get();
		PrivateSetter.setId(urlPack, nextSequence);
		
		map.put(nextSequence, urlPack);
		
		nextSequence++;
		localSequence.set(nextSequence);
	}
	
	@Override
	public List<UrlPack> findAll() {
		Map<Long, UrlPack> map = localMap.get();
		return map.entrySet().stream()
			.map(e->e.getValue())
			.sorted((e1, e2)->(int) (e1.getId()-e2.getId()))
			.collect(Collectors.toList());
	}

	public void deleteAll() {
		localMap.set(new HashMap<Long, UrlPack>());
		localSequence.set(1L);
	}

	@Override
	public List<UrlPack> findByUser(User user) {
		Map<Long, UrlPack> map = localMap.get();
		return map.entrySet().stream()
			.map(e->e.getValue())
			.filter(e->{
				if(e.getUserId()==null) {
					throw new IllegalStateException(
						NO_USER_ASSIGNED_TO_URLPACK.getMessage());
				}
				return e.getUserId().equals(user.getId());})
			.collect(Collectors.toList());
	}

	@Override
	public void update(UrlPack urlPack) {
		Map<Long, UrlPack> map = localMap.get();
		if(map.containsKey(urlPack.getId())) {
			throw new IllegalArgumentException(
				URL_PACK_ENTITY_CORRESPONDING_TO_ID_DOES_NOT_EXIST.getMessage());
		}
		map.put(urlPack.getId(), urlPack);
	}

	@Override
	public Optional<UrlPack> findByValueCompressed(String valueCompressed) {
		Map<Long, UrlPack> map = localMap.get();
		return map.entrySet().stream()
			.map(e->e.getValue())
			.filter(e->{
					//getValueCompressed()가 null일 수 있기 때문에 반드시 체크해줘야 한다.
					if(e.getValueCompressed() == null) {
						throw new IllegalStateException(NO_VALUE_COMPRESSED_ASSIGNED_TO_URLPACK.getMessage());
					}
					return e.getValueCompressed().equals(valueCompressed); 
			}).findAny();
	}

}
