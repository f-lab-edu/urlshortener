package jansegety.urlshortener.service.compressing;

public interface ValueCompressedMaker <T, R> {

	public R compress(T source);
	
}
