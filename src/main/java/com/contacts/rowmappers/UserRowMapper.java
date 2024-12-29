package com.contacts.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.contacts.model.Contact;
import com.contacts.model.UserAccount;

public class UserRowMapper implements RowMapper<UserAccount> {

	@Override
	public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserAccount userAccount = new UserAccount();
		userAccount.setId(rs.getLong("id"));
		userAccount.setUsername(rs.getString("username"));
		userAccount.setPassword(rs.getString("password"));
		userAccount.setRoleId(rs.getLong("role_id"));
		userAccount.setFirstName(rs.getString("first_name"));
		userAccount.setLastName(rs.getString("last_name"));
		
		return userAccount;
	}
}
