package com.contacts.restcontrollers;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

import com.contacts.model.AuthenticationResponse;
import com.contacts.helper.JwtUtil;
import com.contacts.model.AuthenticationRequest;
import com.contacts.model.Contact;
import com.contacts.model.State;
import com.contacts.service.ContactsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")

public class ContactsWebServices {
	
	@Autowired
	protected AuthenticationManager authenticationManager;
	
	@Autowired
	protected UserDetailsService userDetailsService;
	
	@Autowired
	protected JwtUtil jwtUtil;
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}
		catch (AuthenticationException ae) {
			return ResponseEntity.ok(new AuthenticationResponse(StringUtils.EMPTY));
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			
		String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@Autowired
	ContactsService contactsService;
	
	@GetMapping(value = "/getProfilePic/{id}")
	public ResponseEntity<Resource> getProfilePic(@PathVariable Long id) throws SQLException {
		Blob profilePic = this.contactsService.downloadProfilePic(id);
		byte[] profilePicBytes = profilePic.getBytes(1, (int)profilePic.length());
		profilePic.free();
		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(profilePicBytes));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_MIXED);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		headers.add("Content-Disposition", "inline; filename = download.png");
		return ResponseEntity.ok().headers(headers).contentLength(profilePicBytes.length).body(resource);
	}
	
	@GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Contact>> getContacts() {
		List<Contact> arrayList = this.contactsService.getAllContacts();
		return new ResponseEntity<List<Contact>>(arrayList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getStates", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<State>> getStates() {
		List<State> arrayList = this.contactsService.getStates();
		return new ResponseEntity<List<State>>(arrayList, HttpStatus.OK);
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
	
	@PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> insert(
			@RequestParam( value = "file" ) Optional<MultipartFile> file,
			@RequestParam String json) throws JsonParseException, JsonMappingException, IOException, SerialException, SQLException {
		
		Contact contact = new ObjectMapper().readValue(json, Contact.class);
		
		MultipartFile multipartFile = null;
		
		if (file.isPresent()) {
			multipartFile = file.get();
			
			byte[] profilePicBytes = multipartFile.getBytes();
			SerialBlob profilePicBlob = new SerialBlob(profilePicBytes);	
			
			contact.setProfilePic(profilePicBlob);
		}
		
		long recordsInserted = this.contactsService.insertContact(contact);
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Contact> update (
		@RequestParam( value = "file" ) Optional<MultipartFile> file,
		@RequestParam String json) throws JsonParseException, JsonMappingException, IOException, SQLException {
		
		Contact contact = new ObjectMapper().readValue(json, Contact.class);
		
		MultipartFile multipartFile = null;
		
		if (file.isPresent()) {
			multipartFile = file.get();
			
			byte[] profilePicBytes = multipartFile.getBytes();
			SerialBlob profilePicBlob = new SerialBlob(profilePicBytes);	
			
			contact.setProfilePic(profilePicBlob);
		}
			

		
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