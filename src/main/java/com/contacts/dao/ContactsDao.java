package com.contacts.dao;

import java.sql.Blob;
import java.util.List;

import com.contacts.model.Contact;
import com.contacts.model.State;

public interface ContactsDao {
	List<Contact> getContacts(long id);
	
	long updateContact(Contact contact);
	
	long insertContact(Contact contact);
	
	long deleteContact(long id);
	
	Blob downloadProfilePic(long id);
	
	List<State> getStates();

}
