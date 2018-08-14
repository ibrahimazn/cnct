package az.ldap;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;
import com.google.common.base.Optional;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.service.LdapSyncService;
import az.ldap.sync.util.RandomPasswordGenerator;
import az.ldap.zabbix.service.LoginHistoryService;
import az.ldap.zabbix.service.TokenService;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SimpleCORSFilter to allow cross domain call.
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {

    /** Constant for socket. */
    public static final String SOCKET = "/zabbix/ws";
    
    private static Cipher cipher;
    
    @Autowired
    private LdapSyncService ldapSync;

    /** Constant for OPTIONS. */
    public static final String OPTIONS = "OPTIONS";

    /** Logger attribute. */
    private static final Logger logger = LoggerFactory.getLogger(SimpleCORSFilter.class);

    /** SSL enabled status. */
    @Value(value = "${server.ssl.enabled}")
    public Boolean sslEnabled;
    
    @Value(value = "${ldap.enable}")
	private Boolean ldapEnable;
    
    @Value(value = "${aes.salt.secretKey}")
    private String secretKey;
    
    @Autowired
    private LoginHistoryService loginHistoryService;
    
    @Autowired
    private TokenService tokenService;
    
    /** Allow origin access control. */
    @Value(value = "${allow.origin.access.control}")
    public String allowOriginAccessControl;

    /**
     * Overriden method.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        if (!sslEnabled) {
            disableSSL();
        }
        response.setHeader("Access-Control-Allow-Origin", allowOriginAccessControl);
        if (!allowOriginAccessControl.equals("*")) {
			response.setHeader("Access-Control-Allow-Credentials", "true");
		}
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, Range, x-requested-with, x-auth-token, x-auth-panda-token, X-Request-Start, x-auth-username, x-email, x-auth-password, x-auth-remember, x-auth-login-token, x-auth-user-id, x-auth-ldap-user-id, x-auth-login-time, Content-Type, Accept, x-force-login, x-userid, x-password, x-auth-logintype");
        response.setHeader("Access-Control-Expose-Headers", "Rage, Content-Range");
        if(request.getRequestURI().contains(SOCKET)){
        	chain.doFilter(request, response);
        }
        else if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
        	HttpServletRequest httpRequest = asHttp(request);
            HttpServletResponse httpResponse = asHttp(response);
            Optional<String> username = Optional.fromNullable(httpRequest.getHeader("X-Auth-Username"));
            Optional<String> password = Optional.fromNullable(httpRequest.getHeader("X-Auth-Password"));
            Optional<String> loginToken = Optional.fromNullable(httpRequest.getHeader("X-Auth-Login-Token"));
            Optional<String> loginType = Optional.fromNullable(httpRequest.getHeader("X-Auth-Logintype"));
            String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
            try {
				if (resourcePath.contains("login")) {
					String strEncoded = Base64.getEncoder()
		                    .encodeToString(secretKey.getBytes(GenericConstants.CHARACTER_ENCODING));
		            byte[] decodedKey = Base64.getDecoder().decode(strEncoded);
		            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length,
		                    GenericConstants.ENCRYPT_ALGORITHM);
		            String currentPassword = decrypt(password.get(), originalKey);
					if (ldapEnable && loginType.get().equals("AD")) {
						currentPassword = new String(RandomPasswordGenerator.generatePswd(8, 16, 4, 2, 2));
						ldapSync.updatePassword(decrypt(username.get(), originalKey),
								currentPassword);
					}
					loginHistoryService.saveLoginDetails(decrypt(username.get(), originalKey), currentPassword, loginToken.get());
					GenericConstants.AUTH = tokenService.retrieve(loginToken.get());
				} else {
					if (tokenService.contains(loginToken.get())) {
						GenericConstants.AUTH = tokenService.retrieve(loginToken.get());
					} 
				}
                chain.doFilter(request, response);
            } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } 
        }
    }
    
    public static synchronized String decrypt(String plainText, SecretKey secretKey) throws Exception {
        cipher = Cipher.getInstance("AES");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(plainText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
    
    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    /**
     * DisableSSL.
     */
    private void disableSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.
                        X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            final java.security.cert.X509Certificate[] certs,
                            final String authType) {
                        }
                    public void checkServerTrusted(
                            final java.security.cert.X509Certificate[] certs,
                            final String authType) {
                    }
                }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(
                    sc.getSocketFactory());

        } catch (Exception ex) {
            logger.debug("Unable to disable ssl verification" + ex);
        }
    }


    /**
     * Initialization life cycle method.
     */
    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * Destroy life cycle method.
     */
    @Override
    public void destroy() {

    }
}
