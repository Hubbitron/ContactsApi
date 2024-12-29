package com.contacts.service;

import java.sql.Blob;
import java.util.List;

import com.contacts.model.Contact;
import com.contacts.model.State;
import com.contacts.model.UserAccount;

public interface ContactsService {
	List<Contact> getAllContacts();
	
	Contact getOneContact(long id);
	
	long updateContact(Contact contact);
	
	long insertContact(Contact contact);
	
	long deleteContact(long id);
	
	Blob downloadProfilePic(long id);
	
	List<State> getStates();
	
	UserAccount getUser(String username);
	
	
	
}
