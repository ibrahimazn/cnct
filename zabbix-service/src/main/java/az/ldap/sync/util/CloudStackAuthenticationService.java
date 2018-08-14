package az.ldap.sync.util;

import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * CloudStack Authentication Service for cloud Stack connectivity.
 *
 */
@Service
public class CloudStackAuthenticationService {

    /** Cloudstack server for connectivity. */
    @Autowired
    private CloudStackServer server;

    /**
     * sets api key , secret key and url.
     *
     * @param server sets these values.
     */
    public void setServer(CloudStackServer server) {
        this.server = server;
    }

    /**
     * Logs a user into the CloudStack.
     *
     * @param userName - authentication user name
     * @param password - authentication password
     * @param response - Response format as json
     * @param optional - List of optional values
     * @return - Json string response
     * @throws Exception - Raise if any error
     */
    public String login(String userName, String password, String response, HashMap<String, String> optional)
            throws Exception {
    	MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("login", optional);
        arguments.add("username", userName);
        arguments.add("password", password);
        arguments.add("response", response);
        String responseJson = server.requestLogin(arguments);
        return responseJson;
    }

    /**
     * Logs out the user.
     *
     * @param response - Response format as json
     * @param optional - List of optional values
     * @return - Json string response
     * @throws Exception - Raise if any error
     */
    public String logout(String response, HashMap<String, String> optional) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("logout", optional);
        arguments.add("response", response);
        String responseJson = server.request(arguments);
        return responseJson;
    }
}
