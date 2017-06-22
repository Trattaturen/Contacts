package com.contacts.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.contacts.entity.Contacts;
import com.contacts.repository.ContactsRepository;
import com.contacts.service.ContactsService;

@Component
public class ContactsServiceImpl implements ContactsService {

	@Autowired
	private ContactsRepository contactsRepository;

	@Override
	public Contacts addContacts(Contacts contacts) {

		List<String> phones = Arrays.asList(contacts.getPhones().split(","));
		for (String currentPhone : phones) {
			if (contactsRepository.save(new Contacts(contacts.getName(), currentPhone)) == null) {
				return null;
			}
		}
		return contacts;
	}

	@Override
	public Contacts updateContacts(Contacts contacts) {

		Stack<Contacts> existingContactLines = getContactLinesAsStack(contacts);

		List<String> updatedPhones = Arrays.asList(contacts.getPhones().split(","));

		return updateContactInfoLines(contacts, existingContactLines, updatedPhones);

	}

	@Override
	public Contacts findByName(String name) {
		List<Contacts> foundContactLines = contactsRepository.findByName(name);
		if (foundContactLines.isEmpty()) {
			return null;
		}
		StringBuffer phones = getAllContactPhones(foundContactLines);
		return createContactFromLines(name, phones);
	}

	@Override
	public int deleteByUsername(String username) {
		return contactsRepository.deleteByName(username);
	}

	private Stack<Contacts> getContactLinesAsStack(Contacts contacts) {
		Stack<Contacts> existingContactLines = new Stack<Contacts>();
		existingContactLines.addAll(contactsRepository.findByName(contacts.getName()));
		Collections.reverse(existingContactLines);
		return existingContactLines;
	}

	private Contacts updateContactInfoLines(Contacts contacts, Stack<Contacts> existingContactLines,
			List<String> updatedPhones) {
		for (String currentUpdatedPhone : updatedPhones) {
			Contacts contactLineToAdd = new Contacts(contacts.getName(), currentUpdatedPhone);
			if (!existingContactLines.empty()) {
				contactLineToAdd.setId(existingContactLines.pop().getId());
			}
			if (contactsRepository.save(contactLineToAdd) == null) {
				return null;
			}
		}
		while (!existingContactLines.empty()) {
			contactsRepository.delete(existingContactLines.pop());
		}
		return contacts;
	}

	private Contacts createContactFromLines(String name, StringBuffer phones) {
		Contacts contacts = new Contacts();
		contacts.setName(name);
		contacts.setPhones(String.valueOf(phones));
		return contacts;
	}

	private StringBuffer getAllContactPhones(List<Contacts> foundContactLines) {
		StringBuffer phones = new StringBuffer();
		for (Contacts currentLine : foundContactLines) {
			phones.append(currentLine.getPhones() + ",");

		}
		return phones;
	}

}
