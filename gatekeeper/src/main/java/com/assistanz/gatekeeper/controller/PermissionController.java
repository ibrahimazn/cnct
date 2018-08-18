package com.assistanz.gatekeeper.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assistanz.gatekeeper.model.Group;
import com.assistanz.gatekeeper.model.Permission;
import com.assistanz.gatekeeper.service.GroupService;
import com.assistanz.gatekeeper.service.PermissionService;

/**
 * Permission Controller.
 */
@RestController
@RequestMapping("/permission")

public class PermissionController {

    /** Autowired PermissionService. */
    @Autowired
    private PermissionService permissionService;

    /** Autowired GroupService. */
    @Autowired
    private GroupService groupService;

    /**
     * List the permission.
     *
     * @return list of permission.
     * @throws Exception error occurs.
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected List<Permission> getPermissionList() throws Exception {
        return permissionService.getPermissionList();
    }

    /**
     * List the available Groups with its permission.
     *
     * @return group list
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "group/list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected List<Group> getGroupPermissionList() throws Exception {

        List<Group> group = groupService.getGroupList();
        List<Group> groupList = new ArrayList<Group>();

        for (Group groupObj : group) {
            List<Permission> permissionList = permissionService.getPermissionListByGroupId(groupObj);
            groupObj.setPermission(permissionList);
            groupList.add(groupObj);
        }
        return groupList;
    }

}
