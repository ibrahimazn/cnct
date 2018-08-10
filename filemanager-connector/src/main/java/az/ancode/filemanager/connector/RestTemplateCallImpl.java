package az.ancode.filemanager.connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import az.ancode.filemanager.connector.upload.FileUploadRequest;

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
		String params = "";
		method.setHeader("Content-Type", "application/json");
		if (!methods.equals("file")) {
			params = populateQueryParams(request);
			StringEntity entity = new StringEntity(params, "UTF-8");
			LOGGER.info("Request" + params);
			LOGGER.info("URL" + kuberneteUrl);
			method.setEntity(entity);
		}
		if (methods.equals("post")) {
			return getPostResponse(auth, method, params, responseClazz);
		} else if (methods.equals("get")) {
			return getGetResponse(auth, method, params, responseClazz);
		} else {
			return getPostResponseWithFile(auth, method, request, responseClazz);
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
	 * Get response from the kubernete in our specified format also throws
	 * exception in case of error.
	 * 
	 * @param <T>
	 *
	 * @param method
	 *            Passes URL and connects it with kubernete.
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
			requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			requestHeaders.set("Kubernetes-Api-Key", auth);
			HttpEntity<String> request = new HttpEntity<String>(params, requestHeaders);
			String uri = method.getURI().toString();
			LOGGER.info("request " + request.toString());
			LOGGER.info("URI " + uri.toString());
			responseReference = restTemplate.exchange(uri, HttpMethod.POST, request, responseClazz);
			LOGGER.info("Response " + responseReference.getBody());
		} catch (HttpClientErrorException e) {
			throw new FilemanagerException(e.getMessage());
		} catch (Exception e) {
			throw new FilemanagerException(e.getMessage());
		}
		return responseReference.getBody();
	}

	private R getPostResponseWithFile(String auth, HttpPost method, E params, Class<R> responseClazz) throws Exception {
		ResponseEntity<R> responseReference = null;
		RestTemplate restTemplate = null;
		try {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setBufferRequestBody(false);
			restTemplate = new RestTemplate(requestFactory);
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			requestHeaders.set("Kubernetes-Api-Key", auth);
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			FileUploadRequest fileUpload = (FileUploadRequest) params;
			parameters.add("file", new MultipartFileResource(fileUpload.getFile()));
			parameters.add("uuid", fileUpload.getUuid());
			parameters.add("pvc", fileUpload.getPvc());
			parameters.add("path", fileUpload.getPath());
			String uri = method.getURI().toString();
			LOGGER.info("URI " + uri.toString());
			Map<String, Object> FeedBackStatus = new HashMap<String, Object>();
			HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
					parameters, requestHeaders);
			responseReference = restTemplate.exchange(uri, HttpMethod.POST, request, responseClazz);
			LOGGER.info("Response " + responseReference.getBody());
		} catch (HttpClientErrorException e) {
			throw new FilemanagerException(e.getMessage());
		} catch (Exception e) {
			throw new FilemanagerException(e.getMessage());
		}
		return responseReference.getBody();
	}

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
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
			throw new FilemanagerException(e.getMessage());
		} catch (Exception e) {
			throw new FilemanagerException(e.getMessage());
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
	public List<R> restListPostCall(String filemanagerUrl, String auth, E request,
			ParameterizedTypeReference<List<R>> responseType) throws URISyntaxException, InvalidKeyException,
			NoSuchAlgorithmException, HttpException, IOException, Exception {
		// TODO Auto-generated method stub
		this.responseType = responseType;
		HttpPost method = null;
		method = new HttpPost(filemanagerUrl);
		method.setHeader("Content-Type", "application/json");
		String params = populateQueryParams(request);
		StringEntity entity = new StringEntity(params, "UTF-8");
		method.setEntity(entity);
		return getListGetResponse(auth, filemanagerUrl, params, responseType);
	}

	public class MultipartFileResource extends ByteArrayResource {
		 
		private String filename;
	 
		public MultipartFileResource(MultipartFile multipartFile) throws IOException {
			super(multipartFile.getBytes());
			this.filename = multipartFile.getOriginalFilename();
		}
	 
		@Override
		public String getFilename() {
			return this.filename;
		}
	}

}
