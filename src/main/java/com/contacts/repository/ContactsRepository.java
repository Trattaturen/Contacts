package com.contacts.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.contacts.entity.Contacts;

@RepositoryRestResource
public interface ContactsRepository extends CrudRepository<Contacts, Long> {
	@Transactional
	int deleteByName(String name);

	List<Contacts> findByName(String name);

}