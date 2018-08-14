package az.ldap.zabbix.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.zabbix.entity.User;
import az.ldap.zabbix.entity.UserGroup;
import az.ldap.zabbix.service.PagingAndSorting;
import az.ldap.zabbix.service.UserGroupService;

@RestController
@RequestMapping("/zabbix/api/usergroup")
public class UserGroupController extends CRUDController<UserGroup> implements ApiController {

	@Autowired
	private UserGroupService userGroupService;
	
	@Override
	public UserGroup create(UserGroup userGroup) throws Exception {
		return userGroupService.save(userGroup);
	}

	@Override
	public UserGroup update(UserGroup userGroup, String id) throws Exception {
		return userGroupService.update(userGroup);
	}

	@Override
	public void delete(String id) throws Exception {
		userGroupService.delete(id);
	}

	@Override
	public UserGroup read(String id) throws Exception {
		return userGroupService.find(id);
	}

	@Override
	public List<UserGroup> list(String sortBy, String range, Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, UserGroup.class);
		Page<UserGroup> pageResponse = userGroupService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<UserGroup> getUserFromZabbix() throws Exception {
        return userGroupService.SyncUserGroup();
    }
	
	@RequestMapping(value = "/group/{id}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
	public UserGroup getGroup(@PathVariable("id") String id) throws Exception {
		return userGroupService.find(id);
	}
}
