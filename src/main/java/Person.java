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

    private static final String resource="face/v1.0/largepersongroups/";

    private String name;
    private String personGroupId;
    private String userData;
    private String personId;

    private final HttpRequestFacade httpRequestFacade;

    protected Person(String personGroupId, String name) {
        this.personGroupId = personGroupId;
        this.name = name;
        httpRequestFacade=new HttpRequestFacade(resource);
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
        String queryParam=personGroupId +"/persons";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.name);
            jsonObject.put("userData", this.userData);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST,queryParam,jsonObject);
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
        String persistedFaceId = "";
        String queryParam=largePersonGroupId +"/persons/" + this.personId + "/persistedfaces";

        try {

            // Request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", imageUrl);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST,queryParam,jsonObject);
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
