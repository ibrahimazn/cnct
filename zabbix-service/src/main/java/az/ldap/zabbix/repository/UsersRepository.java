package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.Users;

/** MongoDB repository for Action. */
@Repository
public interface UsersRepository extends MongoRepository<Users, String> {
	@Query("{userid : ?0}")
	Users findByUserId(String userId);
	
	@Query("{triggerId : ?0}")
	List<Users> findByTriggerId(String triggerId);
}
