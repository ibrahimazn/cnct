package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.Action;

/** MongoDB repository for Action. */
@Repository
public interface ActionRepository extends MongoRepository<Action, String> {
	@Query("{actionid : ?0}")
	Action findByActionId(String actionId);
	
	@Query("{triggerid : ?0}")
	Action findByTriggerId(String triggerId);
	
	@Query("{triggerid : { $nin: ?0}}")
	List<Action> findByAllNonTriggerId(List<String> triggerIds);
}
