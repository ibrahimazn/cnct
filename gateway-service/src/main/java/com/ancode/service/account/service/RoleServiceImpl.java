/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.Role;
import com.ancode.service.account.repository.RoleRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class RoleServiceImpl.
 */
@Service
public class RoleServiceImpl implements RoleService {

  /** RoleRepository reference. */
  @Autowired
  private RoleRepository roleRepo;

  @Override
  public Role save(Role t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Role update(Role t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(Role t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public Role find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<Role> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Role> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Role> findAllRolesByIsActive(Boolean isActive) throws Exception {
    return roleRepo.findAllRolesByIsActive(isActive);
  }

}
