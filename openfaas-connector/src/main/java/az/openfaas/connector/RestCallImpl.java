package az.openfaas.connector;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HTTP Request to given server.
 *
 * @param <E>
 *          request object
 * @param <R>
 *          response class
 */
public class RestCallImpl<E, R> implements RestCall<E, R> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestCallImpl.class);

  /** Response class reference. */
  private Class<R> responseClazz;

  @Value("#{environment.SSl_VERIFY ?: true}")
  private Boolean sslVerify;

  /**
   * Make request to openFaasUrl.
   *
   * @param openFaasUrl
   *          config
   * @param request
   *          object
   * @param responseClazz
   *          response class
   * @return query parameter
   * @throws HttpException
   *           if error
   * @throws IOException
   *           if error
   * @throws Exception
   *           if error
   */
  public R restCall(String openFaasUrl, E request, Class<R> responseClazz, String user, String methodName)
      throws HttpException, IOException, Exception {
    this.responseClazz = responseClazz;
    HttpPost method = null;
    method = new HttpPost(openFaasUrl);
    method.setHeader("Content-Type", "application/json");
    String params = populateQueryParams(request);
    StringEntity entity = new StringEntity(params, "UTF-8");
    LOGGER.info("Request" + params);
    LOGGER.info("URL" + openFaasUrl);
    method.setEntity(entity);
    if (methodName.equalsIgnoreCase("post")) {
      return getPostResponse(method, params, responseClazz, user);
    } else if (methodName.equalsIgnoreCase("delete")) {
      return getDeleteResponse(method, params, responseClazz, user);
    }
    return null;
  }

  /**
   * Get query parameter with signature.
   *
   * @param request
   *          object
   * @return query parameter
   * @throws Exception
   *           if error
   */
  public String populateQueryParams(E request) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    String dtoAsString = mapper.writeValueAsString(request);
    return dtoAsString;
  }

  private R getDeleteResponse(HttpPost method, String params, Class<R> responseClazz, String user) throws Exception {
    ResponseEntity<R> responseReference = null;
    RestTemplate restTemplate = null;
    try {
      // System.out.println(sslVerify);
      restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
      restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON);
      requestHeaders.set("X_User", user);
      HttpEntity<String> request = new HttpEntity<String>(params, requestHeaders);
      String uri = method.getURI().toString();
      LOGGER.info("request " + request.toString());
      LOGGER.info("URI " + uri.toString());
      responseReference = restTemplate.exchange(uri, HttpMethod.DELETE, request, responseClazz);
      LOGGER.info("Response " + responseReference.getBody());
    } catch (HttpClientErrorException e) {
      throw new OpenFaasException(e.getMessage());
    } catch (Exception e) {
      throw new OpenFaasException(e.getMessage());
    }
    return responseReference.getBody();
  }

  /**
   * Get response from the OpenFaas in our specified format also throws
   * exception in case of error.
   * 
   * @param <T>
   *
   * @param method
   *          Passes URL and connects it with OpenFaas.
   * @param responseName
   *          response name
   * @return response in JSON
   * @throws Exception
   *           if error
   */
  private R getPostResponse(HttpPost method, String params, Class<R> responseClazz, String user) throws Exception {
    ResponseEntity<R> responseReference = null;
    RestTemplate restTemplate = null;
    try {
      // System.out.println(sslVerify);
      restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
      restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON);
      requestHeaders.set("X_User", user);
      HttpEntity<String> request = new HttpEntity<String>(params, requestHeaders);
      String uri = method.getURI().toString();
      LOGGER.info("request " + request.toString());
      LOGGER.info("URI " + uri.toString());
      responseReference = restTemplate.exchange(uri, HttpMethod.POST, request, responseClazz);
      LOGGER.info("Response " + responseReference.getBody());
    } catch (HttpClientErrorException e) {
      throw new OpenFaasException(e.getMessage());
    } catch (Exception e) {
      throw new OpenFaasException(e.getMessage());
    }
    return responseReference.getBody();
  }

}
