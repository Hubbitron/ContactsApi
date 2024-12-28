package com.contacts.dao;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.contacts.model.Contact;
import com.contacts.model.State;

@Repository
public class ContactsDaoImpl implements ContactsDao {
	
	@Autowired 
	JdbcTemplate jdbcTemplate;
	
	@Autowired 
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<Contact> getContacts(long id) {
		List<Contact> contactList = new ArrayList<Contact>();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();		
		String sql = "select id, last_name, middle_name, first_name, dob, state_id from contacts ";
		if (id > 0) {
			sql += "where id = :id";
			paramMap.put("id", id);
		}
		sql += " order by last_name asc";
		
		List<Map<String, Object>> rows = this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
		
		for (Map<String, Object> row: rows) {
			Contact contact = new Contact();
			
			contact.setId(Long.parseLong(row.get("id").toString()));
			contact.setLastName(row.get("last_name").toString());
			contact.setMiddleName(row.get("middle_name").toString());
			contact.setFirstName(row.get("first_name").toString());
			contact.setDob((Date)row.get("dob"));
			contact.setStateId(Long.parseLong(row.get("state_id").toString()));
			
			contactList.add(contact);
		}
		return contactList;
	}

	@Override
	public long updateContact(Contact contact) {
		long recordsUpdated = 0;
		
		String sql = "update contacts "
				+ " set last_name = ?,"
				+ " middle_name = ?,"
				+ " first_name = ?,"
				+ " dob = ?, "
				+ " state_id = ?, "
				+ " profile_pic = ?"
				+ " where id = ?";
		
		Object[] params = {
			contact.getLastName(),
			contact.getMiddleName(),
			contact.getFirstName(),
			contact.getDob(),
			contact.getStateId(),
			contact.getProfilePic(),
			contact.getId()
		};
		recordsUpdated = this.jdbcTemplate.update(sql, params);
		return recordsUpdated;
	}

	@Override
	public long insertContact(Contact contact) {
		long recordsInserted = 0;
		String sql = "insert into contacts (id,"
				+ " last_name,"
				+ " middle_name,"
				+ " first_name,"
				+ " dob,"
				+ " state_id,"
				+ " profile_pic = ? "
				+ " values (?, ?, ?, ?, ?, ?, ?))";
				
		Object[] params = {
			this.getNextId("contacts", "id"),
			contact.getLastName(),
			contact.getMiddleName(),
			contact.getFirstName(),
			contact.getDob(),
			contact.getStateId(),
			contact.getProfilePic()
		};
		recordsInserted = this.jdbcTemplate.update(sql, params);
		return recordsInserted;
	}

	private long getNextId(String tableName, String primaryKeyName) {
		String sql = "select max(" + primaryKeyName + ") id from " + tableName;
		Long id = this.jdbcTemplate.queryForObject(sql, Long.class);
		long nextId = id == null ? 1 : id.longValue() + 1;
		return nextId;
	}

	@Override
	public long deleteContact(long id) {
		long recordsDeleted = 0;
		String sql = "delete from contacts where id = ?";
		Object[] params = {
			id
		};
		recordsDeleted = this.jdbcTemplate.update(sql, params);
		return recordsDeleted;
	}
	
	
	private Contact getBlobFromContact(ResultSet resultSet) throws SQLException {
		Contact contact = new Contact();
		Blob profilepic = resultSet.getBlob("profile_pic");
		contact.setTempBlob(profilepic);
		return contact;
		
	}
	
	
	@Override
	public Blob downloadProfilePic(long id) {
		String sql = "select profile_pic from contacts where id = ?";
		List<Contact> contactList = this.jdbcTemplate.query(sql,
				(resultSet, i) -> this.getBlobFromContact(resultSet),
				id);
				
		if (contactList.size() == 0) {
			return null;
		}
		
		Contact firstContact = contactList.get(0);
		Blob pfp = firstContact.getTempBlob();
		return pfp;
	}

	@Override
	public List<State> getStates() {
		List<State> stateList = new ArrayList<State>();
			
		String sql = "select id, state_abbr, state_name from state ";
		
		sql += "order by state_name asc";
		
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
		
		for (Map<String, Object> row: rows) {
			State state = new State();
			
			state.setId(Long.parseLong(row.get("id").toString()));
			state.setStateAbbr(row.get("state_abbr").toString());
			state.setStateName(row.get("state_name").toString());
;
			
			stateList.add(state);
		}
		return stateList;
	}
}
