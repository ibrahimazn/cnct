package az.ldap.sync.util;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 *
 * CloudStack cluster service for connectivity with CloudStack server.
 */

@Service
public class CloudStackAccountService {

    /** Cloudstack server for connectivity. */
    @Autowired
    private CloudStackServer server;

    /**
     * Sets api key , secret key and url.
     *
     * @param server sets these values.
     */
    public void setServer(CloudStackServer server) {
        this.server = server;
    }

    /**
     * Create account in ACS.
     *
     * @param accountType of the department.
     * @param emailId of the user.
     * @param firstName of the user.
     * @param lastName of the user.
     * @param userName of the user.
     * @param password of the user.
     * @param response json or xml response
     * @param optional values to be mapped in acs call
     * @return account
     * @throws Exception if error occurs.
     */
    public String createAccount(String accountType, String emailId, String firstName, String lastName, String userName,
            String password, String response, HashMap<String, String> optional) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("createAccount", optional);
        arguments.add("accounttype", accountType);
        arguments.add("email", emailId);
        arguments.add("firstname", firstName);
        arguments.add("lastname", lastName);
        arguments.add("username", userName);
        arguments.add("password", password);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Delete account from acs.
     *
     * @param accountId of the department.
     * @param response json or xml
     * @return accounts.
     * @throws Exception if error occurs.
     */
    public String deleteAccount(String accountId, String response) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("deleteAccount", null);
        arguments.add("id", accountId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;

    }

   /**
    * Update account in acs.
    *
    * @param newName of the department.
    * @param optional values to be mapped in acs.
    * @return accounts.
    * @throws Exception if error occurs.
    */
    public String updateAccount(String newName, HashMap<String, String> optional) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("updateAccount", optional);
        arguments.add("newname", newName);
        String responseObject = server.request(arguments);
        return responseObject;

    }

    /**
     * Disable account in ACS.
     *
     * @param lock account to be lock
     * @param accountName of the department..
     * @param domainId of the department.
     * @param response json or xml.
     * @param optional values to be mapped in acs call
     * @return accounts.
     * @throws Exception if error occurs.
     */
    public String disableAccount(String lock, String accountName, String domainId, String response,
            HashMap<String, String> optional) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("disableAccount", optional);
        arguments.add("lock", lock);
        arguments.add("account", accountName);
        arguments.add("domainid", domainId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Enables account in ACS.
     *
     * @param accountName of the department.
     * @param domainId of the department.
     * @param response json or xml
     * @param optional values to be mapped in acs call
     * @return accounts
     * @throws Exception if error occurs.
     */
    public String enableAccount(String accountName, String domainId, String response, HashMap<String, String> optional)
            throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("enableAccount", optional);
        arguments.add("account", accountName);
        arguments.add("domainid", domainId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Lock account in ACS.
     *
     * @param accountName of the department.
     * @param domainId of the department.
     * @param response json or xml
     * @param optional values to be mapped in acs call
     * @return accounts.
     * @throws Exception if error occurs.
     */
    public String lockAccount(String accountName, String domainId, String response, HashMap<String, String> optional)
            throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("lockAccount", optional);
        arguments.add("account", accountName);
        arguments.add("domainid", domainId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;

    }

    /**
     * List accounts in ACS.
     *
     * @param response json or xml
     * @param optional values to be mapped in acs call
     * @return accounts.
     * @throws Exception if error occurs.
     */
    public String listAccounts(String response, HashMap<String, String> optional) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("listAccounts", optional);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

   /**
    * Mark the default zone for account.
    *
    * @param accountName of the department.
    * @param domainId of the department.
    * @param response json or xml
    * @param zoneId of the department
    * @param optional values to be mapped in acs call
    * @return accounts.
    * @throws Exception if error occurs.
    */
    public String markDefaultZoneForAccount(String accountName, String domainId, String response, String zoneId,
            HashMap<String, String> optional) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("markDefaultZoneForAccount", optional);
        arguments.add("account", accountName);
        arguments.add("domainid", domainId);
        arguments.add("zoneid", zoneId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Add user to the project.
     *
     * @param projectId of the user
     * @param account name of the department.
     * @param response json or xml
     * @param optional values to be mapped in acs call
     * @return accounts.
     * @throws Exception if error occurs.
     */
    public String addAccountToProject(String projectId, String account, String response,
            HashMap<String, String> optional) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("addAccountToProject", optional);
        arguments.add("projectid", projectId);
        arguments.add("account", account);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;

    }

   /**
    * Asynchronous job result in acs.
    *
    * @param asychronousJobid of the job result.
    * @param response json or xml
    * @return jobresult
    * @throws Exception if error occurs.
    */
    public String accountJobResult(String asychronousJobid, String response) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("queryAsyncJobResult", null);
        arguments.add("jobid", asychronousJobid);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     *  Delete account from project.
     *
     * @param accountName of the department.
     * @param response json or xml
     * @param projectId of the user.
     * @return accounts
     * @throws Exception if error occurs.
     */
    public String deleteAccountFromProject(String accountName, String response, String projectId) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("deleteAccountFromProject", null);
        arguments.add("account", accountName);
        arguments.add("projectid", projectId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

   /**
    * List the project accounts from ACS.
    *
    * @param projectId of the account.
    * @param response json or xml
    * @param optional values to be mapped in acs call
    * @return accounts
    * @throws Exception if error occurs.
    */
    public String listProjectAccounts(String projectId, String response, HashMap<String, String> optional)
            throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("listProjectAccounts", optional);
        arguments.add("projectid", projectId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;

    }

    /**
     * Get the solid fire account id.
     *
     * @param accountId of the department.
     * @param storageId of the volume.
     * @param response json or xml
     * @param optional values to be mapped in acs call
     * @return accounts
     * @throws Exception if error occurs.
     */
    public String getSolidFireAccountId(String accountId, String storageId, String response,
            HashMap<String, String> optional) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("getSolidFireAccountId", optional);
        arguments.add("accountid", accountId);
        arguments.add("storageid", storageId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }
}
