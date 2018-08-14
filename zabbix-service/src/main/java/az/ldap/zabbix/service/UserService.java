package az.ldap.zabbix.service;

import java.util.List;
import az.ldap.zabbix.entity.User;

public interface UserService extends CRUDService<User> {

	/**
	 * Get all user from zabbix server.
	 *
	 * @return list of active user.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<User> findAllFromZabbixServer() throws Exception;
	
	List<User> findByEmail(String email) throws Exception; 

	void deleteUser(User user) throws Exception;
	
	void deleteAllUser(List<User> user) throws Exception;
	
	User syncSave(User user) throws Exception; 
	
	/**
	 * Get all user from ldap server and sync those in zabbix.
	 *
	 * @return list of active user.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<User> SyncUser() throws Exception;
	
	List<User> findByGroupId(String groupId) throws Exception;
}
