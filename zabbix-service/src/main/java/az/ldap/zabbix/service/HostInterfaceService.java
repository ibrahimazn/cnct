package az.ldap.zabbix.service;

import java.util.List;

import az.ldap.zabbix.entity.HostInterface;

public interface HostInterfaceService extends CRUDService<HostInterface> {

	/**
	 * Get all interface from zabbix server.
	 *
	 * @return list of active interface.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<HostInterface> findAllFromZabbixServer() throws Exception;
	
	List<HostInterface> findByIpandPort(String ip, String port) throws Exception;

	HostInterface saveInterface(HostInterface intrface) throws Exception;

	List<HostInterface> findByHost(String hostId) throws Exception;

	void deleteById(HostInterface intrface) throws Exception;
	
	void deleteAll(List<HostInterface> intrface) throws Exception;
}
