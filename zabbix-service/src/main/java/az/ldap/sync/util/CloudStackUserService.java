package az.ldap.sync.util;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * CloudStack User service for connectivity with CloudStack server.
 *
 */
@Service
public class CloudStackUserService {

    /**
     * Cloudstack server for connectivity.
     */
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
     * Creates a user for a department already exists in Cloudstack.
     *
     * @param accountName Creates the user under the specified account. If no account is specified, the username will be
     *            used as the account name.
     * @param emailId email ID
     * @param firstName first name
     * @param lastName last name
     * @param userName Unique user name.
     * @param password Hashed password (Default is MD5). If you wish to use any other hashing algorithm, you would need
     *            to write a custom authentication adapter See Docs section.
     * @param optional values from cloudstack.
     * @return responseObject
     * @throws Exception if error occurs.
     */
    public String createUser(String accountName, String emailId, String firstName, String lastName, String userName,
            String password, String response, HashMap<String, String> optional) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("createUser", optional);
        arguments.add("account", accountName);
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
     * Delete user from the department in cloudstack.
     *
     * @param userId user id
     * @param response json or xml format.
     * @return responseObject
     * @throws Exception if error occurs.
     */
    public String deleteUser(String userId, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("deleteUser", null);
        arguments.add("id", userId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;

    }

    /**
     * Update a user from department in Cloudstack.
     *
     * @param userId the User id
     * @param optional values from cloudstack.
     * @param response json or xml format
     * @return responseObject
     * @throws Exception if error occurs.
     */
    public String updateUser(String userId, HashMap<String, String> optional, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("updateUser", optional);
        arguments.add("id", userId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Lists all the users from Cloudstack.
     *
     * @param optional values from cloud stack
     * @param response json or xml format
     * @return responseObject
     * @throws Exception if error occurs
     */

    public String listUsers(HashMap<String, String> optional, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("listUsers", optional);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Locks user in cloud stack.
     *
     * @param userId the user id
     * @param response json or xml format
     * @return user
     * @throws Exception if error occurs
     */

    public String lockUser(String userId, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("lockUser", null);
        arguments.add("id", userId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Disables user in cloud stack.
     *
     * @param userId the user id
     * @param response json or xml format
     * @return responseObject
     * @throws Exception if error occurs
     */

    public String disableUser(String userId, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("disableUser", null);
        arguments.add("id", userId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Enables User in cloud stack.
     *
     * @param userId the user id
     * @return responseObject
     * @param response json or xml format
     * @throws Exception if error occurs
     */
     public String enableUser(String userId, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("enableUser", null);
        arguments.add("id", userId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Finds user account by API key in cloud stack.
     *
     * @param userApiKey the user api key
     * @return responseObject
     * @param response json or xml format
     * @throws Exception if error occurs
     */
    public String GetUser(String userApiKey, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("getUser", null);
        arguments.add("userapikey", userApiKey);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Allows a user to register their api and secret key from Cloudstack.
     *
     * @param userId The user id
     * @return responseObject
     * @param response json or xml format
     * @throws Exception if error occurs
     */
    public String registerUserKeys(String userId, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("registerUserKeys", null);
        arguments.add("id", userId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

   /**
    * Asynchronous job result response from cloud stack.
    *
    * @param jobid the ID of the asychronous job
    * @param response json or xml.
    * @return response.
    * @throws Exception if error occurs.
    */
   public String associatedJobResult(String jobid, String response)
           throws Exception {

       MultiValueMap<String, String> arguments
               = server.handleMultiValuedQueryParam("queryAsyncJobResult", null);
       arguments.add("jobid", jobid);
        arguments.add("response",response);
       String responseObject = server.request(arguments);
       return responseObject;
   }
}
