package az.ldap.zabbix.service;

import java.util.List;

import az.ldap.zabbix.entity.Action;
import az.ldap.zabbix.entity.Alarms;
import az.ldap.zabbix.entity.Trigger;

/**
 * Service class for Trigger.
 *
 * This service provides basic CRUD and essential api's for Graph related
 * business actions.
 */
public interface TriggerService extends CRUDService<Trigger> {

	List<Trigger> findAllFromZabbixServer(String hostId) throws Exception;
	
	List<Trigger> findAllByHost(String hostId) throws Exception;
	
	List<Trigger> findAllByItem(String itemId, String hostId) throws Exception;
	
	void syncTriggerFromZabbixServer(List<String> hostIds) throws Exception;
	
	List<Alarms> saveTrigger(Trigger trigger) throws Exception;
	
	List<Alarms> updateTrigger(Trigger trigger) throws Exception;	
	
	List<Alarms> deleteTriggers(Trigger trigger, Action action) throws Exception;
	
	List<Alarms> getAllAlarms(String hostId) throws Exception;
	
	void deleteAll(List<Trigger> trigger)  throws Exception;
	
	List<Trigger> findAllFromConfig(String osType) throws Exception;
	
	List<Trigger> updateByHost(String hostId, String hostName, String osType) throws Exception;
}
