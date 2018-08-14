package az.ldap.zabbix.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.Alert;
import az.ldap.zabbix.entity.Events;

/** MongoDB repository for Graph. */
@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {

	@Query("{userid : ?0}")
	List<Alert> findByUserId(String userId);
	
	@Query("{alertid : ?0}")
	Alert findByAlert(String alertId);

	@Query("{hostId : ?0}")
	List<Alert> findAllByHost(String hostId);

	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }, hostId : ?2}")
	List<Alert> findbyAlertDateBetweenForDateAndHost(Date from, Date to, String type, String hostId);
	
	@Query(value = "{ 'clock' : {$gte : ?0, $lte: ?1 }}")
	List<Alert> findbyAlertDateBetweenForDate(Date from, Date to, String type, String hostId);
}
