package az.ldap.sync.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import az.ldap.sync.domain.entity.Account;
import az.ldap.sync.domain.entity.Domain;
import az.ldap.sync.domain.entity.Group;
import az.ldap.sync.domain.entity.LdapDomainGroup;
import az.ldap.sync.domain.entity.LdapUser;
import az.ldap.sync.domain.entity.User;
import az.ldap.sync.util.CloudStackServer;

/**
 * Synchronization cloudstack users with Ldap server.
 */
@Service
public interface LdapSyncService {
	/**
     * Sync initialize method used to set CS server api,secret Key.
     *
     * @param server inject cloudstack server.
     * @throws Exception handles unhandled errors.
     */
    void init(CloudStackServer server) throws Exception;

    /**
     * Sync method consists of method to be called.
     *
     * @throws Exception handles unhandled errors.
     */
    void sync() throws Exception;
    
    void zabbixSync() throws Exception;
    
    LdapUser updatePassword(String name, String password) throws Exception;
    
    List<User> getUserList() throws Exception;
    
    List<Account> getAccountList() throws Exception;
    
    List<Domain> getDomainList() throws Exception;
    
    /**
     * Verify duplicate/empty email user account.
     *
     * @throws Exception if error.
     */
    JSONObject verifyDuplicateUserEmail() throws Exception;    

    /**
     * Sync with CloudStack server Domain.
     *
     * @throws Exception cloudstack unhandled errors
     */
    void syncDomain() throws Exception;
    
    /**
     * Sync with Cloud Server Account.
     *
     * @throws Exception cloudstack unhandled errors.
     */
    void syncUser() throws Exception;
    
    /**
     * Sync with CloudStack server Templates.
     *
     * @throws Exception cloudstack unhandled errors
     */
    void syncAccount() throws Exception;
}
