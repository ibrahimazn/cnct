package com.assistanz.gatekeeper.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Oauth Authorization configuration.
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

	/** Persistence interface for OAuth2 tokens. */
	@Autowired
	private TokenStore tokenStore;

	/**
	 * Basic interface for determining whether a given client authentication
	 * request has been approved by the current user.
	 */
	@Autowired
	private UserApprovalHandler userApprovalHandler;

	/** Processes an Authentication request. */
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	/** Processes an Authentication request. */
	@Autowired
	private LdapUserDetailsService myUserDetailsService;

	/** Database Datasource. */
	@Autowired
	private DataSource dataSource;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource);
	}

	// @Override
	// public void configure(AuthorizationServerEndpointsConfigurer endpoints)
	// throws Exception {
	// endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
	// .authenticationManager(authenticationManager);
	// }

	@Autowired
	private Environment environment;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.addInterceptor(new HandlerInterceptorAdapter() {
			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) throws Exception {
				if (modelAndView != null && modelAndView.getView() instanceof RedirectView) {
					RedirectView redirect = (RedirectView) modelAndView.getView();
					String url = redirect.getUrl();
					if (url.contains("code=") || url.contains("error=")) {
						HttpSession session = request.getSession(false);
						if (session != null) {
							session.invalidate();
						}
					}
				}
			}
		});
		endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtTokenEnhancer())
				.authenticationManager(authenticationManager).userDetailsService(myUserDetailsService);
	}

	// @Override
	// public void configure(AuthorizationServerEndpointsConfigurer endpoints)
	// throws Exception {
	// endpoints.addInterceptor(new HandlerInterceptorAdapter() {
	// @Override
	// public void postHandle(HttpServletRequest request,
	// HttpServletResponse response, Object handler,
	// ModelAndView modelAndView) throws Exception {
	// if (modelAndView != null
	// && modelAndView.getView() instanceof RedirectView) {
	// RedirectView redirect = (RedirectView) modelAndView.getView();
	// String url = redirect.getUrl();
	// if (url.contains("code=") || url.contains("error=")) {
	// HttpSession session = request.getSession(false);
	// if (session != null) {
	// session.invalidate();
	// }
	// }
	// }
	// }
	// });
	// endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
	// .authenticationManager(authenticationManager);
	// }

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");

	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtTokenEnhancer());
	}

	@Bean
	public JwtAccessTokenConverter jwtTokenEnhancer() {
		String pwd = "mySecretKey";
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"),
				pwd.toCharArray());
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
		return converter;
	}

}
