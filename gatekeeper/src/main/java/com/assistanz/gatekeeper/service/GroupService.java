package com.assistanz.gatekeeper.service;

import java.util.List;

import com.assistanz.gatekeeper.model.Group;

/** Service Interface for the Group. */
public interface GroupService {

    /**
     * List the Groups.
     *
     * @return list of permission.
     * @throws Exception error occurs.
     */
    List<Group> getGroupList() throws Exception;
}
