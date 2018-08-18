/**
 * 
 */
package com.assistanz.gatekeeper.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Service;

import com.assistanz.gatekeeper.model.Permission;
import com.assistanz.gatekeeper.model.Role;
import com.assistanz.gatekeeper.model.User;
import com.assistanz.gatekeeper.service.UserService;

@Service("ldaploginService")
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	private UserService userService;

	/**
	 * @param userService
	 */
	public CustomLdapAuthoritiesPopulator(UserService userService) {
		this.userService = userService;
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,
			String username) {
		Collection<GrantedAuthority> gas = new HashSet<GrantedAuthority>();

		User user = userService.findByUsername(username);
		List<Role> roles = user.getRoles();
		for (Role role : roles) {
			List<Permission> permissions = role.getPermission();
			for (Permission permission : permissions) {
				gas.add(new SimpleGrantedAuthority(permission.getPermission()));
			}
		}
		gas.add(new SimpleGrantedAuthority("user"));
		return gas;
	}
	
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal, authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }
}