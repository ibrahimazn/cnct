package com.assistanz.gatekeeper.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.assistanz.gatekeeper.model.Role;

/**
 * Role repository for get data from database.
 *
 */
@Repository("roleRepository")
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    /**
     * Find role by the role name.
     *
     * @param name role name
     * @return role
     */
    Role findByRole(String name);

    /**
     * Find List of Roles by the Active Status.
     *
     * @param isActive active
     * @return role list
     */
    @Query(value = "SELECT role FROM Role role WHERE role.isActive = :isActive")
    List<Role> findByIsActive(@Param("isActive") Boolean isActive);


    /**
     * List active Role with pagination.
     *
     * @param pageable page
     * @param active active status
     * @return Role
     */
    @Query(value = "SELECT role FROM Role role WHERE role.isActive = :isActive")
    Page<Role> findByRoleIsActive(Pageable pageable, @Param("isActive") Boolean isActive);

    /**
     * List the active roles by search text with pagination.
     *
     * @param pageable pagination
     * @param search search text
     * @param isActive active status
     * @return role list with pagination
     */
    @Query(value = "SELECT role FROM Role role WHERE role.isActive = :isActive  AND (role.role LIKE %:search%)")
    Page<Role> findBySearchText(Pageable pageable, @Param("search") String search, @Param("isActive") Boolean isActive);

}
