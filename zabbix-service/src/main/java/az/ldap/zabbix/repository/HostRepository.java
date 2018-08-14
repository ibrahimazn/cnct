package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import az.ldap.zabbix.entity.Host;

/** MongoDB repository for Host. */
@Repository
public interface HostRepository extends MongoRepository<Host, String> {
	
	@Query("{hostId : ?0}")
	Host findByHostId(String hostId);
	
	@Query("{host : ?0}")
	Host findByName(String name);
	
	@Query("{available : ?0}")
	List<Host> findByStatus(Integer status);
	
	@Query("{domainid_ : ?0}")
	List<Host> findByDomain(Integer domain);
	
	@Query("{departmentid_ : ?0}")
	List<Host> findByUser(Integer departmentId);
	
	@Query("{uuid : ?0}")
	Host findByHostUuid(String uuid);
}
