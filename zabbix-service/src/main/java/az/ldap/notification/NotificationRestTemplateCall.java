package az.ldap.notification;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.commons.httpclient.HttpException;
import org.springframework.core.ParameterizedTypeReference;

/**
 * Rest call interface.
 *
 * @param <E> request object
 * @param <R> response class
 */
public interface NotificationRestTemplateCall<E,R> {

    /**
     * Make API request to notification.
     *
     * @param notification connection object
     * @param request notification request object.
     * @param responseClazz notification response object.processor
     * @return API response from notification
     * @throws URISyntaxException URI syntax exception
     * @throws InvalidKeyException invalid key exception
     * @throws NoSuchAlgorithmException algorithm exception
     * @throws Exception general exception
     * @throws IOException IO exception
     * @throws HttpException HTTP exception
     */
    R restCall(String notificationUrl, E request, String methods, Class<R> responseClazz, String id) throws
        URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, HttpException, IOException, Exception;

    List<R> restListPostCall(String notificationUrl, E request, String methods, ParameterizedTypeReference<List<R>> responseType) throws
    URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, HttpException, IOException, Exception;
}
