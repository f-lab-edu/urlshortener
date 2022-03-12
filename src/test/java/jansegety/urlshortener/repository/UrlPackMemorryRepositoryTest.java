package jansegety.urlshortener.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.service.encoding.Base62Encoder;
import jansegety.urlshortener.service.encoding.Encoder;

class UrlPackMemorryRepositoryTest {
	

	UrlPackMemorryRepository repository = new UrlPackMemorryRepository();
	Encoder<Long, String> encoder = new Base62Encoder();

	@Test
	@DisplayName("id가 이미 할당된 UrlPack Entity가 저장되면 예외가 발생한다.")
	public void when_saveUrlPackIdAssigned_then_throwIllegalArgumentException() {
		
		UrlPack urlPack = new UrlPack(encoder);
		urlPack.setIdCreatingShortUrl(1L);
		
		UrlPack IdAssignedUrlPack = urlPack;
		
		assertThrows(IllegalArgumentException.class, ()->{
			repository.save(IdAssignedUrlPack);
		});
		
	}
	
	@Test
	@DisplayName("id가 할당되지 않은 UrlPack은 저장될 때 index를 차례대로 할당받는다.")
	public void when_saveUrlPackWithNoId_then_idIsAutomaticallyAssignedInTurn() {
		
		UrlPack urlPack1 = new UrlPack(encoder);
		urlPack1.setLongUrl("www.111.111");
		UrlPack urlPack2 = new UrlPack(encoder);
		urlPack1.setLongUrl("www.222.222");
		
		repository.save(urlPack1);
		repository.save(urlPack2);
		
		assertThat(urlPack1.getId(), is(equalTo(1L)));
		assertThat(urlPack2.getId(), is(equalTo(2L)));
		
		
	}
	
	@Test
	@DisplayName("반환 받은 list의 entity의 상태를 바꿔도 저장소의 entity애 영향이 없어야 한다.")
	public void when_entityInTheReturnedListIsChanged_then_entityInTheRepositoryShouldNotChange()
	{
		UrlPack urlPackSaved = new UrlPack(encoder);
		urlPackSaved.setLongUrl("www.111.111");
		repository.save(urlPackSaved);
		
		List<UrlPack> findList = repository.findList();
		
		UrlPack urlPackReturned = findList.get(0);
		assertThat(urlPackReturned.getLongUrl(), is(equalTo("www.111.111")));
		urlPackReturned.setLongUrl("aaa.aaa.aaa");
		
		assertThat(urlPackSaved.getLongUrl(), is(equalTo("www.111.111")));
		
		
	}

	

}