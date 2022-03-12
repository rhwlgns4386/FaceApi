import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class LargeFaceList {
    private final String endpoint = Key.getEndpoint();
    private final String subscriptionKey = Key.getKey();

    private String id;
    private String name;
    private String userData = "";
    private String recognitionModel = "";

    protected LargeFaceList(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public LargeFaceList userDate(String userData) {
        this.userData = userData;
        return this;
    }

    public LargeFaceList recognitionModel(String recognitionModel) {
        this.recognitionModel = recognitionModel;
        return this;
    }

    public static LargeFaceList builder(String id, String name) {
        return new LargeFaceList(id, name);
    }

    public LargeFaceList build() {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(this.endpoint + "/face/v1.0/largefacelists/" + this.id);

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPut request = new HttpPut(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", this.subscriptionKey);

            // Request body.
            JSONObject jo = new JSONObject();
            jo.put("name", this.name);
            jo.put("userDate", this.userData);
            jo.put("recognitionModel", this.recognitionModel);
            StringEntity reqEntity = new StringEntity(jo.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            // 200
            if (response.getStatusLine().getStatusCode() == 200) {
                return this;
            } else {    // 40x
                throw new IllegalStateException(EntityUtils.toString(entity)); // 아래 catch문에서 잡힐 것임
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public String add(String imageUrlList) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String persistedFaceId = "";

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/largefacelists/" + this.id + "/persistedfaces");

            // Request parameters. All of them are optional.


            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            JSONObject jo = new JSONObject();
            jo.put("url", imageUrlList);
            StringEntity reqEntity = new StringEntity(jo.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String jsonString = EntityUtils.toString(entity).trim();
                try {
                    if (jsonString.charAt(0) == '[') {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jsonObject = new JSONObject(jsonArray.get(0));
                        persistedFaceId = jsonObject.getString("persistedFaceId");
                    } else if (jsonString.charAt(0) == '{') {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        persistedFaceId = jsonObject.getString("persistedFaceId");
                    }
                } catch (JSONException e) {
                    throw new IllegalStateException(jsonString);
                }

            }

        } catch (Exception e) {
           e.printStackTrace();
        }

        return persistedFaceId;
    }
}
