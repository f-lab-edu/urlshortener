package jansegety.urlshortener.entity;

import static jansegety.urlshortener.error.message.SimpleEntityMessage.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "user")
@Entity
public class User {
	
	@Id @GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@OneToMany(mappedBy ="user")
	List<UrlPack> urlPackList = new ArrayList<>();
	
	@OneToMany(mappedBy ="user")
	List<ClientApplication> clientApplicationList = new ArrayList<>();
	
	public void setId(Long id) {
		if(this.id != null) {
			throw new IllegalStateException(
					ID_HAS_ALREADY_BEEN_ASSIGNED.getMessage()); }
		this.id = id;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUrlPackList(List<UrlPack> urlPackList) {
		this.urlPackList = urlPackList;
	}
	
	public void setClientApplicationList(
			List<ClientApplication> clientApplicationList) {
		this.clientApplicationList = clientApplicationList;
	}

	public List<UrlPack> getUrlPackList() {
		return Collections.unmodifiableList(urlPackList);
	}

	public List<ClientApplication> getClientApplicationList() {
		return Collections.unmodifiableList(clientApplicationList);
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
	    result = 31 * result + id.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		User user = (User)obj;
		return this.id == user.id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email 
				+ ", password=" + password + "]";
	}
	
}
