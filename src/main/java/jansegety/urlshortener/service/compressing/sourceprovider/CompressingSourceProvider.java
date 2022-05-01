package jansegety.urlshortener.service.compressing.sourceprovider;

/**
 * ValueCompressedMaker에 알고리즘에 사용할 소스를 제공해줍니다.
 */
public interface CompressingSourceProvider <S> {
	final int DEFAULT_LIMITED_NUMBER_OF_OFFERS = 10;
	public void init();
	public S getSource();
}
