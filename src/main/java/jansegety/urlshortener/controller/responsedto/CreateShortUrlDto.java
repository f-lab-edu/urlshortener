package jansegety.urlshortener.controller.responsedto;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.util.UrlMaker;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateShortUrlDto {
	
	private Result result;
	
	public static CreateShortUrlDto makeCreateShortUrlDto(UrlPack urlPack) {
		CreateShortUrlDto createShortUrlDto = new CreateShortUrlDto();
		
		Result result = createShortUrlDto.new Result();
		result.setOrginalUrl(urlPack.getOriginalUrl());
		result.setShortenedUrl(
				UrlMaker.makeUrlWithDomain(urlPack.getValueCompressed()));
		
		createShortUrlDto.setResult(result);
		return createShortUrlDto;
	}
	
	@Getter
	@Setter
	@ToString
	public class Result{
		
		private String shortenedUrl;
		private String orginalUrl;	
		
	}
	
}
