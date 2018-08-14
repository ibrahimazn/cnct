package az.ldap.sync.messages;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import az.ldap.sync.util.JsonUtil;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.service.HostService;

/**
 * Zabbix listener will listen and update resource data to our App DB when an event handled directly in CS
 * server.
 *
 */
public class ZabbixListener implements MessageListener {

    /** Logger attribute. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZabbixListener.class);

    private HostService hostService;
    
    /**
     * Inject zabbix Service.
     *
     */
    public ZabbixListener(HostService hostService) {
        this.hostService = hostService;  
    }

    @Override
    public void onMessage(Message message) {
        try {
            LOGGER.debug("Zabbix event listener started");
            JSONObject instance = new JSONObject(new String(message.getBody()));
            this.handleStatusEvent(instance);
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
    	String entityUuid = JsonUtil.getStringValue(eventObject, "entityUuid");
    	String status = JsonUtil.getStringValue(eventObject, "status");
    	String action = JsonUtil.getStringValue(eventObject, "action");
    	if (entityUuid != null) {
    		Host host = hostService.findByHostUuid(entityUuid);
    		if (host != null && action.equals("DETACH")) {
    			hostService.deleteHost(host);
    		} else if (host != null && action.equals("STATUS_UPDATE")) {
    			if (status.equals("RUNNING")) {
    				host.setStatus(0);
        			hostService.updateHost(host);
    			} else if (status.equals("STOPPED") || status.equals("DESTROYED")) {
    				host.setStatus(1);
        			hostService.updateHost(host);
    			}
    		}
    	}
    	
    }
}
