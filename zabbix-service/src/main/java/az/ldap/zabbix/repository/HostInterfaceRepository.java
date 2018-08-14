package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.HostInterface;

/** MongoDB repository for Host interface. */
@Repository
public interface HostInterfaceRepository extends MongoRepository<HostInterface, String> {

	@Query("{interfaceId : ?0}")
	HostInterface findByInterfaceId(String interfaceId);
	
	@Query("{hostId : ?0}")
	List<HostInterface> findByHostId(String hostId);
	
	@Query("{port : ?0, ipAddress : ?1}")
	List<HostInterface> findByPortAndIP(String port, String ip);
}
