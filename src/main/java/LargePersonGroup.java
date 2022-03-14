import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class LargePersonGroup {

    private static final String resource="face/v1.0/largepersongroups/";

    private final String largePersonGroupId;
    private final String name;

    // optional
    private String userData = "";
    private String recognitionModel = "";

    private final HttpRequestFacade httpRequestFacade;

    public static LargePersonGroup builder(String largePersonGroupId, String name) {
        return new LargePersonGroup(largePersonGroupId, name);
    }

    protected LargePersonGroup(String largePersonGroupId, String name) {
        super();
        this.largePersonGroupId = largePersonGroupId;
        this.name = name;
        httpRequestFacade = new HttpRequestFacade(resource);
    }

    public LargePersonGroup userData(String userData) {
        this.userData = userData;
        return this;
    }

    public LargePersonGroup recognitionModel(String recognitionModel) {
        this.recognitionModel = recognitionModel;
        return this;
    }

    public String getId() {
        return this.largePersonGroupId;
    }

    public LargePersonGroup build() {

        try {
            // Request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.name);
            jsonObject.put("userData", this.userData);
            jsonObject.put("recognitionModel", this.recognitionModel);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.PUT,httpRequestFacade.getUriBuilder(this.largePersonGroupId),jsonObject);
            HttpEntity entity = response.getEntity();

            // 200
            if (response.getStatusLine().getStatusCode() == 200) {
                return this;
            } else {    // api error
                throw new IllegalStateException(EntityUtils.toString(entity)); // 아래 catch문에서 잡힐 것임
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public void train(String largePersonGroupId){
        String path=largePersonGroupId+"/train";
        try {
            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST,httpRequestFacade.getUriBuilder(path),null);
            HttpEntity entity = response.getEntity();
            if(response.getStatusLine().getStatusCode()==202){
                return;
            }
            System.out.println(EntityUtils.toString(entity));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
