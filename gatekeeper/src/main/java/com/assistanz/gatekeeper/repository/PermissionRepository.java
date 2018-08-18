package com.assistanz.gatekeeper.repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.assistanz.gatekeeper.model.Group;
import com.assistanz.gatekeeper.model.Permission;

/**
 * Permission repository for get data from database.
 *
 */
@Repository
public interface PermissionRepository extends PagingAndSortingRepository<Permission, Long> {

    /**
     * List the permission.
     *
     * @return list of permission.
     */
    @Query(value = "select permission from Permission permission where permission.isActive IS TRUE")
    List<Permission> getPermissionList();

    /**
     *  List the available Groups with its permission.
     *
     * @param group role group
     * @return permission list
     */
    @Query(value = "select permission from Permission permission where permission.group = :group")
    List<Permission> getPermissionListByGroupId(@Param("group") Group group);

}
