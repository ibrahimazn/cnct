package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.User;

/** MongoDB repository for User. */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	@Query("{userId : ?0}")
	User findByUserId(String userId);
	
	@Query("{email : ?0}")
	List<User> findByUserEmail(String email);
	
	@Query("{usergroupsList : { $ref : 'usergroupsList', $id : ?0 }}}")
	List<User> findByGroupId(String groupId);
}
