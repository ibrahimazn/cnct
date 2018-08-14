package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.Graph;

/** MongoDB repository for Graph. */
@Repository
public interface GraphRepository extends MongoRepository<Graph, String> {

	@Query("{graphId : ?0}")
	Graph findByGraphId(String graphId);
	
	@Query("{hostId : ?0}")
	List<Graph> findByHostId(String hostId);
	
	@Query("{domainid_ : ?0}")
	List<Graph> findByDomain(Integer domain);
	
	@Query("{departmentid_ : ?0}")
	List<Graph> findByUser(Integer department);
	
	@Query("{hostId : ?0, favorite : ?1}")
	List<Graph> findByHostIdAndFavorite(String hostId, Boolean favorite);
	
	@Query("{hostId : { $nin: ?0}}")
	List<Graph> findGraphByNonHost(List<String> hostIds);
	
	@Query("{favorite : ?0}")
	List<Graph> findByFavorite(Boolean favorite);
	
	@Query("{graphId : ?0}")
	List<Graph> findByGraph(String graphId);
	
}
