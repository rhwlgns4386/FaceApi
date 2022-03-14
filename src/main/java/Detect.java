// <dependencies>
// This sample uses Apache HttpComponents:
// http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/
// https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/
// api 사용 참고
// https://docs.microsoft.com/ko-kr/azure/cognitive-services/face/quickstarts/client-libraries?tabs=visual-studio&pivots=programming-language-rest-api

import java.net.URI;

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
public class Detect {

    public static Face face = new Face();

    private static final String imageWithFaces =
            "https://thumb.mt.co.kr/06/2021/08/2021081919350869043_1.jpg/dims/optimize/";


    public static void main(String[] args) {

        // Format and display the JSON response.
        System.out.println("REST Response:\n");

        String jsonString = face.detect(imageWithFaces);
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