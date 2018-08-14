package az.ldap.notification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;

/**
 * HTTP Request to given server.
 *
 * @param <E>
 *            request object
 * @param <R>
 *            response class
 */
@SuppressWarnings("deprecation")
@Component
public class NotificationRestTemplateCallImpl<E, R> implements NotificationRestTemplateCall<E, R> {

	/** Response class reference. */
	private Class<R> responseClazz;

	private ParameterizedTypeReference<List<R>> responseType;

	/**
	 * Make request to notification.
	 *
	 * @param notification
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
	@Override
	public R restCall(String notificationUrl, E request, String methods, Class<R> responseClazz, String id)
			throws HttpException, IOException, Exception {
		this.responseClazz = responseClazz;
		HttpPost method = null;
		method = new HttpPost(notificationUrl);
		method.setHeader("Content-Type", "application/json");
		String params = populateQueryParams(request);
		StringEntity entity = new StringEntity(params, "UTF-8");
		method.setEntity(entity);
		if (methods.equalsIgnoreCase("post")) {
			return getResponse(method, params, responseClazz);
		} else if(methods.equalsIgnoreCase("put")) {
			return getPutResponse(method, params, responseClazz, id);
		} else {
			return getGetResponse(notificationUrl, params, responseClazz);
		}
	}

	/**
	 * Make request to notification.
	 *
	 * @param notification
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
	@Override
	public List<R> restListPostCall(String notificationUrl, E request, String methods, ParameterizedTypeReference<List<R>> responseType)
			throws HttpException, IOException, Exception {
		this.responseType = responseType;
		HttpPost method = null;
		method = new HttpPost(notificationUrl);
		method.setHeader("Content-Type", "application/json");
		String params = populateQueryParams(request);
		StringEntity entity = new StringEntity(params, "UTF-8");
		method.setEntity(entity);
		if (methods.equalsIgnoreCase("post")) {
			return getListPostResponse(method, params, responseType);
		} else {
			return getListGetResponse(notificationUrl, params, responseType);
		}
	}

	/**
	 * Get query parameter with signature.
	 *
	 * @param notification
	 *            connection configuration
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
	 * Get response from the notification Connector in our specified format also
	 * throws exception in case of error.
	 * 
	 * @param <T>
	 *
	 * @param method
	 *            Passes final URL and connects it with cloud stack.
	 * @param responseName
	 *            response name
	 * @return response in JSON
	 * @throws Exception
	 *             if error
	 */

	private R getResponse(HttpPost method, String params, Class<R> responseClazz) throws Exception {
		R responseReference = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<String>(params, requestHeaders);
			String uri = method.getURI().toString();
			responseReference = restTemplate.postForObject(uri, request, responseClazz);
			/*
			 * String response = null; HttpResponse responses =
			 * client.execute(method); BufferedReader rd = new
			 * BufferedReader(new
			 * InputStreamReader(responses.getEntity().getContent()));
			 * StringBuffer result = new StringBuffer(); String line = ""; while
			 * ((line = rd.readLine()) != null) { result.append(line); }
			 * response = result.toString(); if (response.equals("")) {
			 * System.out.println(method.getURI().toString()+
			 * params+"invalid url"); Gson gson = new GsonBuilder().create();
			 * return responseClazz.newInstance(); } method.releaseConnection();
			 * Gson gson = new GsonBuilder().create(); JsonParser parser = new
			 * JsonParser(); JsonObject resoponseJson =
			 * parser.parse(response).getAsJsonObject(); // if
			 * (resoponseJson.get(responseName) != null) { String
			 * resoponseForGson = resoponseJson.toString(); responseReference =
			 * gson.fromJson(resoponseForGson, responseClazz); //}
			 */ } catch (HttpClientErrorException e) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, e.getMessage());
		} catch (HttpStatusCodeException e) {
			if (e.getMessage().contains("501")) {
				String responseString = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				CustomError result = mapper.readValue(responseString, CustomError.class);
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, result.getGlobalError().get(0));
			}
			if (e.getMessage().contains("500")) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						"Internal server error, please contact administrator");
			}
		}

		return responseReference;
	}

	private List<R> getListPostResponse(HttpPost method, String params,
			ParameterizedTypeReference<List<R>> responseType) throws Exception {
		ResponseEntity<List<R>> responseReference = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<String>(params, requestHeaders);
			String uri = method.getURI().toString();
			responseReference = restTemplate.exchange(uri, HttpMethod.POST, request, responseType);
			/*
			 * String response = null; HttpResponse responses =
			 * client.execute(method); BufferedReader rd = new
			 * BufferedReader(new
			 * InputStreamReader(responses.getEntity().getContent()));
			 * StringBuffer result = new StringBuffer(); String line = ""; while
			 * ((line = rd.readLine()) != null) { result.append(line); }
			 * response = result.toString(); if (response.equals("")) {
			 * System.out.println(method.getURI().toString()+
			 * params+"invalid url"); Gson gson = new GsonBuilder().create();
			 * return responseClazz.newInstance(); } method.releaseConnection();
			 * Gson gson = new GsonBuilder().create(); JsonParser parser = new
			 * JsonParser(); JsonObject resoponseJson =
			 * parser.parse(response).getAsJsonObject(); // if
			 * (resoponseJson.get(responseName) != null) { String
			 * resoponseForGson = resoponseJson.toString(); responseReference =
			 * gson.fromJson(resoponseForGson, responseClazz); //}
			 */ } catch (HttpClientErrorException e) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, e.getMessage());
		} catch (HttpStatusCodeException e) {
			if (e.getMessage().contains("501")) {
				String responseString = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				CustomError result = mapper.readValue(responseString, CustomError.class);
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, result.getGlobalError().get(0));
			}
			if (e.getMessage().contains("500")) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						"Internal server error, please contact administrator");
			}
		}

		return responseReference.getBody();
	}

	private R getPutResponse(HttpPost method, String params, Class<R> responseClazz, String id) throws Exception {
		ResponseEntity<R> responseReference = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			Map<String, String> param = new HashMap<String, String>();
		    param.put("id",id);
			HttpEntity<String> request = new HttpEntity<String>(params, requestHeaders);
			String uri = method.getURI().toString();
			responseReference = restTemplate.exchange(uri+"/{id}", HttpMethod.PUT, request, responseClazz, param);
			/*
			 * String response = null; HttpResponse responses =
			 * client.execute(method); BufferedReader rd = new
			 * BufferedReader(new
			 * InputStreamReader(responses.getEntity().getContent()));
			 * StringBuffer result = new StringBuffer(); String line = ""; while
			 * ((line = rd.readLine()) != null) { result.append(line); }
			 * response = result.toString(); if (response.equals("")) {
			 * System.out.println(method.getURI().toString()+
			 * params+"invalid url"); Gson gson = new GsonBuilder().create();
			 * return responseClazz.newInstance(); } method.releaseConnection();
			 * Gson gson = new GsonBuilder().create(); JsonParser parser = new
			 * JsonParser(); JsonObject resoponseJson =
			 * parser.parse(response).getAsJsonObject(); // if
			 * (resoponseJson.get(responseName) != null) { String
			 * resoponseForGson = resoponseJson.toString(); responseReference =
			 * gson.fromJson(resoponseForGson, responseClazz); //}
			 */ } catch (HttpClientErrorException e) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, e.getMessage());
		} catch (HttpStatusCodeException e) {
			if (e.getMessage().contains("501")) {
				String responseString = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				CustomError result = mapper.readValue(responseString, CustomError.class);
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, result.getGlobalError().get(0));
			}
			if (e.getMessage().contains("500")) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						"Internal server error, please contact administrator");
			}
		}

		return responseReference.getBody();
	}

	private R getGetResponse(String uri, String params, Class<R> responseClazz) throws Exception {
		ResponseEntity<R> responseReference = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<String>(requestHeaders);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
			UriComponents uriComponents = builder.build();
			responseReference = restTemplate.exchange(uri, HttpMethod.GET, request, responseClazz);
			/*
			 * String response = null; HttpResponse responses =
			 * client.execute(method); BufferedReader rd = new
			 * BufferedReader(new
			 * InputStreamReader(responses.getEntity().getContent()));
			 * StringBuffer result = new StringBuffer(); String line = ""; while
			 * ((line = rd.readLine()) != null) { result.append(line); }
			 * response = result.toString(); if (response.equals("")) {
			 * System.out.println(method.getURI().toString()+
			 * params+"invalid url"); Gson gson = new GsonBuilder().create();
			 * return responseClazz.newInstance(); } method.releaseConnection();
			 * Gson gson = new GsonBuilder().create(); JsonParser parser = new
			 * JsonParser(); JsonObject resoponseJson =
			 * parser.parse(response).getAsJsonObject(); // if
			 * (resoponseJson.get(responseName) != null) { String
			 * resoponseForGson = resoponseJson.toString(); responseReference =
			 * gson.fromJson(resoponseForGson, responseClazz); //}
			 */ } catch (HttpClientErrorException e) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, e.getMessage());
		} catch (HttpStatusCodeException e) {
			if (e.getMessage().contains("501")) {
				String responseString = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				CustomError result = mapper.readValue(responseString, CustomError.class);
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, result.getGlobalError().get(0));
			}
			if (e.getMessage().contains("500")) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						"Internal server error, please contact administrator");
			}
		}

		return responseReference.getBody();
	}

	private List<R> getListGetResponse(String uri, String params, ParameterizedTypeReference<List<R>> responseType) throws Exception {
		ResponseEntity<List<R>> responseReference = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<String>(requestHeaders);
			responseReference = restTemplate.exchange(uri, HttpMethod.GET, request, responseType);
			/*
			 * String response = null; HttpResponse responses =
			 * client.execute(method); BufferedReader rd = new
			 * BufferedReader(new
			 * InputStreamReader(responses.getEntity().getContent()));
			 * StringBuffer result = new StringBuffer(); String line = ""; while
			 * ((line = rd.readLine()) != null) { result.append(line); }
			 * response = result.toString(); if (response.equals("")) {
			 * System.out.println(method.getURI().toString()+
			 * params+"invalid url"); Gson gson = new GsonBuilder().create();
			 * return responseClazz.newInstance(); } method.releaseConnection();
			 * Gson gson = new GsonBuilder().create(); JsonParser parser = new
			 * JsonParser(); JsonObject resoponseJson =
			 * parser.parse(response).getAsJsonObject(); // if
			 * (resoponseJson.get(responseName) != null) { String
			 * resoponseForGson = resoponseJson.toString(); responseReference =
			 * gson.fromJson(resoponseForGson, responseClazz); //}
			 */ 
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, e.getMessage());
		} catch (HttpStatusCodeException e) {
			if (e.getMessage().contains("501")) {
				String responseString = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				CustomError result = mapper.readValue(responseString, CustomError.class);
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, result.getGlobalError().get(0));
			}
			if (e.getMessage().contains("500")) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						"Internal server error, please contact administrator");
			}
		}

		return responseReference.getBody();
	}

}
