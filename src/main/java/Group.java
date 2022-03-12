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
public class Group {
    private static final String subscriptionKey = Key.getKey();
    private static final String endpoint = Key.getEndpoint();

    private static final String[] imagesWithFace = {
            "https://img.hankyung.com/photo/202112/BF.28305426.1.jpg",
            "http://file.mk.co.kr/meet/neds/2021/09/image_readtop_2021_915112_16325771814795046.jpg",
            "https://blog.kakaocdn.net/dn/U9jp4/btriYfr5pCN/xtXF2GMKqgHjvSN6h2L3ck/img.jpg",
            "https://cdn.entermedia.co.kr/news/photo/202112/28300_52732_439.jpg",
            "https://www.city.kr/files/attach/images/238/458/380/026/333cd9a7d427ffc1b46b85122bb0e0f2.png",
            "https://cdn.9oodnews.com/news/photo/202112/13020_18839_256.jpg",
            "http://photo.sentv.co.kr/photo/2021/08/24/20210824093751.jpg",
            "https://img.asiatoday.co.kr/file/2021y/12m/15d/2021121501001616700091211.jpg",
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

    // <main>
    public static void main(String[] args) {
        HttpClient httpclient = HttpClientBuilder.create().build();

        LinkedList<String> faceIds = new LinkedList<>();
        for (String image : imagesWithFace) {
            String faceId = getImageId(image);
            faceIds.add(faceId);
        }

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/group");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            JSONObject jo = new JSONObject();
            jo.put("faceIds", faceIds);
            System.out.println(jo);
            StringEntity reqEntity = new StringEntity(jo.toString());
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
// </main>

// <print>
            if (entity != null) {
                // Format and display the JSON response.
                System.out.println("REST Response:\n");

                String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));
                } else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    System.out.println(jsonObject.toString(2));
                } else {
                    System.out.println(jsonString);
                }
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }

    }
}
// </print>
