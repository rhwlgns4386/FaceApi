import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpRequestFacade {

    public enum HttpRequestMethod {GET,POST,PUT}

    private String resource;
    private HttpClient httpClient;
    private static final String subscriptionKey = Key.getKey();
    private static final String endpoint = Key.getEndpoint();

    public HttpRequestFacade(String resource) {
        this.resource = resource;
        httpClient=HttpClientBuilder.create().build();
    }

    public HttpResponse getHttpResponse(URIBuilder uriBuilder)  {
        try {
            URIBuilder builder = uriBuilder;


            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            HttpResponse response = httpClient.execute(request);
            return response;
        }catch (URISyntaxException |IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public HttpResponse getHttpResponse(HttpRequestMethod httpRequestType, URIBuilder uriBuilder, JSONObject jsonObject) {
        try {
        URIBuilder builder = uriBuilder;

        URI uri = builder.build();
        HttpEntityEnclosingRequestBase request;
        if(httpRequestType.equals(HttpRequestMethod.POST)){
            request= new HttpPost(uri);;
        }
        else {
            request=new HttpPut(uri);
        }

        // Request headers.
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        // Request body.
        if(jsonObject!=null){
            StringEntity reqEntity = new StringEntity(jsonObject.toString());
            request.setEntity(reqEntity);
        }

        // Execute the REST API call and get the response entity.
        HttpResponse response =  httpClient.execute(request);
        return response;
        } catch (URISyntaxException |IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public URIBuilder getUriBuilder(String path) throws URISyntaxException {
        return new URIBuilder(endpoint + resource + path);
    }
}