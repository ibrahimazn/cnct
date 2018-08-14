package az.ldap.zabbix.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.LoginHistory;
import az.ldap.zabbix.repository.LoginHistoryRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.authentication.AuthRequest;
import az.zabbix.connector.authentication.AuthResponse;
import az.zabbix.connector.authentication.LoginRequest;
import az.zabbix.connector.authentication.LoginResponse;

/**
 * Login history service implementation.
 *
 */
@Service
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

	/** alert lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<AuthRequest, AuthResponse> authRequest;

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
	public void delete(String id) throws Exception {
		loginRepo.delete(id);
	}

	@Override
	public LoginHistory find(String id) throws Exception {
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
		AuthResponse authResponse = authRequest.restCall(zabbixUrl, loginRequest(userName, password),
				AuthResponse.class);
		if (authResponse.getError() != null) {
			LOGGER.info("Zabbix ldap login error : "+ authResponse.getError());
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, authResponse.getError().get("data"));
		}
		LoginResponse loginRes = authResponse.getResult();
		if (loginRes != null) {
			tokenService.store(loginToken, loginRes.getSessionid());
			LOGGER.info("Zabbix ldap login response : "+ loginRes.getUserid() + "==" + loginRes.getSessionid() + "==" + loginRes.getUserip() + "==" + loginRes.getAlias());
		} 
		return new LoginHistory();
	}

	private AuthRequest loginRequest(String userName, String password) {
		LOGGER.info("Zabbix ldap login request : "+ userName + "==" + password);
		AuthRequest request = new AuthRequest();
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("user.login");
		LoginRequest login = new LoginRequest();
		login.setUser(userName);
		login.setPassword(password);
		login.setUserData(true);
		request.setParams(login);
		return request;
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
	public LoginHistory findByLoginTokenAndSession(String userId, String hostId, String sessionId) {
		return loginRepo.findByLoginToken(userId, hostId, sessionId);
	}

	@Override
	public List<LoginHistory> findBySessionId(String sessionId) {
		return loginRepo.findBySessionId(sessionId);
	}

	@Override
	public LoginHistory findByUser(String userId, String hostId) {
		return loginRepo.findByUserIdAndHost(userId, hostId);
	}

	@Override
	public List<LoginHistory> findBySessionIdAndUser(String sessionId, String userId) {
		return loginRepo.findBySessionIdAndUser(sessionId, userId);
	}

	@Override
	public List<LoginHistory> findByAllActiveHost() {
		return loginRepo.findByHost();
	}

	@Override
	public List<LoginHistory> findByAllActiveUser() {
		// TODO Auto-generated method stub
		return loginRepo.findByUserAndSessionId();
	}
}
