package az.ldap.notification;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * Notification server component to connect to usage server.
 *
 *
 */
@Component
public class NotificationServer {

	/** Logger attribute. */
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServer.class);

	@Value(value = "${server.ssl.enabled}")
	public Boolean sslEnabled;

	private String apiURL;

	/** Default Constructor. */
	public NotificationServer() {

	}

	/**
	 * Parameterized constructor to set notification credentials.
	 *
	 * @param apiURL
	 *            URL of the notification server
	 */
	public void setServer(String apiURL) {
		this.apiURL = apiURL;
	}

	/**
	 * URL Mapping.All notification requests are submitted in the form of a HTTP
	 * GET/POST with an associated command and any parameters.
	 *
	 * @param queryValues
	 *            - The web services command we wish to execute, such as create
	 *            a virtual machine or create a disk volume
	 * @return final URL
	 * @throws Exception
	 *             if any invalid parameters.
	 */
	public String request(LinkedList<NameValuePair> queryValues) throws Exception {
		HttpMethod method = null;
		try {
			method = new GetMethod(apiURL);
			method.setFollowRedirects(true);
			method.setQueryString(queryValues.toArray(new NameValuePair[0]));
		} catch (Exception e) {
			// throw new CloudStackException(e);
		}
		return getResponse(method);
	}

	/**
	 * URL Mapping.All notification requests are submitted in the form of a HTTP
	 * GET/POST with an associated command and any parameters. with headers
	 *
	 * @param queryValues
	 *            - The web services command we wish to execute, such as create
	 *            a virtual machine or create a disk volume
	 * @param method
	 *            - The request method
	 * @return final URL
	 * @throws Exception
	 *             if any invalid parameters.
	 */
	public String requestWithMethod(LinkedList<NameValuePair> queryValues, HttpMethod method) throws Exception {

		if (!sslEnabled) {
			disableSSL();
		}
		method.setFollowRedirects(true);
		method.setQueryString(queryValues.toArray(new NameValuePair[0]));
		return getResponse(method);
	}

	/**
	 * URL Mapping.All notification requests are submitted in the form of a HTTP POST
	 * with an associated command and any parameters.
	 *
	 * @param queryValues
	 *            - The web services command we wish to execute, such as create
	 *            a virtual machine or create a disk volume
	 * @return final URL
	 * @throws Exception
	 *             if any invalid parameters.
	 */
	public String postRequest(String queryValues) throws Exception {
		PostMethod method = null;
		if (!sslEnabled) {
			disableSSL();
		}
		method = new PostMethod(apiURL);
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
		StringRequestEntity requestEntity = new StringRequestEntity(queryValues, "application/json", "UTF-8");
		method.setRequestEntity(requestEntity);
		return getResponse(method);
	}
	
	/**
	 * Upload email template.
	 * 
	 * @param parts multi part file upload
	 * @throws Exception raise if error
	 */
	public String postUploadRequest(Part[] parts) throws Exception {
		PostMethod method = null;
		if (!sslEnabled) {
			disableSSL();
		}
		method = new PostMethod(apiURL);
		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
		HttpClient client = new HttpClient();
		client.executeMethod(method);
		String response = method.getResponseBodyAsString();
		method.releaseConnection();
		return response;
	}

	/**
	 * URL Mapping.All notification requests are submitted in the form of a HTTP PUT
	 * with an associated command and any parameters.
	 *
	 * @param queryValues
	 *            - The web services command we wish to execute, such as create
	 *            a virtual machine or create a disk volume
	 * @return final URL
	 * @throws Exception
	 *             if any invalid parameters.
	 */
	public String putRequest(String queryValues) throws Exception {
		PutMethod method = null;
		if (!sslEnabled) {
			disableSSL();
		}
		method = new PutMethod(apiURL);
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
		StringRequestEntity requestEntity = new StringRequestEntity(queryValues, "application/json", "UTF-8");
		method.setRequestEntity(requestEntity);
		return getResponse(method);
	}

	/**
	 * Get response from the notification in our specified format also throws
	 * exception in case of error.
	 *
	 * @param method
	 *            Passes final URL and connects it with notification.
	 * @return response in xml or json
	 * @throws HttpException
	 *             exception in URL formation.
	 * @throws IOException
	 *             error in signature formation
	 * @throws Exception
	 *             general exceptions
	 */
	public String getResponse(HttpMethod method) throws HttpException, IOException, Exception {
		HttpClient client = new HttpClient();
		String response = null;
		method.setRequestHeader("Range", "items=0-100");
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
		client.executeMethod(method);
		response = method.getResponseBodyAsString();
		method.releaseConnection();
		return response;
	}

	/**
	 * A web service command executed and parameters in command is compared with
	 * the notification parameters.
	 *
	 * @param requestParam
	 *            excluding mandatory fields
	 * @return query values.
	 */
	public LinkedList<NameValuePair> getDefaultQuery(HashMap<String, String> requestParam) {
		LinkedList<NameValuePair> queryValues = new LinkedList<NameValuePair>();
		if (requestParam != null) {
			Iterator optionalIt = requestParam.entrySet().iterator();
			while (optionalIt.hasNext()) {
				Map.Entry pairs = (Map.Entry) optionalIt.next();
				queryValues.add(new NameValuePair((String) pairs.getKey(), (String) pairs.getValue()));
			}
		}
		return queryValues;
	}


	/**
	 * DisableSSL.
	 */
	private void disableSSL() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(final java.security.cert.X509Certificate[] certs,
						final String authType) {
				}

				public void checkServerTrusted(final java.security.cert.X509Certificate[] certs,
						final String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		} catch (Exception ex) {
			LOGGER.debug("Unable to disable ssl verification" + ex);
		}
	}
}
