/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.Role;
import com.ancode.service.account.util.service.CRUDService;

/**
 * The Interface RoleService.
 */
@Service
public interface RoleService extends CRUDService<Role> {

  /**
   * Find all Active Roles.
   *
   * @param isActive
   *          isActive status.
   * @return active role list.
   * @throws Exception
   *           if error occurs.
   */
  List<Role> findAllRolesByIsActive(Boolean isActive) throws Exception;
}
