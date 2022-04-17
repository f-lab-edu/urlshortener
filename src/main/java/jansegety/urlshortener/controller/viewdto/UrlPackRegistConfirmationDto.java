package jansegety.urlshortener.controller.viewdto;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.util.UrlMaker;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UrlPackRegistConfirmationDto {
	
	private String originalUrl;
	private String shortUrl;
	
	public static UrlPackRegistConfirmationDto makeRgistFormDto(UrlPack urlPack) {
		UrlPackRegistConfirmationDto registFormDto = new UrlPackRegistConfirmationDto();
		registFormDto.setOriginalUrl(urlPack.getOriginalUrl());
		registFormDto.setShortUrl(UrlMaker.makeUrlWithDomain(urlPack.getValueCompressed()));
		
		return registFormDto;
	}
	
}
