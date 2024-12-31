package com.contacts.model;

import java.sql.Blob;

import java.util.Date;

import javax.sql.rowset.serial.SerialBlob;

public class Contact {

	long id;
	String lastName;
	String firstName;
	String middleName;
	Date dob;
	String addressLine1;
	long stateId;
	SerialBlob profilePic;
	Blob tempBlob; // temporary field for downloads

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public SerialBlob getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(SerialBlob profilePic) {
		this.profilePic = profilePic;
	}

	public Blob getTempBlob() {
		return tempBlob;
	}

	public void setTempBlob(Blob tempBlob) {
		this.tempBlob = tempBlob;
	}
}
