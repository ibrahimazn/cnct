/*
 *
 */
package com.appfiss.account.configuration;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * Zuul proxy filter for response to add custom headers like
 * x-auth-token,x-auth.
 */
public class AddResponseHeaderFilter extends ZuulFilter {
	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 999;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletResponse servletResponse = context.getResponse();
		servletResponse.addHeader("X-Auth-Token", UUID.randomUUID().toString());
		return null;
	}
}
