package jansegety.urlshortener.repository;

import java.util.Optional;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import jansegety.urlshortener.entity.ClientApplication;

@Mapper
public interface ClientApplicationRepository {
	
	public void save(ClientApplication clientApplication);
	public Optional<ClientApplication> findById(String uuid);
	
}
