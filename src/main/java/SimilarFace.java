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
    private static final String resource = "/face/v1.0/";
    private HttpRequestFacade httpRequestFacade;
    private static Face face = new Face();

    private static final String imageWithFaces =
            "https://www.city.kr/files/attach/images/164/021/406/027/a3a171092f21ff2fc5f3e473ddc99e50.jpeg";
    private static final String[] imagesWithFace = {
            "https://img.hankyung.com/photo/202112/BF.28305426.1.jpg",
            "http://file.mk.co.kr/meet/neds/2021/09/image_readtop_2021_915112_16325771814795046.jpg",
            "https://blog.kakaocdn.net/dn/U9jp4/btriYfr5pCN/xtXF2GMKqgHjvSN6h2L3ck/img.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Dad1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Daughter1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Mom1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family1-Son1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family2-Lady1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family2-Man1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family3-Lady1.jpg",
            "https://csdx.blob.core.windows.net/resources/Face/Images/Family3-Man1.jpg"
    };

    public SimilarFace() {
        this.httpRequestFacade = new HttpRequestFacade(this.resource);
    }

// </environment>

    private static String getImageId(String imageWithFaces) {

        String imageId = "";

        String jsonString = face.detect(imageWithFaces);

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


        // Format and display the JSON response.
        System.out.println("REST Response:\n");

        String jsonString = face.findSimilar(faceId, faceIds.toArray(new String[faceIds.size()]));
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
}
// </print>