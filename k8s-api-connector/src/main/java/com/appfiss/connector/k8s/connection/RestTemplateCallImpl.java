package com.appfiss.connector.k8s.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.appfiss.connector.k8s.exception.CustomError;
import com.appfiss.connector.k8s.exception.CustomGenericException;
import com.appfiss.connector.k8s.exception.KubeCtlException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * HTTP Request to given server.
 *
 * @param <E>
 *            request object
 * @param <R>
 *            response class
 */
public class RestTemplateCallImpl<E, R> implements RestTemplateCall<E, R> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateCallImpl.class);

	/** Response class reference. */
	private Class<R> responseClazz;

	private ParameterizedTypeReference<List<R>> responseType;

	@Value("#{environment.SSl_VERIFY ?: true}")
	private Boolean sslVerify;

	/** Constant for generic exception status code. */
	public static final String NOT_IMPLEMENTED = "501";

	/**
	 * Make request to kubernete.
	 *
	 * @param kubernete
	 *            config
	 * @param request
	 *            object
	 * @param responseClazz
	 *            response class
	 * @return query parameter
	 * @throws HttpException
	 *             if error
	 * @throws IOException
	 *             if error
	 * @throws Exception
	 *             if error
	 */
	public R restCall(String kuberneteUrl, String auth, E request, Class<R> responseClazz, String methods)
			throws HttpException, IOException, Exception {
		this.responseClazz = responseClazz;
		HttpPost method = null;
		method = new HttpPost(kuberneteUrl);
		method.setHeader("Content-Type", "application/json");
		String params = populateQueryParams(request);
		StringEntity entity = new StringEntity(params, "UTF-8");
		LOGGER.info("Request" + params);
		LOGGER.info("URL" + kuberneteUrl);
		method.setEntity(entity);
		if (methods.equals("post")) {
			return getPostResponse(auth, method, params, responseClazz);
		} else {
			return getGetResponse(auth, method, params, responseClazz);
		}
	}

	/**
	 * Get query parameter with signature.
	 *
	 * @param request
	 *            object
	 * @return query parameter
	 * @throws Exception
	 *             if error
	 */
	public String populateQueryParams(E request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String dtoAsString = mapper.writeValueAsString(request);
		return dtoAsString;
	}

	/**
	 * Get response from the kubernetes in our specified format also throws
	 * exception in case of error.
	 * 
	 * @param <T>
	 *
	 * @param method
	 *            Passes URL and connects it with kubernetes.
	 * @param responseName
	 *            response name
	 * @return response in JSON
	 * @throws Exception
	 *             if error
	 */
	private R getPostResponse(String auth, HttpPost method, String params, Class<R> responseClazz) throws Exception {
		ResponseEntity<R> responseReference = null;
		RestTemplate restTemplate = null;
		try {
			restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestHeaders.set("Kubernetes-Api-Key", auth);
			HttpEntity<String> request = new HttpEntity<String>(params, requestHeaders);
			String uri = method.getURI().toString();
			LOGGER.info("request " + request.toString());
			LOGGER.info("URI " + uri.toString());
			responseReference = restTemplate.exchange(uri, HttpMethod.POST, request, responseClazz);
			String response = responseReference.getBody().toString();
			ObjectMapper mapperObj = new ObjectMapper();
			String jsonStr = mapperObj.writeValueAsString(responseReference.getBody());
			JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			if (jsonObj.get("status").getAsString().equals("error")) {
				throw new KubeCtlException(jsonObj.get("message").getAsString());
			}

			LOGGER.info("Response " + responseReference.getBody());
		} catch (HttpClientErrorException e) {
			throw new KubeCtlException(e.getMessage());
		} catch (RestClientException e) {
			String errorMessage = e.getMessage();
			if (e.getRootCause() instanceof SocketTimeoutException) {
				errorMessage = "Unable to reach the kubectl agent. Please contact administrator";
			} else if (e.getRootCause() instanceof ConnectTimeoutException) {
				errorMessage = "Unable to reach the kubectl agent. Please contact administrator";
			} else if (e.getRootCause() instanceof ConnectException) {
				errorMessage = "Unable to reach the kubectl agent. Please contact administrator";
			}
			throw new RestClientException(errorMessage);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			throw new Exception(e.getMessage());
		}
		return responseReference.getBody();
	}

	private R getGetResponse(String auth, HttpPost method, String params, Class<R> responseClazz) throws Exception {
		ResponseEntity<R> responseReference = null;
		RestTemplate restTemplate = null;
		try {
			restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.set("Kubernetes-Api-Key", auth);
			HttpEntity<String> request = new HttpEntity<String>(requestHeaders);
			String uri = method.getURI().toString();
			LOGGER.info("request " + request.toString());
			LOGGER.info("URI " + uri.toString());
			responseReference = restTemplate.exchange(uri, HttpMethod.GET, request, responseClazz);
			LOGGER.info("Response " + responseReference.getBody());
		} catch (HttpClientErrorException e) {
			throw new KubeCtlException(e.getMessage());
		} catch (Exception e) {
			throw new KubeCtlException(e.getMessage());
		}
		return responseReference.getBody();
	}

	private List<R> getListGetResponse(String auth, String uri, String params,
			ParameterizedTypeReference<List<R>> responseType) throws Exception {
		ResponseEntity<List<R>> responseReference = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			requestHeaders.set("Kubernetes-Api-Key", auth);
			HttpEntity<String> request = new HttpEntity<String>(requestHeaders);
			responseReference = restTemplate.exchange(uri, HttpMethod.GET, request, responseType);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			throw new CustomGenericException(NOT_IMPLEMENTED, e.getMessage());
		} catch (HttpStatusCodeException e) {
			if (e.getMessage().contains("501")) {
				String responseString = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				CustomError result = mapper.readValue(responseString, CustomError.class);
				throw new CustomGenericException(NOT_IMPLEMENTED, result.getGlobalError().get(0));
			}
			if (e.getMessage().contains("500")) {
				throw new CustomGenericException(NOT_IMPLEMENTED,
						"Internal server error, please contact administrator");
			}
		}
		return responseReference.getBody();
	}

	@Override
	public List<R> restListPostCall(String kubectlUrl, String auth, E request,
			ParameterizedTypeReference<List<R>> responseType) throws URISyntaxException, InvalidKeyException,
			NoSuchAlgorithmException, HttpException, IOException, Exception {
		// TODO Auto-generated method stub
		this.responseType = responseType;
		HttpPost method = null;
		method = new HttpPost(kubectlUrl);
		method.setHeader("Content-Type", "application/json");
		String params = populateQueryParams(request);
		StringEntity entity = new StringEntity(params, "UTF-8");
		method.setEntity(entity);
		return getListGetResponse(auth, kubectlUrl, params, responseType);
	}

}
