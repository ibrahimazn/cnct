package az.ldap.zabbix.service;

import java.util.List;
import org.springframework.stereotype.Service;

import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.Item;

@Service
public interface ItemService extends CRUDService<Item> {

	/**
	 * Get all default items for zabbix server.
	 * 
	 * @param osType
	 *            os types.
	 * @param hostId
	 *            hosts id.
	 * @return list of active items.
	 * @throws Exception
	 *             if error occurs.
	 */
	List<Item> findAllByHostandOsType(String hostId, String osType) throws Exception;
	
	List<Item> findAllFromZabbixServer(String hostId) throws Exception;
	
	void syncItemFromZabbixServer(List<String> hostIds) throws Exception;
	
	List<Item> findAllByHost(String hostId) throws Exception;
	
	List<Item> findAllByUsage(String hostId) throws Exception;
	
	Item findAllByKeyAndHost(String hostId, String key) throws Exception;
	
	Integer zabbixItemCount(String hostId) throws Exception;
	
	Item deleteItem(Item item) throws Exception;	
	
	void deleteAllItem(List<Item> item) throws Exception;
	
	List<Item> loadDefaultItems(Host host) throws Exception;
	
	List<Item> getItemsForTrigger(String hostId, String osType) throws Exception;
	
	List<Item> findAllByUser(Integer id, String type) throws Exception;
}
