import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class Verify {
    private static final String subscriptionKey = Key.getKey();
    private static final String endpoint = Key.getEndpoint();
    private static String[] person = {
            "https://img.hankyung.com/photo/202112/BF.28305426.1.jpg", //비교대상 이미지
            "https://i.ytimg.com/vi/UAQT5Hgrm1Q/maxresdefault.jpg" //피 비교대상 이미지
    };

    private static String getImageId(String imageWithFaces) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String imageId = "";

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/detect");

            // Request parameters. All of them are optional.
            builder.setParameter("detectionModel", "detection_01");
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            JSONObject jo = new JSONObject();
            jo.put("url", imageWithFaces);
            StringEntity reqEntity = new StringEntity(jo.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                    imageId = jsonObject.getString("faceId");
                } else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    imageId = jsonObject.getString("faceId");
                } else {
                    System.out.println(jsonString);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return imageId;
    }

    public static void main(String[] args) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/verify");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            JSONObject jo = new JSONObject();
            jo.put("faceId1", getImageId(person[0]));
            jo.put("faceId2", getImageId(person[1]));
            StringEntity reqEntity = new StringEntity(jo.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();
                System.out.println(jsonString);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
