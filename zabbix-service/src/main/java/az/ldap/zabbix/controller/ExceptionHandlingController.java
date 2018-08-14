package az.ldap.zabbix.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import az.ldap.sync.util.CustomGenericException;
import az.ldap.sync.util.Errors;

public class ExceptionHandlingController {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ResponseBody
	Errors handleException(CustomGenericException ex) {
		Errors errors = new Errors();
		errors.addGlobalError(ex.getErrorMsg());
		return errors;
	}
}
