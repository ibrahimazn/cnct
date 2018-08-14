package az.ldap.zabbix.service;

import java.util.List;
import az.ldap.zabbix.entity.Host;

/**
 * Service class for Host.
 *
 * This service provides basic CRUD and essential api's for Host related
 * business actions.
 */
public interface HostService extends CRUDService<Host> {

	/**
	 * Get all host from zabbix server.
	 *
	 * @return list of active host.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<Host> findAllFromZabbixServer() throws Exception;
	
	List<Host> findAllByState(Integer state) throws Exception;
	
	Host getHost(String hostId) throws Exception;
	
	Host updateHost(Host host) throws Exception;
	
	Host updateStateHost(Host host) throws Exception;
	
	void deleteAll(List<Host> host) throws Exception;
	
	Host deleteHost(Host host) throws Exception;
	
	Host findByName(String name) throws Exception;
	
	List<Host> findAllByUser(Integer id, String type) throws Exception;
	
	Host findByHostUuid(String uuid) throws Exception;
}
