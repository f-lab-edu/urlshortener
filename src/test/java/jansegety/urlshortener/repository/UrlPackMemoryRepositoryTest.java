package jansegety.urlshortener.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.repository.memoryrepository.UrlPackMemoryRepository;

class UrlPackMemoryRepositoryTest {
	
	UrlPackMemoryRepository repository = new UrlPackMemoryRepository();
	
	@Test
	@DisplayName("id가 이미 할당된 UrlPack Entity가 저장되면 예외가 발생한다.")
	public void when_saveUrlPackIdAssigned_then_throwIllegalArgumentException() {
		
		UrlPack urlPack = new UrlPack();
		urlPack.setId(1L);
		
		UrlPack IdAssignedUrlPack = urlPack;
		
		assertThrows(IllegalStateException.class, 
				()->repository.save(IdAssignedUrlPack));
		
	}
	
	@Test
	@DisplayName("id가 할당되지 않은 UrlPack은 저장될 때 index를 차례대로 할당받는다.")
	public void when_saveUrlPackWithNoId_then_idIsAutomaticallyAssignedInTurn() {
		
		UrlPack urlPack1 = new UrlPack();
		urlPack1.setOriginalUrl("www.111.111");
		UrlPack urlPack2 = new UrlPack();
		urlPack1.setOriginalUrl("www.222.222");
		
		repository.save(urlPack1);
		repository.save(urlPack2);
		
		assertThat(urlPack1.getId(), is(equalTo(1L)));
		assertThat(urlPack2.getId(), is(equalTo(2L)));
		
	}
	

}
