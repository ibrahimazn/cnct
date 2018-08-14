package az.ldap.sync.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.BasicDBObject;
import az.ldap.sync.domain.entity.Account;
import az.ldap.sync.domain.entity.Domain;
import az.ldap.sync.domain.entity.LdapSync;
import az.ldap.sync.domain.entity.User;
import az.ldap.sync.service.LdapSyncService;

@RestController
@RequestMapping("/zabbix/api/ldap")
public class LdapController {
	
	/** Logger attribute. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapController.class);

	@Autowired
	private LdapSyncService ldapSync;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
    JobLauncher jobLauncher;
    
    @Autowired
    Job itemLoad;
    
    @Autowired
    Job triggerLoad;
	
	@RequestMapping("/domain/manualsync")
	public List<Domain> getAllDomains() throws Exception {
		return ldapSync.getDomainList();
	}
	
	private JobParameters createInitialJobParameterMap() {
        Map<String, JobParameter> m = new HashMap<>();
        m.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters p = new JobParameters(m);
        return p;
	}
	
	@RequestMapping("/department/manualsync")
	public List<Account> getAllDepartments() throws Exception {
		return ldapSync.getAccountList();
	}
	
	@RequestMapping("/user/manualsync")
	public List<User> getAllUsers() throws Exception {
		return ldapSync.getUserList();
	}
	
	@RequestMapping(value = "/sync/{key}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected LdapSync initialSync(@PathVariable("key") String key) throws Exception {
		LdapSync ldapSyncObj = new LdapSync();
		ldapSyncObj.setKey(key);
    	if (key.equals("ALL")) {
    		LOGGER.info("Zabbix all sync started");
    		ldapSync.zabbixSync();
    	} else if (key.equals("ITEM")) {
    		LOGGER.info("Zabbix server item sync started");
    		mongoTemplate.getCollection("default_items").remove(new BasicDBObject());
    		jobLauncher.run(itemLoad, createInitialJobParameterMap());
    	} else if (key.equals("TRIGGER")) {
    		LOGGER.info("Zabbix server trigger sync started");
    		mongoTemplate.getCollection("default_triggers").remove(new BasicDBObject());
    		jobLauncher.run(triggerLoad, createInitialJobParameterMap());
    	}
		return ldapSyncObj;
    }
}
