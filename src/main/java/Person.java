import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/persongroups/" + personGroupId +"/persons");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

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

            if (entity != null) {
                String jsonString;
                jsonString = EntityUtils.toString(entity).trim();
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
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return this;
    }


}
