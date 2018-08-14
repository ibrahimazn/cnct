package az.ldap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.repository.config.EnableLdapRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.RestTemplateCallImpl;

/**
 * Spring boot application configuration class.
 */
@EnableMongoRepositories("az.ldap.zabbix")
@EnableLdapRepositories("az.ldap.domain")
@SpringBootApplication(exclude={ErrorMvcAutoConfiguration.class, DataSourceAutoConfiguration.class })
@EnableWebMvc
@ComponentScan
@Configuration
@EnableMongoAuditing
@EnableScheduling
public class ApplicationConfig extends WebMvcConfigurerAdapter {

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
	 * @param args
	 *            to pass from command line
	 * @throws Exception
	 *             if any error
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApplicationConfig.class, args);
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
	
	   /**
     * Connector processor method.
     *
     * @return ConnectionProcessor
     */
    @Bean
    public RestTemplateCall<?,?> connectionProcessor() {
        return new RestTemplateCallImpl<>();
    }
	
	@Bean
	public LdapTemplate ldapTemplate() throws Exception{
		LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
		ldapTemplate.setIgnorePartialResultException(true);
		ldapTemplate.setContextSource(getContextSource());
		return ldapTemplate;
	}
}