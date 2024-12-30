package com.contacts.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.contacts.model.UserAccount;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	ContactsService contactsService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = this.contactsService.getUser(username);
		
		if(userAccount == null) {
			throw new UsernameNotFoundException(username);
		}

		List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>();
		
		authList.add(new SimpleGrantedAuthority(userAccount.getRoleId() + StringUtils.EMPTY));
		
		String encodedPassword = userAccount.getPassword();
		
		User user = new User(username, encodedPassword, authList);
		
		return user;
	}
}
