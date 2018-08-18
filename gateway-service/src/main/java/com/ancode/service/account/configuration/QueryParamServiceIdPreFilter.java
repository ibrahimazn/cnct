/*
 *
 */
package com.ancode.service.account.configuration;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * The Class QueryParamServiceIdPreFilter.
 */
public class QueryParamServiceIdPreFilter extends ZuulFilter {

  @Override
  public int filterOrder() {
    // run before PreDecorationFilter
    return 5 - 1;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = getCurrentContext();
    return ctx.getRequest().getParameter("service") != null;
  }

  @Override
  public Object run() {
    RequestContext ctx = getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    // put the serviceId in `RequestContext`
    ctx.put("serviceId", request.getParameter("service"));
    return null;
  }
}
