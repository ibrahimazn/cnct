package az.ldap.sync.messages;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import az.ldap.sync.constants.CloudStackConstants;
import az.ldap.sync.service.LdapSyncService;

/**
 * Action event listener will listen and update resource data to our App DB when an event handled directly in CS server.
 */
public class ActionListener implements MessageListener {
	
	/** Logger attribute. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionListener.class);

    /** Response event entity. */
    private ResponseEvent eventResponse = null;
    
    /** Cloud stack user service. */
    private LdapSyncService ldapSyncService;

    /**
     * Inject SyncService.
     *
     * @param ldapSyncService user service object.
     */
    public ActionListener(LdapSyncService ldapSyncService) {
        this.ldapSyncService = ldapSyncService;
        
    }

    /** Action event listener . */
    @Override
    public void onMessage(Message message) {
        try {
            LOGGER.debug("Action event listener started");
            String eventName = "";
            JSONObject eventObject = new JSONObject(new String(message.getBody()));
            if (eventObject.has(CloudStackConstants.CS_EVENT_NAME)
                    && eventObject.has(CloudStackConstants.CS_EVENT_STATUS)) {
                    if (eventObject.getString(CloudStackConstants.CS_EVENT_NAME) != null) {
                        eventName = eventObject.getString(CloudStackConstants.CS_EVENT_NAME);
                        String eventStart = eventName.substring(0, eventName.indexOf('.', 0)) + ".";
                        this.handleActionEvent(eventName, eventStart, new String(message.getBody()));
                    }
            }
            LOGGER.debug("Action Listener" + eventName + " " + new String(message.getBody()));
        } catch (Exception e) {
            LOGGER.debug("Error on convert action event message", e.getMessage());
        }
    }

    /**
     * Handling VM events and updated those in our application DB according to the type of events.
     *
     * @param eventName event name.
     * @param eventStart event name start with.
     * @param eventMessage event message.
     * @throws Exception exception.
     */
    public void handleActionEvent(String eventName, String eventStart, String eventMessage) throws Exception {
		JSONObject eventObject = new JSONObject(eventMessage);
		// Event record from action listener call.
		ObjectMapper eventmapper = new ObjectMapper();
		eventResponse = eventmapper.readValue(eventMessage, ResponseEvent.class);
		String notificationMessage = null;
		if (eventResponse.getDescription() != null) {
			if (eventResponse.getDescription().contains(".")) {
				String[] notications = eventResponse.getDescription().split(".");
				if (notications.length > 0) {
					notificationMessage = notications[0];
				} else {
					notificationMessage = eventResponse.getDescription();
				}
			} else {
				notificationMessage = eventResponse.getDescription();
			}
			if (notificationMessage != null) {
				if (notificationMessage.contains("id") || notificationMessage.contains("Id")
						|| notificationMessage.contains("ID")) {
					String[] notify = notificationMessage.split("id");
					if (notify.length == 0) {
						notificationMessage = eventResponse.getDescription();
					} else {
						notificationMessage = notify[0];
					}
				}
			}
		}
		if (eventResponse.getStatus().equalsIgnoreCase(CloudStackConstants.CS_EVENT_COMPLETE)) {
			if (eventName.contains("USER")) {
				if (eventName.equals("USER.CREATE")) {
					ldapSyncService.getUserList();
				} else if (eventName.equals("USER.DELETE")) {
					ldapSyncService.getUserList();
				} else if (eventName.equals("USER.UPDATE")) {
					ldapSyncService.getUserList();
				} else if (eventName.equals("USER.DISABLE")) {
					ldapSyncService.getUserList();
				} else if (eventName.equals("USER.ENABLE")) {
					ldapSyncService.getUserList();
				} else {

				}
			} else if (eventName.contains("ACCOUNT")) {
				if (eventName.equals("ACCOUNT.CREATE")) {
					ldapSyncService.getUserList();
					ldapSyncService.getAccountList();
				} else if (eventName.equals("ACCOUNT.DELETE")) {
					ldapSyncService.getUserList();
					ldapSyncService.getAccountList();
				} else if (eventName.equals("ACCOUNT.UPDATE")) {
					ldapSyncService.getUserList();
					ldapSyncService.getAccountList();
				}
			} else if(eventName.contains("DOMAIN")){
				if (eventName.equals("DOMAIN.CREATE")) {
					ldapSyncService.getDomainList();
				} else if (eventName.equals("DOMAIN.DELETE")) {
					ldapSyncService.getDomainList();
				} else if (eventName.equals("DOMAIN.UPDATE")) {
					ldapSyncService.getDomainList();
				}
			}
		}
		if (eventResponse.getDescription().contains(CloudStackConstants.CS_STATUS_ERROR)) {

		}
    }
}
