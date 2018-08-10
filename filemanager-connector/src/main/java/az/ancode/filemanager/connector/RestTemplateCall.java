package az.ancode.filemanager.connector;

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
     * Make API request to filemanager.
     *
     * @param filemanagerUrl connection object
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
    R restCall(String filemanagerUrl, String auth, E request, Class<R> responseClazz, String method) throws
        URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, HttpException, IOException, Exception;

    List<R> restListPostCall(String filemanagerUrl, String auth, E request, ParameterizedTypeReference<List<R>> responseType) throws
    URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, HttpException, IOException, Exception;
}
