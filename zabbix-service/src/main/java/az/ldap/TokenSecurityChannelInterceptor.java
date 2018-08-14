package az.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.Dashboards;
import az.ldap.zabbix.entity.LoginHistory;
import az.ldap.zabbix.service.LoginHistoryService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TokenSecurityChannelInterceptor extends ChannelInterceptorAdapter {
	/** Logger constant. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenSecurityChannelInterceptor.class);

	private static final ThreadLocal<Stack<SecurityContext>> ORIGINAL_CONTEXT = new ThreadLocal<>();

	private final SecurityContext EMPTY_CONTEXT;
	
	private LoginHistoryService loginHistoryService;

	private final Authentication anonymous;

	public TokenSecurityChannelInterceptor(LoginHistoryService loginHistoryService) {
		this.EMPTY_CONTEXT = SecurityContextHolder.createEmptyContext();
		this.anonymous = new AnonymousAuthenticationToken("key", "anonymous",
				AuthorityUtils.createAuthorityList(new String[] { "ROLE_ANONYMOUS" }));
		this.loginHistoryService = loginHistoryService;
	}

	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		this.setup(message);
		return message;
	}

	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		this.cleanup();
	}

	private void setup(Message<?> message) {
		getAuthentication(message.getHeaders(), message);
	}

	private Authentication getAuthentication(MessageHeaders messageHeaders, Message<?> message) {
		Authentication authentication = this.anonymous;
        SimpMessageType simpMessageType = (SimpMessageType) messageHeaders.get(GenericConstants.SIMP_MESSAGE_TYPE);
        String simpSessionId = (String) messageHeaders.get(GenericConstants.SIMP_SESSION_ID);
        if (simpMessageType == SimpMessageType.CONNECT) {
        	Map<String, LinkedList> nativeHeaders = (Map<String, LinkedList>) messageHeaders.get(GenericConstants.NATIVE_HEADERS);
            if (nativeHeaders != null) {
                LinkedList login_token = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_LOGIN_TOKEN);
                LinkedList userType = nativeHeaders.get(GenericConstants.HEADER_X_TYPE);
                LinkedList department = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_DEPARTMENT_ID);
                LinkedList domain = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_DOMAIN_ID);
                LinkedList login_user_id = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_USER_ID);
            }
        } else if (simpMessageType == SimpMessageType.DISCONNECT){
        	try {
        		List<LoginHistory> logins = loginHistoryService.findBySessionId(simpSessionId);
        		if (logins.size() > 0) {
					for (LoginHistory login : logins) {
						login.setIsAlreadyLogin(false);
						login.setSessionId(null);
						login.setHostId(null);
						loginHistoryService.delete(login);
					}
				}
        	} catch (Exception e) {
				e.printStackTrace();
			}
        } else if (simpMessageType == SimpMessageType.MESSAGE) {
			try {
				ObjectMapper eventmapper = new ObjectMapper();
				String types = (String)  messageHeaders.get("simpDestination");
				if(types.contains("zabbix")) {
					Dashboards dashboard = eventmapper.readValue(new String((byte[]) message.getPayload()), Dashboards.class);
					if (dashboard.getUserId() != null && dashboard.getType().contains("host.disconnect")) {
						String hostIds = dashboard.getType().split("-")[1];
						LoginHistory loginHistory = loginHistoryService.findByLoginTokenAndSession(dashboard.getUserId(), hostIds, simpSessionId);
						if (loginHistory != null) {
							loginHistory.setHostId(null);
							loginHistoryService.save(loginHistory);
						}
					} else if (dashboard.getUserId() != null && dashboard.getType().contains("host.connect")) {
						String hostIds = dashboard.getType().split("-")[1];
						List<LoginHistory> logins = loginHistoryService.findBySessionIdAndUser(simpSessionId, dashboard.getUserId());
						if (logins.size() > 0) {
							for (LoginHistory loginHistory : logins) {
								LoginHistory login = loginHistoryService
										.findByLoginTokenAndSession(dashboard.getUserId(), hostIds, simpSessionId);
								if (login != null) {
									login.setHostId(hostIds);
									loginHistoryService.save(login);
								} else {
									loginHistory.setHostId(hostIds);
									loginHistoryService.save(loginHistory);
								}
							}
						} else {
							
						}
					} else if (dashboard.getUserId() != null && dashboard.getType().equals("zabbix.disconnect")) {
						List<LoginHistory> logins = loginHistoryService.findByUserId(dashboard.getUserId());
						if (logins.size() > 0) {
							for (LoginHistory loginHistory : logins) {
								loginHistory.setHostId(null);
								loginHistory.setIsAlreadyLogin(false);
								loginHistory.setSessionId(null);
								loginHistoryService.delete(loginHistory);
							}
						} 
					} else if (dashboard.getUserId() != null && dashboard.getType().equals("zabbix.connect")) {
						 Map<String, LinkedList> nativeHeaders = (Map<String, LinkedList>) messageHeaders.get(GenericConstants.NATIVE_HEADERS);
						 LinkedList login_token = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_LOGIN_TOKEN);
			             LinkedList userType = nativeHeaders.get(GenericConstants.HEADER_X_TYPE);
			             LinkedList department = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_DEPARTMENT_ID);
			             LinkedList domain = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_DOMAIN_ID);
			             LinkedList login_user_id = nativeHeaders.get(GenericConstants.HEADER_X_AUTH_USER_ID);
						 List<LoginHistory> logins = loginHistoryService.findByUserId(login_user_id.getFirst().toString());
    					 if (logins.size() > 0) {
							for (LoginHistory login : logins) {
								login.setLoginToken(login_token.getFirst().toString());
								login.setIsAlreadyLogin(true);
								login.setSessionId(simpSessionId);
								login.setUserType(userType.getFirst().toString());
								login.setUserId(login_user_id.getFirst().toString());
								login.setDomainId(domain.getFirst().toString());
								login.setDepartmentId(department.getFirst().toString());
								loginHistoryService.save(login);
							}
    					} else {
    						LoginHistory login = new LoginHistory();
    						login.setLoginToken(login_token.getFirst().toString());
    						login.setIsAlreadyLogin(true);
    						login.setSessionId(simpSessionId);
    						login.setUserType(userType.getFirst().toString());
    						login.setUserId(login_user_id.getFirst().toString());
    						login.setDomainId(domain.getFirst().toString());
							login.setDepartmentId(department.getFirst().toString());
    						loginHistoryService.save(login);
    					}
					}
				}
				LOGGER.debug("Websocket---Message--" + new String((byte[]) message.getPayload()) + " --WebsocketSess"
						+ simpSessionId);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				LOGGER.debug("Websocket---ERROR--Message--" + new String((byte[]) message.getPayload())
						+ " --WebsocketSess" + simpSessionId);
				e1.printStackTrace();
			}
		}
		return authentication;
	}

	private void cleanup() {

	}
}
