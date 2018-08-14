package com.appfiss.account.util;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RestErrorHandler implements ResponseErrorHandler {
  private static final Logger log = LoggerFactory.getLogger(RestErrorHandler.class);
  
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return RestUtil.isError(response.getStatusCode());
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());    
  }

}
