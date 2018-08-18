package com.assistanz.gatekeeper.service;

import java.util.List;
import org.springframework.data.domain.Page;

import com.assistanz.gatekeeper.model.Role;
import com.assistanz.gatekeeper.util.sorting.PagingAndSorting;

/** Service Interface for the Role. */
public interface RoleService extends CRUDService<Role> {

        /**
     * Find Role by Name.
     *
     * @param name role name
     * @return role
     */
     Role findRoleByName(String name);

     
    /**
     * Find List of roles by avtive status.
     *
     * @param isActive active status
     * @return roles
     */
     List<Role> findAllByIsActive(Boolean isActive);

     /**
      * List active Role with pagination.
      *
      * @param pagingAndSorting pagination and sorting
      * @param active active status
      * @return Role list
      * @throws Exception if any error occurs
      */
    Page<Role> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean active) throws Exception;

    /**
     * List Active Roles by search text and Pagination.
     *
     * @param page pagination
     * @param searchText search text
     * @param active active status
     * @return role list with pagination
     * @throws if any error occurs
     */
    Page<Role> findAllByActiveAndSearchText(PagingAndSorting page, String searchText, Boolean active) throws Exception;

}
