package com.assistanz.gatekeeper;

import java.security.Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Oauth2 Authorization Server.
 */
@SpringBootApplication
@RestController
@EnableResourceServer
@ComponentScan("com.assistanz.gatekeeper")
public class GatekeeperApplication  extends SpringBootServletInitializer {

    /** Logger for GateKeeper Application. */
    private static final Log logger = LogFactory.getLog(GatekeeperApplication.class);
    
    /** The Ldap host and port. */
	@Value(value = "${ldap.url}")
	private String ldapUrl;

	/** The Ldap user dn. */
	@Value(value = "${ldap.userDn}")
	private String ldapUser;
	
	/** The Ldap user password. */
	@Value(value = "${ldap.password}")
	private String ldapPassword;
	
	/** The Ldap Base Dn. */
	@Value(value = "${ldap.base}")
	private String ldapBase;

    /**
     * Default spring boot main method.
     *
     * @param args to pass from command line
     * @throws Exception if any error
     */
    public static void main(String[] args) {
        SpringApplication.run(GatekeeperApplication.class, args);
    }
    
    @Bean
	public LdapContextSource getContextSource() throws Exception{
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl(ldapUrl);
		ldapContextSource.setBase(ldapBase);
		ldapContextSource.setUserDn(ldapUser);
		ldapContextSource.setPassword(ldapPassword);
		return ldapContextSource;
	}
    
    @Bean
	public LdapTemplate ldapTemplate() throws Exception{
		LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
		ldapTemplate.setIgnorePartialResultException(true);
		ldapTemplate.setContextSource(getContextSource());
		return ldapTemplate;
	}
    
    

    /**
     * User Principal.
     *
     * @param user user details
     * @return user
     */
    @RequestMapping("/user")
    public Principal user(Principal user) {
       logger.info("AS /user has been called");
       logger.debug("user info: " + user);
       return user;
    }



}
