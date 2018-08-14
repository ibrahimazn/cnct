package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.DiscoveryRule;

/** MongoDB repository for Host. */
@Repository
public interface DiscoveryRuleRepository extends MongoRepository<DiscoveryRule, String> {
	@Query("{hostId : ?0}")
	List<DiscoveryRule> findByHostId(String hostId);
	
}
