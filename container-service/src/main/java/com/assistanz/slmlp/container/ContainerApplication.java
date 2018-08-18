/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import az.ancode.kubectl.connector.RestTemplateCall;
import az.ancode.kubectl.connector.RestTemplateCallImpl;
import az.openfaas.connector.RestCall;
import az.openfaas.connector.RestCallImpl;

/**
 * The Class Application.
 */
@SpringBootApplication
public class ContainerApplication extends WebMvcConfigurerAdapter {
    /** User profile image directory.*/
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
     * Main Method.
     * 
     * @param args string args.
     */
    public static void main(String[] args) {
        SpringApplication.run(ContainerApplication.class, args);
    }

    /**
     * 
     * @param user user.
     * @return user details.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Map<String, String> user(Principal user) {
        return Collections.singletonMap("message", "user is: " + user.toString());
    }
    
    /**
     * 
     * @return rest template.
     */
    @Bean
    public RestTemplateCall<?,?> connectionProcessor() {
        return new RestTemplateCallImpl<>();
    }
    
    @Bean
    public RestCall<?,?> openFaasProcessor() {
        return new RestCallImpl<>();
    }
    /**
     * 
     * @return ret template connector.
     */
    @Bean
    public az.ancode.filemanager.connector.RestTemplateCall<?,?> fileConnector() {
        return new az.ancode.filemanager.connector.RestTemplateCallImpl<>();
    }
    
    /**
     * 
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(RESOURCE_LOCATIONS);
    }
    
    /**
     * 
     * @return rest template instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
