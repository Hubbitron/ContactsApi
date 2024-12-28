package com.contacts;

import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.contacts.model.Contact;
import com.contacts.model.State;
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
		System.out.println(contact.getFirstName());
	}
	
	@Test
	public void getContacts() {
		List<Contact> contactList = this.contactsService.getAllContacts();
		for (Contact contact : contactList) {
			System.out.println(contact.getFirstName());
		}
	}
	
	@Test
	void updateContact() {
		Contact contact = new Contact();
		contact.setId(10);
		System.out.println(contact.getFirstName());	}
	
	@Test
	void insertContact() {
		Contact contact = new Contact();
		contact.setLastName("Test");
		System.out.println(this.contactsService.insertContact(contact));
	}
	
	@Test void deleteContact() {
		System.out.println(this.contactsService.deleteContact(6));
	}
	
	@Test
	public void getProfilePic() {
		Blob pfp = this.contactsService.downloadProfilePic(1);
		System.out.println(pfp);
		
	}
	
	@Test
	public void getStates() {
		List<State> stateList = this.contactsService.getStates();
		for (State state : stateList) {
			System.out.println(state.getStateAbbr());
		
	}
	}
}
