package com.contacts.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.contacts.model.Contact;

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
		String sql = "select id, last_name from contacts ";
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
			
			contactList.add(contact);
		}
		return contactList;
	}

	@Override
	public long updateContact(Contact contact) {
		long recordsUpdated = 0;
		
		String sql = "update contacts set last_name = ? where id = ?";
		
		Object[] params = {
			contact.getLastName(),
			contact.getId()
		};
		recordsUpdated = this.jdbcTemplate.update(sql, params);
		return recordsUpdated;
	}

	@Override
	public long insertContact(Contact contact) {
		long recordsInserted = 0;
		String sql = "insert into contacts (id, last_name) values (?, ?)";
				
		Object[] params = {
			this.getNextId("contacts", "id"),
			contact.getLastName()	
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
}
