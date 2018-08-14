package az.ldap.zabbix.service;

import java.util.List;
import az.ldap.zabbix.entity.HostGroup;

/**
 * Service class for Host group.
 *
 * This service provides basic CRUD and essential api's for Host group related
 * business actions.
 */
public interface HostGroupService extends CRUDService<HostGroup> {

	/**
	 * Get all hostgroup from zabbix server.
	 *
	 * @return list of active hostgroup.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<HostGroup> findAllFromZabbixServer() throws Exception;

	HostGroup findByName(String name) throws Exception;
	
	List<HostGroup> findAllByType(String type) throws Exception;
	
	HostGroup syncSave(HostGroup hostGroup) throws Exception;
	
	void removeHostGroup(String name) throws Exception;
}
