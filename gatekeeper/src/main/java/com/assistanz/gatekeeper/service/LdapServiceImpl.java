/**
 *
 */
package com.assistanz.gatekeeper.service;

import javax.naming.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import com.assistanz.gatekeeper.model.LdapUser;
import com.assistanz.gatekeeper.model.User;
import com.assistanz.gatekeeper.model.LdapUser.Status;

/**
 * @author azn0084
 *
 */
@Service
public class LdapServiceImpl implements LdapService {

	@Autowired
	private LdapTemplate ldapTemplate;

	/** User account value is append. */
    @Value(value = "${user.account}")
    private String account;

    private LdapUser getLdapUserByUser(User user) {
      LdapUser ldapUser = new LdapUser();
      ldapUser.setAccount(account);
      ldapUser.setEmail(user.getEmail());
      ldapUser.setDescription(user.getFirstName());
      ldapUser.setUserName(user.getUserName());

      ldapUser.setFirstName(user.getFirstName());
      if (user.getLastName() != null && user.getLastName() != "") {
        ldapUser.setLastName(user.getLastName());
        ldapUser.setFullName(user.getFirstName()+""+user.getLastName());
      } else {
        ldapUser.setLastName(" ");
        ldapUser.setFullName(user.getFirstName()+""+user.getLastName());
      }
      ldapUser.setLanguage("EN");
      ldapUser.setMail(user.getEmail());
      ldapUser.setType(ldapUser.getType().values()[user.getUserType().ordinal()]);
      ldapUser.setStatus(Status.ACTIVE);
      ldapUser.setUserPassword(user.getPassword());
      ldapUser.setDn(buildUserDn(ldapUser));
      return ldapUser;
    }

	@Override
	public void saveUser(User user) {
	    LdapUser ldapUser =getLdapUserByUser(user);
		ldapTemplate.create(ldapUser);
	}

	@Override
	public void updateUser(LdapUser user) {
		ldapTemplate.update(user);
	}

	@Override
	public void deleteUser(User user) {
	    LdapUser ldapUser =getLdapUserByUser(user);
	    ldapTemplate.delete(ldapUser);
	}

	private Name buildUserDn(LdapUser user) {
		return LdapNameBuilder.newInstance().add("ou", user.getAccount()).add("uid", user.getUserName()).build();
	}

}
