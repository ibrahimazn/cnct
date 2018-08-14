package az.ldap.sync.domain.repository;

import org.springframework.ldap.repository.LdapRepository;

import az.ldap.sync.domain.entity.LdapDomainGroup;

public interface LdapDomainRepository extends LdapRepository<LdapDomainGroup>  {

}
