package jansegety.urlshortener.entity;

import static jansegety.urlshortener.error.message.SimpleEntityMessage.*;
import static jansegety.urlshortener.error.message.UrlPackMessage.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.compressing.ValueCompressedMaker;
import jansegety.urlshortener.service.compressing.sourceprovider.CompressingSourceProvider;


/*
 * id가 할당될 때 shortUrl이 초기화 된다.
 */
@Table(name = "url_pack")
@Entity
public class UrlPack{
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "original_url")
	private String origianlUrl;
	@Column(name = "value_compressed")
	private String valueCompressed;
	
	@Column(name = "request_num")
	private Integer requestNum=0;
	
	@Column(name = "user_id")
	private Long userId;

	public static UrlPack makeUrlPackRegisteredAndHavingValueCompressed(
			User user,
			String originalUrl,
			UrlPackService urlPackService,
			CompressingSourceProvider<String> compressingSourceProvider,
			ValueCompressedMaker<String, String> valueCompressedMaker) {
		
		UrlPack newUrlPack = new UrlPack();
		newUrlPack.setUserId(user.getId());
		newUrlPack.setOriginalUrl(originalUrl);
		newUrlPack
			.createValueEncoded(
				urlPackService, 
				compressingSourceProvider, 
				valueCompressedMaker);
		urlPackService.regist(newUrlPack); //mysql에서는 모든 필드가 세팅된 뒤에 영속화 해줘야 함
		
		return newUrlPack;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		
		if(this.id != null)
			throw new IllegalStateException(
					ID_HAS_ALREADY_BEEN_ASSIGNED.getMessage());
		
		this.id = id;
	}
	
	public Long getUserId() {
		return this.userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getOriginalUrl() {
		return origianlUrl;
	}
	
	public void setOriginalUrl(String originalUrl) {
		this.origianlUrl = originalUrl;
	}
	
	public String getValueCompressed() {
		return valueCompressed;
	}
	
	public Integer getRequestNum() {
		return requestNum;
	}
	
	public void setRequestNum(Integer requestNum) {
		this.requestNum = requestNum;
	}
	
	
	public String requestShortUrlWithOriginalUrl(String originalUrl) {
		if(!this.origianlUrl.equals(originalUrl))
			throw new IllegalArgumentException(
					ORIGiNAL_URL_DOES_NOT_MATCH.getMessage());
		requestNum++;
		return valueCompressed;
	}
	
	private void createValueEncoded(
			UrlPackService urlPackService, 
			CompressingSourceProvider<String> compressingSourceProvider, 
			ValueCompressedMaker<String, String> valueCompressedMaker) {
		
		String valueCompressed;
		
		//소스 제공자의 제한 제공 수를 초기화 합니다.
		compressingSourceProvider.init();
		
		do {	
			String source = compressingSourceProvider.getSource();
			valueCompressed = valueCompressedMaker.compress(source);
		//만약 같은 값의 단축 url이 존재한다면 다시 단축 알고리즘을 진행합니다.	
		} while(urlPackService.isPresentUrlPackWithValueCompressed(valueCompressed));
		
		this.valueCompressed = valueCompressed;
	}
	
	@Override
	public String toString() {
		return "UrlPack [id=" + id + ", originalUrl=" + origianlUrl 
				+ ", valueEncoded=" + valueCompressed + "]";
	}

}
