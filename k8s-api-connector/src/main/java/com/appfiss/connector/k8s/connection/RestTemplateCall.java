package com.appfiss.connector.k8s.connection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.springframework.core.ParameterizedTypeReference;

/**
 * Connection processor interface.
 *
 * @param <E> request object
 * @param <R> response class
 */
public interface RestTemplateCall<E,R> {

    /**
     * Make API request to kubectl.
     *
     * @param kubectlUrl connection object
     * @param request cloud stack request object.
     * @param responseClazz cloud stack response object.processor
     * @return API response from cloud stack
     * @throws URISyntaxException URI syntax exception
     * @throws InvalidKeyException invalid key exception
     * @throws NoSuchAlgorithmException algorithm exception
     * @throws Exception general exception
     * @throws IOException IO exception
     * @throws HttpException HTTP exception
     */
    R restCall(String kubectlUrl, String auth, E request, Class<R> responseClazz, String method) throws
        URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, HttpException, IOException, Exception;

    List<R> restListPostCall(String kubectlUrl, String auth, E request, ParameterizedTypeReference<List<R>> responseType) throws
    URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, HttpException, IOException, Exception;
}
