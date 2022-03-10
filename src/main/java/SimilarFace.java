// <dependencies>
// This sample uses Apache HttpComponents:
// http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/
// https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/
// api 사용 참고
// https://docs.microsoft.com/ko-kr/azure/cognitive-services/face/quickstarts/client-libraries?tabs=visual-studio&pivots=programming-language-rest-api

import java.net.URI;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
// </dependencies>

// <environment>
/*
 * To compile and run, enter the following at a command prompt:
 *   javac Detect.java -cp .;lib\*
 *   java -cp .;lib\* Detect
 */
public class SimilarFace {
    private static final String subscriptionKey = Key.getKey();
    private static final String endpoint = Key.getEndpoint();

    private static final String imageWithFaces =
            "https://csdx.blob.core.windows.net/resources/Face/Images/findsimilar.jpg";
    private static final String[] imagesWithFace = {
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Dad1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Daughter1.jpg",
    "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Mom1.jpg",
    "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Son1.jpg",
    "https://csdx.blob.core.windows.net/resources/Face/Images/Family2-Lady1.jpg",
    "https://csdx.blob.core.windows.net/resources/Face/Images/Family2-Man1.jpg",
    "https://csdx.blob.core.windows.net/resources/Face/Images/Family3-Lady1.jpg",
    "https://csdx.blob.core.windows.net/resources/Face/Images/Family3-Man1.jpg"
    };

// </environment>

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
                }
                else if (jsonString.charAt(0) == '{') {
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

    // <main>
    public static void main(String[] args) {
        HttpClient httpclient = HttpClientBuilder.create().build();

        LinkedList<String> faceIds = new LinkedList<>();
        for (String image : imagesWithFace) {
            String faceId = getImageId(image);
            faceIds.add(faceId);
        }

        String faceId = getImageId(imageWithFaces);

        try
        {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/findsimilars");

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
            jo.put("faceId", faceId);
            jo.put("maxNumOfCandidatesReturned", 10);
            jo.put("mode", "matchPerson");
            for (String id : faceIds) {
                jo.append("faceIds", id);
            }
            System.out.println(jo);
            StringEntity reqEntity = new StringEntity(jo.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
// </main>

// <print>
            if (entity != null)
            {
                // Format and display the JSON response.
                System.out.println("REST Response:\n");

                String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));
                }
                else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    System.out.println(jsonObject.toString(2));
                } else {
                    System.out.println(jsonString);
                }
            }
        }
        catch (Exception e)
        {
            // Display error message.
            System.out.println(e.getMessage());
        }

    }
}
// </print>