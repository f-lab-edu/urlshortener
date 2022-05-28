package jansegety.urlshortener.service.hashing;

public interface Hasher<T,R> {
	public R hash(T source);
}
