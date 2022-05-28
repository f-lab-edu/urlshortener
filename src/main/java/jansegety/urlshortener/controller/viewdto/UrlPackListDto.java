package jansegety.urlshortener.controller.viewdto;

import java.util.ArrayList;
import java.util.List;

import jansegety.urlshortener.entity.UrlPack;
import jansegety.urlshortener.util.UrlMaker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlPackListDto {
	
	private List<UrlPackInfo> urlPackInfoList;
	
	public static UrlPackListDto makeUrlPackListDto(List<UrlPack> urlPackList) {
		List<UrlPackInfo> urlPackInfoList = new ArrayList<>();
		for(UrlPack urlPack : urlPackList)
		{
			UrlPackInfo urlInfo = new UrlPackInfo();
			urlInfo.setOriginalUrl(urlPack.getOriginalUrl());
			urlInfo.setShortenedUrl(
					UrlMaker.makeUrlWithDomain(
							urlPack.getValueCompressed()));
			urlInfo.setRequstNum(urlPack.getRequestNum());
			urlPackInfoList.add(urlInfo);
		}
		UrlPackListDto urlPackListDto = new UrlPackListDto();
		urlPackListDto.setUrlPackInfoList(urlPackInfoList);
		return urlPackListDto;
	}

}
