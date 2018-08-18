package com.assistanz.gatekeeper.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.gatekeeper.model.Group;
import com.assistanz.gatekeeper.model.Permission;
import com.assistanz.gatekeeper.repository.PermissionRepository;
import com.assistanz.gatekeeper.util.sorting.PagingAndSorting;

/** Service Implementation Interface for the Permission. */
@Service("permissionService")
public class PermissionServiceImpl  implements PermissionService {

    /** Autowired permissionRepository. */
    @Autowired
    private PermissionRepository permissionRepo;

    @Override
    public List<Permission> getPermissionList() throws Exception {
        return permissionRepo.getPermissionList();
    }

    @Override
    public List<Permission> getPermissionListByGroupId(Group group) throws Exception {
        return permissionRepo.getPermissionListByGroupId(group);
    }

	@Override
	public Permission save(Permission permission) throws Exception {
		return permissionRepo.save(permission);
	}

	@Override
	public Permission update(Permission permission) throws Exception {
		return permissionRepo.save(permission);
	}

	@Override
	public void delete(Permission permission) throws Exception {
		permissionRepo.delete(permission);
	}

	@Override
	public void delete(Long id) throws Exception {
		permissionRepo.delete(id);
	}

	@Override
	public Permission softDelete(Permission permission) throws Exception {
		return permissionRepo.save(permission);
	}

	@Override
	public Permission find(Long id) throws Exception {
		return permissionRepo.findOne(id);
	}

	@Override
	public Page<Permission> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return permissionRepo.findAll(pagingAndSorting.toPageRequest());
	}

	@Override
	public List<Permission> findAll() throws Exception {
		return (List<Permission>) permissionRepo.findAll();
	}

}
