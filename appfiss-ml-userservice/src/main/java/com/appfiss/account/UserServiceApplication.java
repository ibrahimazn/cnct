/*
 *
 */
package com.appfiss.account;

import java.security.Principal;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.github.mthizo247.cloud.netflix.zuul.web.socket.EnableZuulWebSocket;

/**
 * Spring boot application configuration and bean initialization for add on
 * services of spring boot .
 */
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
@EnableZuulProxy
@EnableZuulWebSocket
public class UserServiceApplication extends WebMvcConfigurerAdapter {

	/** User profile image directory. */
	@Value("${profile.image.dir}")
	private static String backgroundLogoDir;

	/** File import temporaury directory. */
	@Value("${fileimport.temp.dir}")
	private static String importTempDir;

	private static final String[] RESOURCE_LOCATIONS = { "file:" + backgroundLogoDir, "file:" + importTempDir, };

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	/**
	 * Currenly logged in user information.
	 *
	 * @param user
	 *            the user
	 * @return the map
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Map<String, String> user(Principal user) {
		return Collections.singletonMap("message", "user is: " + user.toString());
	}

	/*
	 * Add resource handler to access external location.
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
	 * #addResourceHandlers(org.springframework.web.servlet.config.annotation.
	 * ResourceHandlerRegistry)
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (backgroundLogoDir != null) {
			registry.addResourceHandler("/resources/**").addResourceLocations("file:" + backgroundLogoDir,
					"file:" + importTempDir);
		}
	}

	/**
	 * Internationalization messages configuration.
	 *
	 * @return MessageSource
	 */
	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(1000);
		return messageSource;
	}

	/**
	 * Configuring default locale resolver.
	 *
	 * @return LocaleResolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.ENGLISH);
		return slr;
	}

	/**
	 * Locale change interceptor configuration.
	 *
	 * @return LocaleChangeInterceptor
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	/**
	 * Locale change interceptor configuration.
	 *
	 * @param registry
	 *            inceptors
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	/**
	 * Instantiates validator bean for javax.validation.Validator interface.
	 *
	 * @return Validator implementation
	 */
	@Bean
	public javax.validation.Validator localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}

	/**
	 * Create bean for rest template .
	 *
	 * @return the rest template
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
