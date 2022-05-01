package jansegety.urlshortener.controller;

import static jansegety.urlshortener.controller.jsondto.CreateShortUrlDto.makeCreateShortUrlDto;
import static jansegety.urlshortener.entity.UrlPack.makeUrlPackRegisteredAndHavingValueCompressed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import jansegety.urlshortener.controller.jsondto.CreateShortUrlDto;
import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.compressing.ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;
import jansegety.urlshortener.service.encoding.Encoder;
import jansegety.urlshortener.service.hashing.Hasher;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/urlpack/util")
@RequiredArgsConstructor
public class UrlPackJsonController {

	private final UrlPackService urlPackService;
	
	private final ValueCompressedMaker<String, String> valueCompressedMaker;
	
	private final CompressingSourceProvider<String> compressingSourceProvider;
	
	
	/*
	 * 등록된 application인지 탐색하고 
	 * 만약 있다면 해당 어플리케이션의 urlPack으로 등록한다.
	 * 만약 없다면 어플리케이션을 등록해달라는 안내페이지로 이동시킨다.
	 */
	@PostMapping("/shorturl")
	public CreateShortUrlDto create(
			@RequestParam("url") String originalUrl,
			@SessionAttribute User clientUser) {
		
		//성공 로직
		UrlPack urlPack = 
			makeUrlPackRegisteredAndHavingValueCompressed(
				clientUser, 
				originalUrl, 
				urlPackService,
				compressingSourceProvider,
				valueCompressedMaker);
	
		CreateShortUrlDto createShortUrlDto = makeCreateShortUrlDto(urlPack);
		
		return createShortUrlDto;
	}
	
}
