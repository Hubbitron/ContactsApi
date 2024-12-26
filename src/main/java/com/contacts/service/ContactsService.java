package com.contacts.service;

import java.util.List;

import com.contacts.model.Contact;

public interface ContactsService {
	List<Contact> getAllContacts();
	
	Contact getOneContact(long id);
	
	long updateContact(Contact contact);
	
	long insertContact(Contact contact);
	
	long deleteContact(long id);
}
