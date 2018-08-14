package az.ldap.sync.domain.repository;

import org.springframework.ldap.repository.LdapRepository;

import az.ldap.sync.domain.entity.LdapUser;

public interface LdapUserRepository extends LdapRepository<LdapUser>  {

}
