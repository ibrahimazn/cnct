package az.ldap.zabbix.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import az.ldap.zabbix.entity.HostTemplate;

/** MongoDB repository for host templates. */
@Repository
public interface HostTemplateRepository extends MongoRepository<HostTemplate, String> {

}
