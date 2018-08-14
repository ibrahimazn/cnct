package az.ldap.zabbix.service;

import java.util.List;

import az.ldap.zabbix.entity.Graph;

/**
 * Service class for Graph.
 *
 * This service provides basic CRUD and essential api's for Graph related
 * business actions.
 */
public interface GraphService extends CRUDService<Graph> {

	List<Graph> findAllFromZabbixServer(String hostId) throws Exception;
	
	List<Graph> findAllByHost(String hostId) throws Exception;
	
	List<Graph> findAllByItem(String itemId, String hostId) throws Exception;
	
	List<Graph> findAllByHostAndFavorite(String hostId, Boolean isFavorite) throws Exception;
	
	List<Graph> findAllByFavorite(Boolean isFavorite) throws Exception;
	
	List<Graph> setFavorite(String graphId, Boolean isFavorite) throws Exception;
	
	void syncGraphFromZabbixServer(List<String> hostIds) throws Exception;
	
	void deleteGraph(Graph graph) throws Exception;
	
	void deleteAllGraph(List<Graph> graph) throws Exception;
}
