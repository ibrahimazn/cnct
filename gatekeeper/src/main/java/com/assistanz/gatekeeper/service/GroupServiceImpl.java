package com.assistanz.gatekeeper.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assistanz.gatekeeper.model.Group;
import com.assistanz.gatekeeper.repository.GroupRepository;

/** Service Implementation Interface for the Group. */
@Service("groupService")
public class GroupServiceImpl  implements GroupService {

    /** Autowired permissionRepository. */
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<Group> getGroupList() throws Exception {
        return groupRepository.getGroupList();
    }

}
