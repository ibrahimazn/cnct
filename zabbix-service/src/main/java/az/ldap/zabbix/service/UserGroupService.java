package az.ldap.zabbix.service;

import java.util.List;

import az.ldap.zabbix.entity.UserGroup;

public interface UserGroupService extends CRUDService<UserGroup> {

	/**
	 * Get all user from zabbix server.
	 *
	 * @return list of active user.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<UserGroup> findAllFromZabbixServer() throws Exception;
	
	UserGroup syncSave(UserGroup userGroup) throws Exception;
	
	List<UserGroup> findAllByType(String type) throws Exception;
	
	void removeUserGroup(String name) throws Exception;
	
	void deleteUserGroup(UserGroup userGroup) throws Exception;
	
	void deleteAllUserGroup(List<UserGroup> userGroup) throws Exception;
	
	/**
	 * Get all usergroup from ldap server and sync those in zabbix.
	 *
	 * @return list of active usergroup.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<UserGroup> SyncUserGroup() throws Exception;
	
	UserGroup getByName(String name) throws Exception;
	
	UserGroup findByHost(String hostId) throws Exception;
}
