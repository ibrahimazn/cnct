package az.ldap.sync.util;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * CloudStack Domain service for cloudStack server connectivity with domain.
 *
 */
@Service
public class CloudStackDomainService {

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
     * Creates a domain in ACS.
     *
     * @param domainName create a domain with that name
     * @param optional values to mapped in ACS call.
     * @param response json or xml.
     * @return responseObject
     * @throws Exception if error occurs.
     */
    public String createDomain(String domainName, String response, HashMap<String, String> optional) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("createDomain", optional);
        arguments.add("name", domainName);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Updates a domain in ACS with a name
     *
     * @param domainId of the domain to be updated
     * @param optional values to mapped in ACS call.
     * @param response json or xml.
     * @return responseObject
     * @throws Exception if error occurs.
     */
    public String updateDomain(String domainId, String response, HashMap<String, String> optional) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("updateDomain", optional);
        arguments.add("id", domainId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Deletes a particular domain in ACS.
     *
     * @param domainId of the domain to be deleted.
     * @param response json or xml.
     * @return responseObject
     * @throws Exception if error occurs.
     */
    public String deleteDomain(String domainId, String response) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("deleteDomain", null);
        arguments.add("id", domainId);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Lists al the domains from ACS.
     *
     * @param optional values to mapped in ACS call.
     * @param response json or xml
     * @return responseObject.
     * @throws Exception unhandled errors.
     */
    public String listDomains(String response, HashMap<String, String> optional) throws Exception {

        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("listDomains", optional);
        arguments.add("response", response);
        String responseObject = server.request(arguments);
        return responseObject;
    }

    /**
     * Job status for deleting a domain.
     *
     * @param jobid job id to be update domain response from ACS.
     * @param responsejson or xml.
     * @param response json or xml.
     * @return responseObject
     * @throws Exception if error occurs.
     */
    public String queryAsyncJobResult(String jobid, String response) throws Exception {
        MultiValueMap<String, String> arguments = server.handleMultiValuedQueryParam("queryAsyncJobResult", null);
        arguments.add("jobid", jobid);
        arguments.add("response", response);
        return server.request(arguments);
    }
}
