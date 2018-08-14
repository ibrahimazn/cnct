package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.DefaultTriggers;

/** MongoDB repository for Trigger. */
@Repository
public interface DefaultTriggersRepository extends MongoRepository<DefaultTriggers, String> {
	@Query("{osType : ?0}")
	List<DefaultTriggers> findAllByDefaultAndHost(String osType);
}
