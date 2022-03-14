import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class Identity {
    private HttpRequestFacade httpRequestFacade;
    private String resource = "face/v1.0/identify";

    public Identity() {
        httpRequestFacade = new HttpRequestFacade(resource);
    }

    public String build(String largePersonGroupId, String[] faceIds) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("largePersonGroupId", largePersonGroupId);
        jsonObject.put("faceIds", faceIds);
        try {
            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST, httpRequestFacade.getUriBuilder(""), jsonObject);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity).trim();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
