package com.contacts.service;

import java.sql.Blob;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contacts.dao.ContactsDao;
import com.contacts.model.Contact;
import com.contacts.model.State;
import com.contacts.model.UserAccount;

@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {
	
	@Autowired
	ContactsDao contactsDao;

	@Override
	public List<Contact> getAllContacts() {
		return this.contactsDao.getContacts(0);
	}

	@Override
	public Contact getOneContact(long id) {
		List<Contact> contactList = this.contactsDao.getContacts(id);
		Contact contact = contactList.get(0);
		return contact;
	}

	@Override
	public long updateContact(Contact contact) {
		return this.contactsDao.updateContact(contact);
	}

	@Override
	public long insertContact(Contact contact) {
		return this.contactsDao.insertContact(contact);
	}

	@Override
	public long deleteContact(long id) {
		return this.contactsDao.deleteContact(id);
	}

	@Override
	public Blob downloadProfilePic(long id) {
		return this.contactsDao.downloadProfilePic(id);
	}

	@Override
	public List<State> getStates() {
		return this.contactsDao.getStates();
	}

	@Override
	public UserAccount getUser(String username) {
		return this.contactsDao.getUser(username);
	}
}
