package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.Trigger;

/** MongoDB repository for Trigger. */
@Repository
public interface TriggerRepository extends MongoRepository<Trigger, String> {

	@Query("{hostId : ?0}")
	List<Trigger> findByHostId(String hostId);
	
	@Query("{domainid_ : ?0}")
	List<Trigger> findByDomain(Integer domain);
	
	@Query("{departmentid_ : ?0}")
	List<Trigger> findByUser(Integer department);
	
	@Query("{hostId : { $nin: ?0}}")
	List<Trigger> findTriggerByNonHost(List<String> hostIds);
	
	@Query("{triggerid : ?0}")
	Trigger findByTriggerId(String triggerId);
	
	@Query("{status : ?0}")
	List<Trigger> findByStatus(Integer status);
	
	@Query("{metricId : ?0, hostId : ?1}")
	List<Trigger> findByItemId(String itemId, String hostId);
	
	@Query("{'triggerid' : {$exists :true, $ne: null}}")
	List<Trigger> findAllByTrigger();
}
