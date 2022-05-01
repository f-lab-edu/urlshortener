package jansegety.urlshortener.service.compressing.sourceprovider;

import static jansegety.urlshortener.error.message.UrlPackMessage.*;

import java.util.UUID;

import org.springframework.stereotype.Component;

import jansegety.urlshortener.error.exception.ValueCompressedException;

@Component
public class UUIDCompressingSourceProvider implements CompressingSourceProvider<String> {
	 
	//동시성 환경에서 각 스레드가 고유한 limit을 사용할 수 있도록 합니다.
	private ThreadLocal<Integer> threadLocalLimit = new ThreadLocal<>();
	
	public UUIDCompressingSourceProvider() {
		threadLocalLimit.set(DEFAULT_LIMITED_NUMBER_OF_OFFERS);
	}

	@Override
	public String getSource() {
		
		Integer limit = threadLocalLimit.get();
		
		if(limit == 0) {
			throw new ValueCompressedException(
				NUMBER_OF_SHORTENED_ALGORITHM_ITERATIONS_EXCEEDED.toString());
		}
		threadLocalLimit.set(--limit);
		
		return UUID.randomUUID().toString().replace("-", "");
	}

	@Override
	public void init() {
		threadLocalLimit.set(DEFAULT_LIMITED_NUMBER_OF_OFFERS);
	}

}
