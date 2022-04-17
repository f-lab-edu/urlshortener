package jansegety.urlshortener.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.service.UrlPackService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RedirectController {
	
	private final UrlPackService urlPackService;
	
	@RequestMapping("/{valueEncoded}")
	public void redirectTooriginalUrl(@PathVariable String valueEncoded, 
			HttpServletResponse response) {
		
		Optional<UrlPack> opUrlPack = urlPackService.findByValueEncoded(valueEncoded);
		
		if(opUrlPack.isEmpty()){
			response.setStatus(SC_BAD_REQUEST);
			return ;
		}
		
		UrlPack urlPack = opUrlPack.get();
		urlPack.setRequestNum(urlPack.getRequestNum()+1);
		
		response.setStatus(SC_MOVED_PERMANENTLY);
		response.setHeader("Location", urlPack.getOriginalUrl());
		
	}
	
}
