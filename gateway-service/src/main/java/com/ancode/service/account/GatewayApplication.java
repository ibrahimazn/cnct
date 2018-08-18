/*
 *
 */
package com.ancode.service.account;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.mthizo247.cloud.netflix.zuul.web.socket.EnableZuulWebSocket;

/**
 * The Class Application.
 */
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
@EnableZuulProxy
@EnableZuulWebSocket
public class GatewayApplication extends WebMvcConfigurerAdapter {

  /** User profile image directory. */
  @Value("${profile.image.dir}")
  private static String backgroundLogoDir;
  
  /** File import temporaury directory. */
  @Value("${fileimport.temp.dir}")
  private static String importTempDir;

  private static final String[] RESOURCE_LOCATIONS = {
		  "file:" + backgroundLogoDir,
		  "file:" + importTempDir,
  };

  /**
   * The main method.
   *
   * @param args
   *          the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

  /**
   * User.
   *
   * @param user
   *          the user
   * @return the map
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public Map<String, String> user(Principal user) {
    return Collections.singletonMap("message", "user is: " + user.toString());
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (backgroundLogoDir != null) {
      registry.addResourceHandler("/resources/**").addResourceLocations("file:" + backgroundLogoDir,"file:" + importTempDir);
    }
  }

  /**
   * Rest template.
   *
   * @return the rest template
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
