package az.ldap.notification;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Notification service connectivity for monitoring.
 *
 */
@Service
public class NotificationService {
	
	/** Logger attribute. */
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	/** Notification server for connectivity. */
	@Autowired
	private NotificationServer server;
	
	@Autowired
	private NotificationRestTemplateCall<NotificationAction, NotificationAction> restNotificationActionCall;
	
	@Autowired
	private NotificationRestTemplateCall<EmailNotificationStatus, EmailNotificationStatus> restEmailNotificationStatusCall;
	
	/** Notification Connector URL. */
	@Value(value = "${notification.connector}")
	private String notificationURL;

	/**
	 * Set notification connection URL.
	 *
	 * @param server
	 *            sets these values.
	 */
	public void setServer(NotificationServer server) {
		this.server = server;
	}
	
	public NotificationAction updateNotificationAction(NotificationAction notificationAction) throws Exception {
		LOGGER.info("Notification url : "+notificationURL + ": ObjectId : "+notificationAction.getObjectId());
		return restNotificationActionCall.restCall(notificationURL + "/actions", notificationAction, "post", NotificationAction.class, "");
    }
	
	public List<EmailNotificationStatus> getEmailNotificationStatus(String eventId) throws Exception {
		LinkedList<NameValuePair> arguments = new LinkedList<NameValuePair>();
		arguments.add(new NameValuePair("eventId", eventId));
		String responseJson = request(notificationURL + "/emailNotificationStatus/listbyevent", arguments);
		return restEmailNotificationStatusCall.restListPostCall(responseJson, new EmailNotificationStatus(), "get",
			new ParameterizedTypeReference<List<EmailNotificationStatus>>() {
		});
	}
	
	public String request(String api, LinkedList<NameValuePair> queryValues) throws Exception {
		HttpMethod method = null;
		UriComponents uriComponents = null;
		try {
			method = new GetMethod(api);
			method.setFollowRedirects(true);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(api);
			if (queryValues.size() > 0) {
				for (NameValuePair temp : queryValues) {
					builder.queryParam(temp.getName(), temp.getValue());
				}
			}
			uriComponents = builder.build();
		} catch (Exception e) {
			// throw new CloudStackException(e);
		}
		return uriComponents.toString();
	}
}
