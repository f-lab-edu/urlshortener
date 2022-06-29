package jansegety.urlshortener.entity;

import static jansegety.urlshortener.error.message.SimpleEntityMessage.*;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "client_application")
@Entity
public class ClientApplication {
	
	//UUID로 할당된다.
	//TODO DB에서 128bit 크기의 Binary타입으로 바꿀것
	@Id @GeneratedValue
	@Column(name = "id")
	private String id;
	
	@Column(name = "name")
	private String name;
	
	//UUID로 할당된다.
	//TODO DB에서 128bit 크기의 Binary타입으로 바꿀것
	@Column(name = "client_secret")
	private String clientSecret;
	
	@JoinColumn(name = "user_id")
	private Long userId;

	public void setId(UUID id){
		if(this.id != null)
			throw new IllegalStateException(
					ID_HAS_ALREADY_BEEN_ASSIGNED.getMessage());
		
		this.id = id.toString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public boolean equalsClientSecret(UUID clientSecret) {
		return this.clientSecret.equals(clientSecret.toString());
	}
	
	public UUID getId() {
		return UUID.fromString(id);
	}

	public String getName() {
		return name;
	}

	public UUID getClientSecret() {
		return UUID.fromString(clientSecret);
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "ClientApplication [id=" + id + ", name=" + name 
				+ ", clientSecret=" + clientSecret + "]";
	}	

}
