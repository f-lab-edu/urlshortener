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
	
	@RequestMapping("/{valueCompressed}")
	public void redirectTooriginalUrl(@PathVariable String valueCompressed, 
			HttpServletResponse response) {
		
		urlPackService
			.increaseByOneTheNumberOfRequestForShortenedUrlCorrespondingToValueCompressed(
					valueCompressed, response);
		
	}
	
}
