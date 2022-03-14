import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class Face {

    private static final String resource="/face/v1.0/";
    private HttpRequestFacade httpRequestFacade;

    public Face() {
        this.httpRequestFacade = new HttpRequestFacade(resource);
    }

    public String detect(String imageWithFaces) {
        String imageId = "";
        String path="detect";

        try {

            URIBuilder builder = this.httpRequestFacade.getUriBuilder(path);
            // Request parameters. All of them are optional.
            builder.setParameter("detectionModel", "detection_01");
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");


            // Request body.
            JSONObject jo = new JSONObject();
            jo.put("url", imageWithFaces);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST,builder,jo);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public String findSimilar(String faceId,String[] faceIds){

        String path="findsimilars";

        JSONObject jo = new JSONObject();
        jo.put("faceId", faceId);
        jo.put("maxNumOfCandidatesReturned", 10);
        jo.put("mode", "matchPerson");
        jo.put("faceIds", faceIds);

        try {
            HttpResponse response=httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST,httpRequestFacade.getUriBuilder(path),jo);
            HttpEntity entity=response.getEntity();
            return EntityUtils.toString(entity);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String group(String[] faceIds) {

        String path = "group";

        JSONObject jo = new JSONObject();
        jo.put("faceIds", faceIds);
        System.out.println(jo);

        HttpResponse response = null;
        try {
            response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST, httpRequestFacade.getUriBuilder(path), jo);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }
}
