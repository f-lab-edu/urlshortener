package jansegety.urlshortener.controller;

import static jansegety.urlshortener.controller.viewdto.UrlPackListDto.*;
import static jansegety.urlshortener.controller.viewdto.UrlPackRegistConfirmationDto.*;
import static jansegety.urlshortener.entity.UrlPack.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jansegety.urlshortener.controller.viewdto.UrlPackListDto;
import jansegety.urlshortener.controller.viewdto.UrlPackRegistConfirmationDto;
import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.entity.User;
import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.compressing.ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/urlpack")
@RequiredArgsConstructor
public class UrlPackController {
	
	private final UrlPackService urlPackService;
	private final ValueCompressedMaker<String, String> valueCompressedMaker;
	private final CompressingSourceProvider<String> compressingSourceProvider;

	@RequestMapping(value = "/registform", method = GET)
	public String createForm() {
		return "/urlpack/registform";
	}
	
	@RequestMapping(value = "/regist", method = POST)
	public String create(@RequestParam String originalUrl, 
			@RequestAttribute(required=true) User loginUser, 
			Model model) {
		
		UrlPack urlPack = 
			makeUrlPackRegisteredAndHavingValueCompressed(
				loginUser, 
				originalUrl, 
				urlPackService,
				compressingSourceProvider,
				valueCompressedMaker);
		
		UrlPackRegistConfirmationDto urlPackRegistConfirmationDto = 
				makeRegistConfirmationDto(urlPack);
		model.addAttribute("urlPackRegistConfirmationDto"
			, urlPackRegistConfirmationDto);
		
		return "/urlpack/registconfirmation";
	}

	
	//로그인 유저의 urlpack list를 넣은 뷰를 반환한다
	@RequestMapping(value = "/list", method = GET)
	public String show(
			Model model, 
			@RequestAttribute(required=true) User loginUser) {
	
		List<UrlPack> urlPackList = urlPackService.findByUser(loginUser);
		
		UrlPackListDto urlPackListDto = makeUrlPackListDto(urlPackList);
		model.addAttribute("urlPackListDto", urlPackListDto);
		
		return "/urlpack/list";
	}
}
