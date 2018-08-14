package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.Item;

/** MongoDB repository for Item. */
@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

	@Query("{'$and': [{'itemId' : ?0}, { 'itemId' : {'$exists':'true'} } ]}")
	Item findByItemId(String itemId);
	
	@Query("{itemId : ?0}")
	List<Item> findByItem(String itemId);
	
	@Query("{hostId : ?0, key_ : ?1}")
	Item findItemByKeyAndHost(String hostId, String key_);
	
	@Query("{hostId : { $nin: ?0}}")
	List<Item> findItemByNonHost(List<String> hostIds);
	
	@Query("{hostId : ?0, key_ : { $regex: ?1, $options: 'i' }}")
	List<Item> findItemByLikeKeyAndHost(String hostId, String key_);
	
	@Query("{hostId : ?0}")
	List<Item> findByHost(String hostId);
	
	@Query("{status : ?0}")
	List<Item> findByStatus(Integer status);
	
	@Query("{hostId : ?0, key_:?1}")
	Item findByKeyAndHost(String hostId, String key);
	
	@Query("{usage : ?0, hostId : ?1}")
	List<Item> findAllByUsage(Integer usage, String hostId);

	@Query("{domainid_ : ?0}")
	List<Item> findByDomain(Integer id);

	@Query("{departmentid_ : ?0}")
	List<Item> findByUser(Integer id);
}
