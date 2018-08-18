package com.appfiss.account.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import com.appfiss.account.dao.LoginHistoryRepository;
import com.appfiss.account.entity.LoginHistory;
import com.appfiss.account.entity.User;
import com.appfiss.account.security.TokenService;
import com.appfiss.account.util.PagingAndSorting;

/**
 * Login history service implementation.
 *
 */
public class LoginHistoryServiceImpl implements LoginHistoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginHistoryServiceImpl.class);

	/** Login History repository reference. */
	@Autowired
	private LoginHistoryRepository loginRepo;
	
	@Autowired
	private TokenService tokenService;

	@Value(value = "${zabbix.proxy}")
	private String zabbixUrl;

	/** Back end admin. */
	public static final String BACKEND_ADMIN = "BACKEND_ADMIN";

	/** Root domain. */
	public static final String ROOT = "ROOT";

	/** Time zone. */
	public static final String UTC = "UTC";

	/** Cookie time out. */
	public static final String COOKIE_TIME_OUT = "COOKIE_TIME_OUT";


	@Override
	public LoginHistory save(LoginHistory loginHistory) throws Exception {
		return loginRepo.save(loginHistory);
	}

	@Override
	public LoginHistory update(LoginHistory loginHistory) throws Exception {
		return loginRepo.save(loginHistory);
	}

	@Override
	public void delete(LoginHistory loginHistory) throws Exception {
		loginRepo.delete(loginHistory);
	}

	@Override
	public void delete(Long id) throws Exception {
		loginRepo.delete(id);
	}

	@Override
	public LoginHistory find(Long id) throws Exception {
		return loginRepo.findOne(id);
	}

	@Override
	public Page<LoginHistory> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return loginRepo.findAll(pagingAndSorting.toPageRequest());
	}

	@Override
	public List<LoginHistory> findAll() throws Exception {
		return (List<LoginHistory>) loginRepo.findAll();
	}

	@Override
	public LoginHistory saveLoginDetails(String userName, String password, String loginToken) throws Exception {
		
			tokenService.store(loginToken, loginToken);
			LOGGER.info("ldap login response : "+ loginToken + "==" );
		return new LoginHistory();
	}

	@Override
	public List<LoginHistory> findByUserIdAndAlreadyLogin(String userId, Boolean isAlreadyLogin) {
		return loginRepo.findByUserIdAndAlreadyLogin(userId, isAlreadyLogin);
	}

	@Override
	public List<LoginHistory> findByUserId(String userId) {
		return loginRepo.findByUserId(userId);
	}

	@Override
	public List<LoginHistory> findBySessionId(String sessionId) {
		return loginRepo.findBySessionId(sessionId);
	}

	@Override
	public List<LoginHistory> findBySessionIdAndUser(String sessionId, String userId) {
		return loginRepo.findBySessionIdAndUser(sessionId, userId);
	}

}
