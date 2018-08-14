/*
 *
 */
package com.appfiss.account.configuration;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.appfiss.account.entity.User;
import com.appfiss.account.service.UserService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;

/**
 * PrefixRequest Entity Filter.
 */
@Component
public class PrefixRequestEntityFilter extends ZuulFilter {

	/** The user service. */
	@Autowired
	private UserService userService;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 6;
	}

	@Override
	public boolean shouldFilter() {
		getCurrentContext();
		return true;
	}

	@Override
	public Object run() {
		try {
			RequestContext context = getCurrentContext();
			HttpServletRequest request = context.getRequest();
			System.out.println(request.getRequestURI());
			if (!request.getRequestURI().contains("deployment-ws")) {
				Authentication auth = authentication();
				User user = userService.findByUserNameAndIsActive(auth.getName(), 1);
				context.addZuulRequestHeader("userId", String.valueOf(user.getId()));
				context.addZuulRequestHeader("userName", String.valueOf(user.getUserName()));
			}
			InputStream in = (InputStream) context.get("requestEntity");
			if (in == null) {
				in = context.getRequest().getInputStream();
			}
			String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			final byte[] bytes = body.getBytes("UTF-8");
			context.setRequest(new HttpServletRequestWrapper(getCurrentContext().getRequest()) {
				@Override
				public ServletInputStream getInputStream() throws IOException {
					return new ServletInputStreamWrapper(bytes);
				}

				@Override
				public int getContentLength() {
					return bytes.length;
				}

				@Override
				public long getContentLengthLong() {
					return bytes.length;
				}
			});
		} catch (IOException e) {
			rethrowRuntimeException(e);
		}
		return null;
	}

	/**
	 * Authentication.
	 *
	 * @return the authentication
	 */
	private Authentication authentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}
}
