package com.assistanz.gatekeeper.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.assistanz.gatekeeper.service.UserService;


/**
 * Spring Security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /** A service that provides the details about an OAuth2 client. */
    @Autowired
    private ClientDetailsService clientDetailsService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    @Qualifier("ldaploginService")
    LdapAuthoritiesPopulator ldapAuthoritiesPopulator;

    /** It uses the BCrypt strong hashing function. */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /** Database Datasource. */
    @Autowired
    private DataSource dataSource;
    
    /** Database Datasource. */
    @Autowired
    private LdapContextSource ldapContext;

    /** User details for the authorization Server. */
    @Value("${spring.queries.users-query}")
    private String usersQuery;

    /** Role details. */
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    /** Permission Group Details. */
    @Value("${spring.queries.group-query}")
    private String groupQuery;
    
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

    @Autowired
    private Environment environment;
    
    /** LDAP Dn Patterns. */
    @Value(value = "${ldap.dnPatterns}")
    private String dnPatterns;
    
    /** LDAP Group Saerch Base. */
    @Value(value = "${ldap.groupSearchBase}")
    private String groupSearchBase;
    
    /** LDAP User Saerch Base. */
    @Value(value = "${ldap.userSearchBase}")
    private String userSearchBase;
    
    /** LDAP URL. */
    @Value(value = "${ldap.url}")
    private String url;
    
    /** LDAP UserDnDc. */
    @Value(value = "${ldap.userDnDc}")
    private String userDnDc;
    
    /** LDAP DnPatternUid. */
    @Value(value = "${ldap.dnPatternUid}")
    private String dnPatternUid;
    
    /**
     * Method to Collect the User details for Authentication.
     *
     * @param auth authentication builder
     * @throws Exception if error occurs
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {

        auth
		.ldapAuthentication()
			.userDnPatterns(dnPatterns)
			.groupSearchBase(groupSearchBase)
			.contextSource()
				.url(url+userDnDc)
				.and()
			.passwordCompare()
				.passwordAttribute("userPassword")
				.and()
				.userSearchBase(userSearchBase)
				.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/oauth/authorize", "/oauth/token").permitAll();
        http.authorizeRequests().and().csrf().disable()
        .httpBasic().disable()
        .anonymous().disable()
        .formLogin()
         .loginPage("/api/user/login").failureUrl("/api/user/login?error=true")
        .defaultSuccessUrl("/api/user/admin/home")
        .usernameParameter("email")
        .passwordParameter("password")
        .and().logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/api/user/login")
        .and().exceptionHandling()
        .accessDeniedPage("/access-denied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
           .ignoring()
           .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public FilterBasedLdapUserSearch userSearch() {
        return new FilterBasedLdapUserSearch(userSearchBase, dnPatternUid,  ldapContext);
    }
    
    @Bean
    public LdapUserDetailsService myUserDetailsService() throws Exception {
        return new LdapUserDetailsService(userSearch(), ldapAuthoritiesPopulator);
    }

    /**
     * JDBC Token Store.
     *
     * @return token
     */
//    @Bean
//    public JdbcTokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }

    /**
     * Authorization Services.
     *
     * @return authentication
     */
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * Token Store Handler.
     *
     * @param tokenStore token store
     * @return handler
     */
    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

    /**
     * Token Approval Store.
     *
     * @param tokenStore token store
     * @return token store
     * @throws Exception if error occurs
     */
    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }
    
    @Bean
    @Autowired
	public CustomLdapAuthoritiesPopulator customLdapAuthorities(UserService userService) throws Exception {
    	return new CustomLdapAuthoritiesPopulator(userService);
    }


}

