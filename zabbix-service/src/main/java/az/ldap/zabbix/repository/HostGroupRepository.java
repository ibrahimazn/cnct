package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.HostGroup;

/** MongoDB repository for Host Group. */
@Repository
public interface HostGroupRepository extends MongoRepository<HostGroup, String> {

	@Query("{groupId : ?0}")
	HostGroup findByGroupId(String groupId);
	
	@Query("{name : ?0}")
	HostGroup findByName(String name);
	
	@Query("{type : ?0}")
	List<HostGroup> findByType(String type);
}
