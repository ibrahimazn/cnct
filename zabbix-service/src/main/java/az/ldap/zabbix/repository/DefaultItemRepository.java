package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.DefaultItems;
import az.ldap.zabbix.entity.Item;

/** MongoDB repository for Item. */
@Repository
public interface DefaultItemRepository extends MongoRepository<DefaultItems, String> {
	@Query("{osType : ?0}")
	List<DefaultItems> findAllByOsType(String osType);
	
	@Query("{osType : ?1, isDefault : ?0}")
	List<DefaultItems> findAllByDefaultAndOstype(Boolean isDefault, String osType);
	
	@Query("{osType : ?0}")
	List<DefaultItems> findAllByDefaultAndHost(String osType);
	
	@Query("{key_ : ?0, osType : ?1}")
	DefaultItems findAllByKey_(String key_, String osType);
	
	@Query("{osType: ?1, key_ : { $regex: ?0, $options: 'i' }}")
	DefaultItems findItemByLikeKeyAndOsType(String key_, String osType);
}
