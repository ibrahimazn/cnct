package az.ldap.zabbix.service;

import java.util.List;
import az.ldap.zabbix.entity.Action;

/**
 * Service class for Actions.
 *
 * This service provides basic CRUD and essential api's for Action related
 * business actions.
 */
public interface ActionService extends CRUDService<Action> {

	List<Action> findAllFromZabbixServer() throws Exception;
	
	Action updateHost(Action action) throws Exception;
	
	Action findByTriggerId(String triggerId) throws Exception;
	
	void deleteAll(List<Action> action) throws Exception;
	
	void removeAllByTriggers(List<String> triggers) throws Exception;
	
	Action deleteAction(Action action) throws Exception;
	
}
