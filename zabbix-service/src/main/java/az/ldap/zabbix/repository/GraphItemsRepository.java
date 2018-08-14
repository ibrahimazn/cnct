package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.GraphItem;

/** MongoDB repository for Graph Items. */
@Repository
public interface GraphItemsRepository extends MongoRepository<GraphItem, String> {
	@Query("{gitemId : ?0}")
	GraphItem findByGraphItemId(String graphItemId);
	
	@Query("{itemId : ?0}")
	GraphItem findByItemId(String ItemId);
	
	@Query("{hostId : ?0}")
	List<GraphItem> findByHostId(String hostId);
	
	@Query("{graphId : ?0}")
	List<GraphItem> findByGraphId(String graphId);
	
	@Query("{hostId : { $nin: ?0}}")
	List<GraphItem> findGraphItemByNonHost(List<String> hostIds);
	
	@Query("{itemId : { $in: ?0}}")
	List<GraphItem> findGraphItemByNonItems(List<String> itemsId);
}
