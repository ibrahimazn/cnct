package az.ancode.filemanager.connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import java.io.IOException;

/**
 * Fetches Remote HTTP URL information as text/object.
 *
 */
public class HttpFetcher {

    /** The URL for the REST server. */
    private String url;

    /**
     * Initializes the connection between server for the given URL. The same
     * connection will be used across all the other activities.
     *
     * @param url URL for the resource to connect.
     */
    public HttpFetcher(String url) {
        this.url = url;
    }

    /**
     * Request the given API to cloud stack and get response.
     *
     * @param <T> response type
     * @param t response object
     * @param clazz response class reference
     * @param responseCommand response command.
     * @return API response.
     * @throws ClientProtocolException client protocol exception
     * @throws IOException IO exception
     */
    public <T> T getApiResponse(T t, Class<T> clazz, String responseCommand)
            throws ClientProtocolException, IOException {
        try {
            //Form the url from the url and location
            String connectionURL = url;
            //Use the URL to connect, response will be a JSON string
            String response = "";
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();
            JsonParser parser = new JsonParser();
            response = Request.Get(connectionURL).execute().returnContent().asString();
            JsonObject resoponseJson = parser.parse(response).getAsJsonObject();
            String resoponseForGson = resoponseJson.get(responseCommand).toString();
            T listApiResponse = gson.fromJson(resoponseForGson, clazz);
            return listApiResponse;

        } catch (ClientProtocolException e) {
            throw new ClientProtocolException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Request the given API to cloud stack and post response.
     *
     * @param <T> response type
     * @param t response object
     * @param clazz response class reference
     * @param responseCommand response command.
     * @return API response.
     * @throws ClientProtocolException client protocol exception
     * @throws IOException IO exception
     */
    public <T> T postApiResponse(T t, Class<T> clazz, String responseCommand)
            throws ClientProtocolException, IOException {
        try {
            //Form the url from the url and location
            String connectionURL = url;
            //Use the URL to connect, response will be a JSON string
            String response = "";
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();
            JsonParser parser = new JsonParser();
            response = Request.Post(connectionURL).execute().returnContent().asString();
            JsonObject resoponseJson = parser.parse(response).getAsJsonObject();
            String resoponseForGson = resoponseJson.get(responseCommand).toString();
            T listApiResponse = gson.fromJson(resoponseForGson, clazz);
            return listApiResponse;

        } catch (ClientProtocolException e) {
            throw new ClientProtocolException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Fetch the data as a String.
     *
     * @param url URL to connect.
     * @return data or body of the response.
     * @throws ClientProtocolException when connection fails.
     * @throws IOException when connection fails.
     */
    public String fetchAsString(String url) throws ClientProtocolException, IOException {
        return Request.Get(url).execute().returnContent().toString();
    }

}
