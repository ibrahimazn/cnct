package az.ldap.zabbix.service;

import java.util.Date;
import java.util.List;

import az.ldap.sync.util.VMDashboards;
import az.ldap.sync.util.ZabbixDashBoard;
import az.ldap.zabbix.entity.Events;

/**
 * Service class for Graph.
 *
 * This service provides basic CRUD and essential api's for Graph related
 * business actions.
 */
public interface HistoryService extends CRUDService<VMDashboards> {

	VMDashboards findAllByHost(String hostId) throws Exception;
	
	ZabbixDashBoard findByHost(String id, String type, String hostIds) throws Exception;
	
	VMDashboards getVmDashboard(String hostId) throws Exception;
	
	List<Events> getAllAlerts(Integer id, String type) throws Exception;
	
	List<Events> syncEventService(String hostId) throws Exception;
	
	void syncEvent(List<String> hostIds) throws Exception;
	
	void syncEventTrigger(List<String> triggers) throws Exception;
	
	void removeAllEvent(String hostIds) throws Exception;
	
	List<Events> getRecentByFilterAlerts(String hostId, String status, String severity, Integer id, String type) throws Exception;
	
	List<Events> getByFilterAlerts(Date from, Date to, String hostId, Integer status, String severity, Integer id, String type) throws Exception;
}
