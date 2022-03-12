import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class Person {

    private final String endpoint = Key.getEndpoint();
    private final String subscriptionKey = Key.getKey();

    private String name;
    private String personGroupId;
    private String userData;
    private String personId;

    protected Person(String personGroupId, String name) {
        this.personGroupId = personGroupId;
        this.name = name;
    }

    public static Person builder(String personGroupId, String name) {
        return new Person(personGroupId, name);
    }

    public Person userData(String userData) {
        this.userData = userData;
        return this;
    }

    public String getPersonId() {
        return this.personId;
    }

    public Person build() {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/largepersongroups/" + personGroupId +"/persons");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.name);
            jsonObject.put("userData", this.userData);

            StringEntity reqEntity = new StringEntity(jsonObject.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString;
                jsonString = EntityUtils.toString(entity).trim();
                try {
                    if (jsonString.charAt(0) == '[') {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jo = new JSONObject(jsonArray.get(0));
                        this.personId = jo.getString("personId");
                    } else if (jsonString.charAt(0) == '{') {
                        JSONObject jo = new JSONObject(jsonString);
                        this.personId = jo.getString("personId");
                    } else {
                        this.personId = jsonString;
                    }
                } catch (JSONException e) {
                    throw new IllegalStateException(jsonString);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public String addFace(String largePersonGroupId, String imageUrl) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String persistedFaceId = "";

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/largepersongroups/" +  largePersonGroupId + "/persons/" + this.personId + "/persistedfaces");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", imageUrl);

            StringEntity reqEntity = new StringEntity(jsonObject.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString;
                jsonString = EntityUtils.toString(entity).trim();
                try {
                    if (jsonString.charAt(0) == '[') {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jo = new JSONObject(jsonArray.get(0));
                        persistedFaceId = jo.getString("persistedFaceId");
                    } else if (jsonString.charAt(0) == '{') {
                        JSONObject jo = new JSONObject(jsonString);
                        persistedFaceId = jo.getString("persistedFaceId");
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
