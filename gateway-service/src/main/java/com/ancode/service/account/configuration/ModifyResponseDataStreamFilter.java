/*
 *
 */
package com.ancode.service.account.configuration;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * The Class ModifyResponseDataStreamFilter.
 */
public class ModifyResponseDataStreamFilter extends ZuulFilter {
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
    RequestContext context = getCurrentContext();
    return context.getRequest().getParameter("service") != null;
  }

  @Override
  public Object run() {
    try {
      RequestContext context = getCurrentContext();
      InputStream stream = context.getResponseDataStream();
      String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
      body = "Modified via setResponseDataStream(): " + body;
      context.setResponseDataStream(new ByteArrayInputStream(body.getBytes("UTF-8")));
    } catch (IOException e) {
      rethrowRuntimeException(e);
    }
    return null;
  }
}
