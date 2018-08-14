package az.ldap.zabbix.service;

import java.util.List;

import az.ldap.zabbix.entity.Alert;

/**
 * Service class for Graph.
 *
 * This service provides basic CRUD and essential api's for Graph related
 * business actions.
 */
public interface AlertService extends CRUDService<Alert> {

	List<Alert> findAllFromZabbixServer() throws Exception;
	
	void deleteAllItem(List<Alert> items) throws Exception;
	
	void deleteItem(Alert items) throws Exception;
	
	List<Alert> findAllByHost(String hostId) throws Exception;
	
	List<Alert> findAllByUser(String userId) throws Exception;
}
