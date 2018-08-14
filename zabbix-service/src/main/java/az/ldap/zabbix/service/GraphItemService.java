package az.ldap.zabbix.service;

import java.util.List;

import az.ldap.zabbix.entity.GraphItem;

/**
 * Service class for Graph Items.
 *
 * This service provides basic CRUD and essential api's for Graph Items related
 * business actions.
 */
public interface GraphItemService extends CRUDService<GraphItem> {

	List<GraphItem> findAllFromZabbixServer(String graphIds) throws Exception;
	
	void deleteAllByItems(String graphIds) throws Exception;

	GraphItem findByItem(String id) throws Exception;
	
	List<GraphItem> findAllByHost(String hostId) throws Exception;
	
	void deleteItem(GraphItem items) throws Exception;
	
	void syncGraphItemFromZabbixServer(List<String> hostIds) throws Exception;
	
	void deleteAllItem(List<GraphItem> items) throws Exception;
	
	List<GraphItem> findAllByGraph(String graphId) throws Exception;
}
