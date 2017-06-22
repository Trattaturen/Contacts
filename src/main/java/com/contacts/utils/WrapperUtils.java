package com.contacts.utils;

import java.util.Arrays;

import com.contacts.entity.Contacts;
import com.contacts.entity.ContactsWrapper;

public class WrapperUtils {

	public static ContactsWrapper wrap(Contacts contacts){
		
		ContactsWrapper wrappedContacts = new ContactsWrapper(contacts.getName(), Arrays.asList(contacts.getPhones().split(",")));
		return wrappedContacts;
		
	}
	
	public static Contacts unWrap(ContactsWrapper contactsWrapper){
		
		String phoneNumbers = String.join(",", contactsWrapper.getPhones());   
		Contacts unWrappedContacts = new Contacts(contactsWrapper.getName(), phoneNumbers);
		return unWrappedContacts;
		
	}
	
}
