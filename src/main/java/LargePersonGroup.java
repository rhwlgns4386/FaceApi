import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;

public class LargePersonGroup {

    private final String endpoint = Key.getEndpoint();
    private final String subscriptionKey = Key.getKey();

    private final String largePersonGroupId;
    private final String name;
    private String userData = "";
    private String recognitionModel = "";

    public static LargePersonGroup builder(String largePersonGroupId, String name) {
        return new LargePersonGroup(largePersonGroupId, name);
    }

    protected LargePersonGroup(String largePersonGroupId, String name) {
        super();
        this.largePersonGroupId = largePersonGroupId;
        this.name = name;
    }

    public LargePersonGroup userData(String userData) {
        this.userData = userData;
        return this;
    }

    public LargePersonGroup recognitionModel(String recognitionModel) {
        this.recognitionModel = recognitionModel;
        return this;
    }

    public String getId() {
        return this.largePersonGroupId;
    }

    public LargePersonGroup build() {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/largepersongroups/" + this.largePersonGroupId);

            URI uri = builder.build();
            HttpPut request = new HttpPut(uri);

            // Request headers
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.name);
            jsonObject.put("userData", this.userData);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            // 200
            if (response.getStatusLine().getStatusCode() == 200) {
                return this;
            } else {    // 40x
                throw new APIException(entity); // 아래 catch문에서 잡힐 것임
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return this;
    }

    public String addFace(String personId, String imageUrl) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/persongroups/" +  this.largePersonGroupId + "/persons/" + personId + "/persistedFaces");

            URI uri = builder.build();
            HttpPut request = new HttpPut(uri);

            // Request headers
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", imageUrl);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString;
                jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    JSONObject jo = new JSONObject(jsonArray.get(0));
                    return jo.getString("persistedFaceId");
                } else if (jsonString.charAt(0) == '{') {
                    JSONObject jo = new JSONObject(jsonString);
                    return jo.getString("persistedFaceId");
                } else {
                    return jsonString;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "error";
    }

}
