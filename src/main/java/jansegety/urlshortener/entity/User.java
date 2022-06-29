package jansegety.urlshortener.entity;

import static jansegety.urlshortener.error.message.SimpleEntityMessage.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "user")
@Entity
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
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
