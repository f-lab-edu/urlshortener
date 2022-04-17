package jansegety.urlshortener.entity;

import static jansegety.urlshortener.error.message.SimpleEntityMessage.*;
import static jansegety.urlshortener.error.message.UrlPackMessage.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jansegety.urlshortener.service.UrlPackService;
import jansegety.urlshortener.service.encoding.Encoder;


/*
 * id가 할당될 때 shortUrl이 초기화 된다.
 */
@Table(name = "url_pack")
@Entity
public class UrlPack{
	
	@Id @GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "original_url")
	private String origianlUrl;
	@Column(name = "value_compressed")
	private String valueCompressed;
	
	@Column(name = "request_num")
	private Integer requestNum=0;
	
	@ManyToOne
	@JoinColumn(name = "id")
	private User user;
	
	public static UrlPack makeUrlPackRegisteredAndHavingValueCompressed(
			User user,
			String originalUrl,
			UrlPackService urlPackService,
			Encoder<Long, String> encoder) {
		
		UrlPack newUrlPack = new UrlPack();
		newUrlPack.setUser(user);
		newUrlPack.setOriginalUrl(originalUrl);
		urlPackService.regist(newUrlPack);
		newUrlPack.createValueEncoded(encoder);
		
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
	
	public User getUser() {
		return user;
	}

	public void setRequestNum(Integer requestNum) {
		this.requestNum = requestNum;
	}
	
	private void setUser(User user) {
		this.user = user;
	}
	
	public String requestShortUrlWithOriginalUrl(String originalUrl) {
		if(!this.origianlUrl.equals(originalUrl))
			throw new IllegalArgumentException(
					ORIGiNAL_URL_DOES_NOT_MATCH.getMessage());
		requestNum++;
		return valueCompressed;
	}
	
	public void createValueEncoded(Encoder<Long, String> encoder) {
		
		if(id==null) {
			throw new IllegalStateException(NO_ID_ASSIGNED.getMessage());}
		
		this.valueCompressed = encoder.encoding(this.id);
	}
	
	@Override
	public String toString() {
		return "UrlPack [id=" + id + ", originalUrl=" + origianlUrl 
				+ ", valueEncoded=" + valueCompressed + "]";
	}

}
