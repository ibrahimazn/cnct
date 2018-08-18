package com.assistanz.gatekeeper.service;

import java.util.List;

import com.assistanz.gatekeeper.model.Group;
import com.assistanz.gatekeeper.model.Permission;

/** Service Interface for the Permission. */
public interface PermissionService extends CRUDService<Permission> {

    /**
     * List the permission.
     *
     * @return list of permission.
     * @throws Exception error occurs.
     */
    List<Permission> getPermissionList() throws Exception;

    /**
     * List the permission.
     *
     * @param group role group
     * @return list of permission.
     * @throws Exception error occurs.
     */
    List<Permission> getPermissionListByGroupId(Group group) throws Exception;
}
