package az.ldap.sync.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.apache.http.HttpException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Rest Template for CloudStack Connector.
 */
@SuppressWarnings("deprecation")
public class RestClient {

	/** HTTP Status. */
    public static final int HTTP_OK = 200;

    /**
     * Retrieve a representation by doing a GET on the specified URL. The response (if any) is converted and returned.
     *
     * @param url requested url.
     * @return response object as json string.
     * @throws HttpException http unhandled exception.
     * @throws IOException IO unhandled exception.
     * @throws URISyntaxException URI unhandled exception.
     */
    public static String doGet(String url, MultiValueMap<String, String> queryValuePair) throws HttpException,
            IOException, URISyntaxException {
		String responseBody = null;
		try {
			RestTemplate rest = new RestTemplate();		
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<String> response = rest.exchange(new URI(url), HttpMethod.GET, entity, String.class);
			responseBody = response.getBody();
		} catch (org.springframework.web.client.UnknownHttpStatusCodeException e) {
			responseBody = e.getResponseBodyAsString();
		} catch (org.springframework.web.client.HttpClientErrorException e) {
			responseBody = e.getResponseBodyAsString();
		} catch (org.springframework.web.client.HttpServerErrorException e) {
			responseBody = e.getResponseBodyAsString();
		}
		return responseBody;
    }

    /**
     * Create a new resource by POSTing the given json string to the URI template, and returns the new resource is stored.
     *
     * @param url requested url.
     * @param POSTText post string as json
     * @return response object as json string.
     * @throws HttpException http unhandled exception.
     * @throws IOException IO unhandled exception.
     * @throws URISyntaxException URI unhandled exception.
     */
    public static String doPost(String url, MultiValueMap<String, String> queryValues)
            throws URISyntaxException, HttpException, IOException {
		String responseBody = null;
		try {
			RestTemplate rest = new RestTemplate();			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<?> requestEntity = new HttpEntity<Object>(queryValues, headers);
			ResponseEntity<String> resp = rest.exchange(url, HttpMethod.POST, requestEntity, String.class);
			responseBody = resp.getBody();
		} catch (org.springframework.web.client.UnknownHttpStatusCodeException e) {
			responseBody = e.getResponseBodyAsString();
		} catch (org.springframework.web.client.HttpClientErrorException e) {
			responseBody = e.getResponseBodyAsString();
		} catch (org.springframework.web.client.HttpServerErrorException e) {
			responseBody = e.getResponseBodyAsString();
		}
		return responseBody;
    }
}
