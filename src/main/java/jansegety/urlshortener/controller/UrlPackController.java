package jansegety.urlshortener.controller;

import static jansegety.urlshortener.controller.viewdto.UrlPackListDto.makeUrlPackListDto;
import static jansegety.urlshortener.controller.viewdto.UrlPackRegistConfirmationDto.makeRgistFormDto;
import static jansegety.urlshortener.entity.UrlPack.makeUrlPackRegisteredAndHavingValueCompressed;
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
import jansegety.urlshortener.service.encoding.Encoder;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/urlpack")
@RequiredArgsConstructor
public class UrlPackController {
	
	private final UrlPackService urlPackService;
	private final Encoder<Long, String> encoder;

	@RequestMapping(value = "/registform", method = GET)
	public String createForm() {
		return "/urlpack/registform";
	}
	
	@RequestMapping(value = "/registform", method = POST)
	public String create(@RequestParam String originalUrl, 
			@RequestAttribute(required=true) User loginUser, 
			Model model) {
		
		UrlPack urlPack = 
			makeUrlPackRegisteredAndHavingValueCompressed(
				loginUser, 
				originalUrl, 
				urlPackService, 
				encoder);
		
		UrlPackRegistConfirmationDto urlPackRegistConfirmationDto = 
				makeRgistFormDto(urlPack);
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
