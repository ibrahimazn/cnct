package az.ldap.zabbix.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.Events;

/** MongoDB repository for Graph. */
@Repository
public interface EventRepository extends MongoRepository<Events, String> {

	@Query("{userid : ?0}")
	List<Events> findByUserId(String userId);
	
	@Query("{eventid : ?0, value:  ?1 }")
	List<Events> findByEventId(String eventId, Integer value, org.springframework.data.domain.Sort sort);
	
	@Query("{eventid : ?0, value:  ?1 }")
	List<Events> findByEventIdAndSort(String eventId, Integer value, org.springframework.data.domain.Sort sort);
	
	@Query("{triggerId : ?0}")
	List<Events> findByTrigger(String triggerId);
	
	@Query("{hostId : { $nin: ?0}}")
	List<Events> findEventByNonHost(List<String> hostIds);
	
	@Query("{triggerId : { $nin: ?0}}")
	List<Events> findEventByNonTrigger(List<String> triggers);
	
	@Query("{value : ?0}")
	List<Events> findAllByState(Integer value);
	
	@Query("{value : ?0, domainid_ : ?1}")
	List<Events> findAllByStateAndDomain(Integer value, Integer domain);
	
	@Query("{domainid_ : ?0}")
	List<Events> findAllByDomain(Integer domain);
	
	@Query("{departmentid_ : ?0}")
	List<Events> findAllByDept(Integer dept);
	
	@Query("{value : ?0, departmentid_ : ?1}")
	List<Events> findAllByStateAndUser(Integer value, Integer department);
	
	@Query("{value : ?0}")
	List<Events> findAllByStateAndSort(Integer value, org.springframework.data.domain.Sort sort);
	
	@Query("{value : ?0, domainid_ : ?1}")
	List<Events> findAllByStateAndSortAndDomain(Integer value, Integer domain, org.springframework.data.domain.Sort sort);
	
	@Query("{severity : ?0}")
	List<Events> findAllBySeverity(String severity);
	
	@Query("{severity : ?0, domainid_ : ?1}")
	List<Events> findAllBySeverityAndDomain(String severity, Integer domain);
	
	@Query("{severity : ?0, departmentid_ : ?1}")
	List<Events> findAllBySeverityAndUser(String severity, Integer department);
	
	@Query("{triggerId : ?0, value:  ?1, eventid : ?2}")
	List<Events> findByTriggerAndEventAndValue(String triggerId, Integer value, String eventId);
	
	@Query("{triggerId : ?0 , value:  ?1, {value : {$ne : 0}} }")
	List<Events> findByTrigger(String triggerId, Integer value, org.springframework.data.domain.Sort sort);
	
	@Query("{triggerId : ?0 }")
	List<Events> findByAllTrigger(String triggerId, org.springframework.data.domain.Sort sort);
	
	@Query("{clock : {$ne : null}} ")
	List<Events> findByAllUsingSort(org.springframework.data.domain.Sort sort);
	
	@Query("{clock : {$ne : null}, domainid_ : ?0} ")
	List<Events> findByAllUsingSortAndDomain(Integer domainId, org.springframework.data.domain.Sort sort);
	
	@Query("{clock : {$ne : null}, departmentid_ : ?0} ")
	List<Events> findByAllUsingSortAndUser(Integer department, org.springframework.data.domain.Sort sort);

	@Query("{hostId : ?0}")
	List<Events> findAllByHost(String hostId);
	
	@Query("{itemId : ?0}")
	List<Events> findAllByItemId(String itemId);
	
	@Query(value = "{'clock' : {$gte : ?0, $lte: ?1 }, hostId : ?2 }")
	List<Events> findbyEventDateBetweenForDateAndHost(Date from, Date to, String hostId);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }}")
	List<Events> findbyEventDateBetweenForDate(Date from, Date to);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, domainid_ : ?2}")
	List<Events> findbyEventDateBetweenForDateAndDomain(Date from, Date to, Integer domain);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, departmentid_ : ?2}")
	List<Events> findbyEventDateBetweenForDateAndUser(Date from, Date to, Integer department);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, value : ?2}")
	List<Events> findbyEventDateBetweenForDateAndState(Date from, Date to, Integer state);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, value : ?2, domainid_ : ?3 }")
	List<Events> findbyEventDateBetweenForDateAndStateAndDomain(Date from, Date to, Integer state, Integer domain);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, value : ?2, departmentid_ : ?3 }")
	List<Events> findbyEventDateBetweenForDateAndStateAndUser(Date from, Date to, Integer state, Integer department);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, severity : ?2}")
	List<Events> findbyEventDateBetweenForDateAndType(Date from, Date to, String type);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, severity : ?2, domainid_ : ?3 }")
	List<Events> findbyEventDateBetweenForDateAndTypeAndDomain(Date from, Date to, String type, Integer domain);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, severity : ?2, departmentid_ : ?3}")
	List<Events> findbyEventDateBetweenForDateAndTypeAndUser(Date from, Date to, String type, Integer department);
}
