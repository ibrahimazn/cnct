package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.Groups;

/** MongoDB repository for Action. */
@Repository
public interface GroupsRepository extends MongoRepository<Groups, String> {
	@Query("{usrgrpid : ?0}")
	Groups findByUsergroupId(String usrgrpid);
	
	@Query("{triggerId : ?0}")
	List<Groups> findByTriggerId(String triggerId);
}
