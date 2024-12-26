package com.contacts.dao;

import java.util.List;

import com.contacts.model.Contact;

public interface ContactsDao {
	List<Contact> getContacts(long id);
	
	long updateContact(Contact contact);
	
	long insertContact(Contact contact);
	
	long deleteContact(long id);

}
