package az.ldap.zabbix.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.service.LdapSyncService;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.User;
import az.ldap.zabbix.service.PagingAndSorting;
import az.ldap.zabbix.service.UserService;

@RestController
@RequestMapping("/zabbix/api/user")
public class UserController extends CRUDController<User> implements ApiController {

	@Autowired
	private UserService userService;
	
	@Override
	public User create(@RequestBody User user) throws Exception {
		return userService.save(user);
	}

	@Override
	public User update(@RequestBody User user, @PathVariable(PATH_ID) String id) throws Exception {
		return userService.update(user);
	}

	@Override
	public void delete(@PathVariable(PATH_ID) String id) throws Exception {
		userService.delete(id);
	}

	@Override
	public User read(@PathVariable(PATH_ID) String id) throws Exception {
		return userService.find(id);
	}

	@Override
	public List<User> list(@RequestParam(required = false) String sortBy, @RequestHeader("Range") String range, @RequestParam(required = false) Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, User.class);
		Page<User> pageResponse = userService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUserFromZabbix() throws Exception {
        return userService.SyncUser();
    }
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUserByGroupId(@RequestParam String groupId) throws Exception {
        return userService.findByGroupId(groupId);
    }
}
