package com.assistanz.gatekeeper.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.gatekeeper.model.Permission;
import com.assistanz.gatekeeper.model.Role;
import com.assistanz.gatekeeper.model.Role.Status;
import com.assistanz.gatekeeper.repository.RoleRepository;
import com.assistanz.gatekeeper.util.sorting.PagingAndSorting;

/**
 * Role service implementation class.
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    /** Role repository reference. */
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByRole(name);
    }

    @Override
    public List<Role> findAllByIsActive(Boolean isActive) {
        return roleRepository.findByIsActive(true);
    }

    @Override
    public Page<Role> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean active) throws Exception {
        return roleRepository.findByRoleIsActive(pagingAndSorting.toPageRequest(), true);
    }

    @Override
    public Page<Role> findAllByActiveAndSearchText(PagingAndSorting page, String searchText, Boolean active) throws Exception {
        return roleRepository.findBySearchText(page.toPageRequest(),searchText, true);

    }

	@Override
	public Role save(Role role) throws Exception {
		role.setStatus(Status.ENABLED);
        role.setIsActive(true);
        List<Permission> perms = new  ArrayList<Permission>();
        perms = role.getPermission();
        return roleRepository.save(role);
	}


	@Override
	public Role update(Role role) throws Exception {
		Role roleObj = this.find(role.getId());
        roleObj.setRole(role.getRole());
        roleObj.setStatus(Status.ENABLED);
        roleObj.setPermission(role.getPermission());
        roleObj.setIsActive(true);
        return roleRepository.save(roleObj);
	}

	
	@Override
	public void delete(Role role) throws Exception {
		roleRepository.delete(role);	
	}

	@Override
	public void delete(Long id) throws Exception {
		roleRepository.delete(id);
	}


	@Override
	public Role softDelete(Role role) throws Exception {
		role.setStatus(Status.DISABLED);
        role.setIsActive(false);
        return roleRepository.save(role);
	}

	
	@Override
	public Role find(Long id) throws Exception {
		return roleRepository.findOne(id);
	}

	@Override
	public Page<Role> findAll(PagingAndSorting page) throws Exception {
		return roleRepository.findAll(page.toPageRequest());
	}

	@Override
	public List<Role> findAll() throws Exception {
		return (List<Role>) roleRepository.findAll();
		
	}

}
