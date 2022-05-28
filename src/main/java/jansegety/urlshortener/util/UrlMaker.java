package jansegety.urlshortener.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlMaker {
	
	private static String domainName;
	
	@Value("${domain.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	public static String makeUrlWithDomain(String additionalUrl) {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		return stringBuilder.append("https://").append(domainName).append("/")
				.append(additionalUrl).toString();
	}

}
