import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.net.URI;

public class LargePersonGroup {

    private final String endpoint = Key.getEndpoint();
    private final String subscriptionKey = Key.getKey();

    private final String largePersonGroupId;
    private final String name;
    private String userData = "";
    private String recognitionModel = "";

    public static LargePersonGroup builder(String largePersonGroupId, String name) {
        return new LargePersonGroup(largePersonGroupId, name);
    }

    protected LargePersonGroup(String largePersonGroupId, String name) {
        super();
        this.largePersonGroupId = largePersonGroupId;
        this.name = name;
    }

    public LargePersonGroup userDate(String userData) {
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
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/largepersongroups/" + this.largePersonGroupId);

            URI uri = builder.build();
            HttpPut request = new HttpPut(uri);

            // Request headers
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.name);
            jsonObject.put("userData", this.userData);
            jsonObject.put("recognitionModel", this.recognitionModel);

            // Execute the REST API call and get the response entity
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            // 200
            if (response.getStatusLine().getStatusCode() == 200) {
                return this;
            } else {    // 40x
                throw new APIException(entity); // 아래 catch문에서 잡힐 것임
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return this;
    }


}
