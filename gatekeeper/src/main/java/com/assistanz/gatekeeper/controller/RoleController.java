package com.assistanz.gatekeeper.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.assistanz.gatekeeper.constants.GenericConstants;
import com.assistanz.gatekeeper.model.Role;
import com.assistanz.gatekeeper.model.User;
import com.assistanz.gatekeeper.service.RoleService;
import com.assistanz.gatekeeper.util.sorting.PagingAndSorting;
import com.assistanz.gatekeeper.util.web.ApiController;
import com.assistanz.gatekeeper.util.web.CRUDController;

/**
 * Role Controller.
 */
@RestController
@RequestMapping("/role")
public class RoleController extends CRUDController<Role> implements ApiController {

    /** Service reference to role. */
    @Autowired
    private RoleService roleService;

    /**
     * Method for Role Creation.
     *
     * @param roles roles
     * @param bindingResult binding Result
     * @return roles
     */
    @RequestMapping(value = "/createRole", method = RequestMethod.POST)
    @ResponseBody
    public Role createRole(@RequestBody Role role, BindingResult bindingResult) {
        Role roleExists = roleService.findRoleByName(role.getRole());
        ModelAndView modelAndView = new ModelAndView();
        if (roleExists != null) {
            bindingResult
                    .rejectValue("role", "error.role",
                            "Already Role Exists");
        } else {
            try {
                roleService.save(role);
                modelAndView.addObject("successMessage", "Role has been created successfully");
                modelAndView.addObject("user", new User());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return role;
    }

    /**
     * Method for Updating existing Roles.
     *
     * @param role role
     * @return role
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Role updateRole(@RequestBody Role role) throws Exception {
       return roleService.update(role);
    }

    /**
     * Method to soft delete Role.
     *
     * @param role role
     * @param id role id
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public void softDelete(@RequestBody Role role, @PathVariable("id") Integer id) throws Exception {
        /** Doing Soft delete from the Role table. */
        roleService.softDelete(role);
    }

    /**
     * Method to read role object.
     *
     * @param id role id
     * @return role
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE })
    public Role read(@PathVariable("id") Long id) throws Exception {
        return roleService.find(id);
    }

    /**
     * List all avialble avtive roles.
     *
     * @return roles
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected List<Role> findAllByIsActive() throws Exception {
        return roleService.findAllByIsActive(true);
    }

    /**
     * List Role with Pagination.
     *
     * @param sortBy sort by fields
     * @param range range
     * @param limit limit
     * @param request request
     * @param response response
     * @return role
     * @throws Exception if error occurs
     */
    @RequestMapping(value = "listview", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Role> listView(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
            @RequestParam(required = false) Integer limit, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Role.class);
        Page<Role> pageResponse = roleService.findAllByIsActive(page, true);
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }

    /**
     * List the Roles based on the Search Text with Pagination.
     *
     * @param sortBy sort by fields
     * @param range range
     * @param limit limit
     * @param searchText search text
     * @param request request
     * @param response response
     * @return role list with Pagination
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "listbysearchtext", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Role> listBySearchText(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
            @RequestParam Integer limit, @RequestParam String searchText, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Role.class);
        Page<Role> pageResponse = roleService.findAllByActiveAndSearchText(page,searchText,true);
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }
}
