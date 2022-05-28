package jansegety.urlshortener.service.encoding;

import org.springframework.stereotype.Component;

@Component
public interface Encoder<S,T> {
	
	public T encode(S source);
	public S decode(T encoded);
	public T encodeWithLengths(S source, int length);
	
}
