/*
 *
 */
package com.ancode.service.account.configuration;

import java.io.IOException;

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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * SimpleCORSFilter to allow cross domain call.
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {

  /** Constant for socket. */
  public static final String SOCKET = "container-ws";

  /** Constant for recover user. */
  public static final String RECOVER = "recover";

  /** Constant for recover user. */
  public static final String RECOVER_PASSWORD = "recover/password";

  /** Constant for remember user. */
  public static final String REMEMBER_USER = "recover/user";

  /** Constant for recover user. */
  public static final String GENERAL_CONFIG = "generalconfiguration";

  /** Constant for resources. */
  public static final String RESOURCES = "resources";

  /** Constant for OPTIONS. */
  public static final String OPTIONS = "OPTIONS";

  /** Constant for HOME. */
  public static final String HOME = "home";

  /** Logger attribute. */
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCORSFilter.class);

  /** Allow origin access control. */
  // @Value(value = "${app.allow.origin.access.control}")
  private String allowOriginAccessControl;

  /**
   * Overriden method.
   */
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;
    allowOriginAccessControl = "*";

    response.setHeader("Access-Control-Allow-Origin", allowOriginAccessControl);
    if (!allowOriginAccessControl.equals("*")) {
      response.setHeader("Access-Control-Allow-Credentials", "true");
    }
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers",
        "Origin, Range, x-requested-with, Authorization, Authorized, x-auth-token, X-Request-Start, x-auth-username, x-email, x-auth-password, x-auth-remember, x-auth-login-token, x-auth-user-id, x-auth-login-time, Content-Type, Accept, x-force-login, x-userid, x-password ");
    response.setHeader("Access-Control-Expose-Headers", "Rage, Content-Range");
    if (request.getRequestURI().contains(SOCKET) || request.getRequestURI().contains("trace")) {
      LOGGER.debug("request filter applied");
    }
    if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(req, res);
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
