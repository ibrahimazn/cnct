package com.assistanz.gatekeeper.service;

import org.springframework.stereotype.Service;

import com.assistanz.gatekeeper.model.LdapUser;
import com.assistanz.gatekeeper.model.User;

/** Service Interface for the Ldap. */
@Service
public interface LdapService {
	
	/**
	 * Save the ldap user.
	 * 
	 * @param user
	 */
	void saveUser(User user);
	
	/**
	 * Update the ldap user.
	 * 
	 * @param user
	 */
	void updateUser(LdapUser user);
	
	/**
	 * Delete the ldap user.
	 * @param user
	 */
	void deleteUser(User user);

}
