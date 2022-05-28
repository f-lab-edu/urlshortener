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
	
	public static UrlPackRegistConfirmationDto makeRegistConfirmationDto(UrlPack urlPack) {
		UrlPackRegistConfirmationDto registConfirmationDto = new UrlPackRegistConfirmationDto();
		registConfirmationDto.setOriginalUrl(urlPack.getOriginalUrl());
		registConfirmationDto.setShortUrl(UrlMaker.makeUrlWithDomain(urlPack.getValueCompressed()));
		
		return registConfirmationDto;
	}
	
}
