package com.contacts;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.contacts.model.Contact;
import com.contacts.service.ContactsService;

@SpringBootTest
class ContactsApiApplicationTests {
	
	@Autowired
	ContactsService contactsService;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	public void getContact() {
		Contact contact = this.contactsService.getOneContact(2);
		System.out.println(contact.getId() + " " + contact.getLastName());
	}
	
	@Test
	public void getContacts() {
		List<Contact> contactList = this.contactsService.getAllContacts();
		for (Contact contact : contactList) {
			System.out.println(contact.getId() + " " + contact.getLastName());
			
		}
	}
	
	@Test
	void updateContact() {
		Contact contact = new Contact();
		contact.setId(2);
		contact.setLastName("Termite");
		System.out.println(this.contactsService.updateContact(contact));
	}
	
	@Test
	void insertContact() {
		Contact contact = new Contact();
		contact.setLastName("Polp");
		System.out.println(this.contactsService.insertContact(contact));
	}
	
	@Test void deleteContact() {
		System.out.println(this.contactsService.deleteContact(6));
	}
}
