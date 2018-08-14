package az.ldap.sync.messages;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import az.ldap.sync.constants.CloudStackConstants;
import az.ldap.sync.constants.EventTypes;
import az.ldap.sync.service.LdapSyncService;
import az.ldap.sync.util.CloudStackUserService;

/**
 * Asynchronous Job listener will listen and update resource data to our App DB when an event handled directly in CS
 * server.
 *
 */
public class AsynchronousJobListener implements MessageListener {

    /** Logger attribute. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsynchronousJobListener.class);

    /** Asynchronous job id. */
    public static final String CS_ASYNC_JOB_ID = "jobId";
    
    /** Cloud stack user service. */
    private CloudStackUserService cloudStackUserService;
    
    /** Cloud stack user service. */
    private LdapSyncService ldapSyncService;;

    /**
     * Inject SyncService.
     *
     * @param cloudStackUserService user service object.
     * @param ldapSyncService ldap sync.
     */
    public AsynchronousJobListener(CloudStackUserService cloudStackUserService, LdapSyncService ldapSyncService) {
    	this.cloudStackUserService = cloudStackUserService;
        this.ldapSyncService = ldapSyncService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            LOGGER.debug("Asynchronous job event listener started");
            JSONObject instance = new JSONObject(new String(message.getBody()));
            this.handleStatusEvent(instance);
            if (instance.has(CloudStackConstants.CS_EVENT_NAME)) {
                LOGGER.debug("AsyncJob Listener with event" + instance.getString(CloudStackConstants.CS_EVENT_NAME) + " " + new String(message.getBody()));
            } else {
            	LOGGER.debug("AsyncJob Listener without event" + new String(message.getBody()));
            }
        } catch (Exception e) {
            LOGGER.debug("Error on convert action event message", e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * Handling all the CS events and updated those in our application DB according to the type of events.
     *
     * @param eventObject event object.
     * @throws Exception exception.
     */
    public void handleStatusEvent(JSONObject eventObject) throws Exception {
        if (eventObject.has(CloudStackConstants.CS_EVENT_STATUS)) {
			if (eventObject.getString(CloudStackConstants.CS_STATUS)
					.equalsIgnoreCase(CloudStackConstants.CS_STATUS_SUCCEEDED)) {
				String eventObjectResult = cloudStackUserService
						.associatedJobResult(eventObject.getString(CS_ASYNC_JOB_ID), CloudStackConstants.JSON);
				JSONObject jobResultResponse = new JSONObject(eventObjectResult)
						.getJSONObject(CloudStackConstants.QUERY_ASYNC_JOB_RESULT_RESPONSE);
				JSONObject jobResult = null;
				if (jobResultResponse.has(CloudStackConstants.CS_JOB_RESULT)) {
					jobResult = jobResultResponse.getJSONObject(CloudStackConstants.CS_JOB_RESULT);
				}
				if (jobResult != null) {
					switch (eventObject.getString(CloudStackConstants.CS_COMMAND_EVENT_TYPE)) {
					case EventTypes.EVENT_USER_CREATE:
						ldapSyncService.syncUser();
						break;
					case EventTypes.EVENT_USER_DELETE:
						ldapSyncService.syncUser();
						break;
					case EventTypes.EVENT_USER_DISABLE:
						ldapSyncService.syncUser();
						break;
					case EventTypes.EVENT_USER_ENABLE:
						ldapSyncService.syncUser();
						break;
					case EventTypes.EVENT_USER_LOCK:
						ldapSyncService.syncUser();
						break;
					case EventTypes.EVENT_USER_LOGOUT:
						break;
					case EventTypes.EVENT_USER_UPDATE:
						ldapSyncService.syncUser();
						break;
					case EventTypes.EVENT_ACCOUNT_CREATE:
						ldapSyncService.syncUser();
						ldapSyncService.syncAccount();
						break;
					case EventTypes.EVENT_ACCOUNT_DELETE:
						ldapSyncService.syncUser();
						ldapSyncService.syncAccount();
						break;
					case EventTypes.EVENT_ACCOUNT_UPDATE:
						ldapSyncService.syncUser();
						ldapSyncService.syncAccount();
						break;
					case EventTypes.EVENT_DOMAIN_CREATE:
						ldapSyncService.syncDomain();
						break;
					case EventTypes.EVENT_DOMAIN_DELETE:
						ldapSyncService.syncUser();
						ldapSyncService.syncAccount();
						ldapSyncService.syncDomain();
						break;
					case EventTypes.EVENT_DOMAIN_UPDATE:
						ldapSyncService.syncDomain();
						break;
					default:
						LOGGER.debug("No async required");
					}
				}
			}
        }
    }
}
