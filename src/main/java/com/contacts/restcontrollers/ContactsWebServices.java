package com.contacts.restcontrollers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contacts.model.Contact;
import com.contacts.service.ContactsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping(value = "api")

public class ContactsWebServices {
	
	@Autowired
	ContactsService contactsService;
	
	@GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Contact>> get() {
		List<Contact> arrayList = this.contactsService.getAllContacts();
		return new ResponseEntity<List<Contact>>(arrayList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getSingle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Contact> getOne(@PathVariable String id){
		if(id.length() == 0) {
			return new ResponseEntity<Contact>(HttpStatus.NOT_FOUND);
		}
	
		long parsedId = Long.parseLong(id);
		
		Contact contact = this.contactsService.getOneContact(parsedId);
		if (contact == null) {
			return new ResponseEntity<Contact>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Contact>(contact, HttpStatus.OK);
	}
	
	@PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> insert(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		
		Contact contact = new ObjectMapper().readValue(json, Contact.class);
		
		long recordsInserted = this.contactsService.insertContact(contact);
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Contact> update(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		Contact contact = new ObjectMapper().readValue(json, Contact.class);
		
		long recordsUpdated = this.contactsService.updateContact(contact);
		
		return new ResponseEntity<Contact>(contact, HttpStatus.OK);
		
	}
	
	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable String id){
		if(id.length() == 0) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	
		long parsedId = Long.parseLong(id);
		
		long recordsDeleted = this.contactsService.deleteContact(parsedId);
		if (recordsDeleted == 0) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Void>(HttpStatus.OK);	
		
	}
}