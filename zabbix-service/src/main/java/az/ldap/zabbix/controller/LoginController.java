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
import az.ldap.zabbix.entity.Graph;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.LoginHistory;
import az.ldap.zabbix.service.GraphService;
import az.ldap.zabbix.service.LoginHistoryService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/login")
public class LoginController extends CRUDController<LoginHistory> implements ApiController {

	@Autowired
	private LoginHistoryService loginHistoryService;
	
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String setFavorite(@RequestHeader("X-Auth-Username") String username, @RequestHeader("X-Auth-Password") String password, @RequestHeader("X-Auth-Login-Token") String token) throws Exception {
        return "authenticated";
    }
}
