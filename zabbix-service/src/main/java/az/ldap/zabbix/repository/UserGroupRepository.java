package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.UserGroup;

/** MongoDB repository for User Group. */
@Repository
public interface UserGroupRepository extends MongoRepository<UserGroup, String> {

	@Query("{usrgrpId : ?0}")
	UserGroup findByUserGroupId(String usergroupId);
	
	@Query("{name : ?0}")
	UserGroup findByUserGroupName(String name);
	
	@Query("{hostId : ?0}")
	UserGroup findByHostId(String hostId);
	
	@Query("{type : ?0}")
	List<UserGroup> findByType(String type);
}
