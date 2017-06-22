package com.contacts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.contacts.entity.Contacts;
import com.contacts.entity.ContactsWrapper;
import com.contacts.service.impl.ContactsServiceImpl;
import com.contacts.utils.WrapperUtils;

@RestController
public class ContactsController {

	@Autowired
	ContactsServiceImpl contactsService;

	@RequestMapping(value = "/contacts/{name}", method = RequestMethod.GET)
	public ResponseEntity<ContactsWrapper> getContacts(@PathVariable String name) {
		Contacts contacts = contactsService.findByName(name);
		if (contacts == null) {
			return new ResponseEntity<ContactsWrapper>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ContactsWrapper>(WrapperUtils.wrap(contacts), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/contacts", method = RequestMethod.POST)
	public ResponseEntity<ContactsWrapper> addContacts(@RequestBody ContactsWrapper contactsWrapper ) {
		if(!isValidContactsWrapper(contactsWrapper)){
			return new ResponseEntity<ContactsWrapper>(HttpStatus.BAD_REQUEST);
		}
		if (contactsService.findByName(contactsWrapper.getName()) != null) {
			return new ResponseEntity<ContactsWrapper>(HttpStatus.CONFLICT);
		}

		Contacts contacts = contactsService.addContacts(WrapperUtils.unWrap(contactsWrapper));
		return new ResponseEntity<ContactsWrapper>(WrapperUtils.wrap(contacts), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/contacts/{name}", method = RequestMethod.PUT)
	public ResponseEntity<ContactsWrapper> updateContacts(@PathVariable String name,
			@RequestBody ContactsWrapper contactsWrapper) {
		if(!isValidContactsWrapper(contactsWrapper)){
			return new ResponseEntity<ContactsWrapper>(HttpStatus.BAD_REQUEST);
		}
		Contacts currentContacts = contactsService.findByName(name);

		if (currentContacts == null) {
			return new ResponseEntity<ContactsWrapper>(HttpStatus.NOT_FOUND);
		}

		Contacts updatedContacts = WrapperUtils.unWrap(contactsWrapper);
		currentContacts.setPhones(updatedContacts.getPhones());

		if (contactsService.updateContacts(currentContacts) != null) {
			return new ResponseEntity<ContactsWrapper>(WrapperUtils.wrap(currentContacts), HttpStatus.OK);
		}
		return new ResponseEntity<ContactsWrapper>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/contacts/{name}")
	public ResponseEntity<Contacts> deleteContacts(@PathVariable String name) {
		Contacts contacts = contactsService.findByName(name);
		if (contacts == null) {
			return new ResponseEntity<Contacts>(HttpStatus.NOT_FOUND);
		}
		contactsService.deleteByUsername(name);
		return new ResponseEntity<Contacts>(HttpStatus.OK);
	}
	
	private boolean isValidContactsWrapper(ContactsWrapper contactsWrapper){
		return contactsWrapper.getName()!=null && contactsWrapper.getPhones()!= null;
	}
}
