package jansegety.urlshortener.controller.viewdto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UrlPackInfo {

	private String originalUrl;
	private String shortenedUrl;
	private int requstNum;
	
}
