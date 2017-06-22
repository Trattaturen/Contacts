package com.contacts.service;

import org.springframework.stereotype.Component;

import com.contacts.entity.Contacts;

@Component
public interface ContactsService {

	public Contacts addContacts(Contacts contactInfo);

	public Contacts updateContacts(Contacts contactInfo);

	public Contacts findByName(String username);
	
	public int deleteByUsername(String username);
	
}
