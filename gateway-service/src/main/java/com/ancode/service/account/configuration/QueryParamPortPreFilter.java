/*
 *
 */
package com.ancode.service.account.configuration;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * The Class QueryParamPortPreFilter.
 */
public class QueryParamPortPreFilter extends ZuulFilter {

  @Override
  public int filterOrder() {
    // run after PreDecorationFilter
    return 5 + 1;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = getCurrentContext();
    return ctx.getRequest().getParameter("port") != null;
  }

  @Override
  public Object run() {
    RequestContext ctx = getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    // put the serviceId in `RequestContext`
    String port = request.getParameter("port");
    try {
      URL url = UriComponentsBuilder.fromUri(ctx.getRouteHost().toURI()).port(new Integer(port)).build().toUri()
          .toURL();
      ctx.setRouteHost(url);
    } catch (Exception e) {
      ReflectionUtils.rethrowRuntimeException(e);
    }
    return null;
  }
}
