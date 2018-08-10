package az.openfaas.connector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.httpclient.HttpException;

/**
 * Connection processor interface.
 *
 * @param <E> request object
 * @param <R> response class
 */
public interface RestCall<E,R> {

    /**
     * Make API request to openFaasUrl.
     *
     * @param openFaasUrl connection object
     * @param request openfaas stack request object.
     * @param responseClazz cloud stack response object.processor.
     * @param user userName.
     * @return API response from cloud stack
     * @throws URISyntaxException URI syntax exception
     * @throws InvalidKeyException invalid key exception
     * @throws NoSuchAlgorithmException algorithm exception
     * @throws Exception general exception
     * @throws IOException IO exception
     * @throws HttpException HTTP exception
     */
    R restCall(String openFaasUrl, E request, Class<R> responseClazz, String user, String methodName) throws
        URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, HttpException, IOException, Exception;

}
