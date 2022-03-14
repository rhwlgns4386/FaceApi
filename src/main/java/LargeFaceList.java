import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LargeFaceList {
    private static final String resource="face/v1.0/largefacelists/";

    private String id;
    private String name;
    private String userData = "";
    private String recognitionModel = "";

    private final HttpRequestFacade httpRequestFacade;

    protected LargeFaceList(String id, String name) {
        this.id = id;
        this.name = name;
        httpRequestFacade =new HttpRequestFacade(resource);
    }

    public LargeFaceList userDate(String userData) {
        this.userData = userData;
        return this;
    }

    public LargeFaceList recognitionModel(String recognitionModel) {
        this.recognitionModel = recognitionModel;
        return this;
    }

    public static LargeFaceList builder(String id, String name) {
        return new LargeFaceList(id, name);
    }

    public LargeFaceList build() {
        JSONObject jo = new JSONObject();
        jo.put("name", this.name);
        jo.put("userDate", this.userData);
        jo.put("recognitionModel", this.recognitionModel);

        try {

            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.PUT,httpRequestFacade.getUriBuilder(this.id),jo);
            HttpEntity entity = response.getEntity();

            // 200
            if (response.getStatusLine().getStatusCode() == 200) {
                return this;
            } else {    // 40x
                throw new IllegalStateException(EntityUtils.toString(entity)); // 아래 catch문에서 잡힐 것임
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public String add(String imageUrlList) {
        String persistedFaceId = "";

        String path=this.id + "/persistedfaces";

        try {
            JSONObject jo = new JSONObject();
            jo.put("url", imageUrlList);

            HttpResponse response = httpRequestFacade.getHttpResponse(HttpRequestFacade.HttpRequestMethod.POST, httpRequestFacade.getUriBuilder(path), jo);
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String jsonString = EntityUtils.toString(entity).trim();
                try {
                    if (jsonString.charAt(0) == '[') {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jsonObject = new JSONObject(jsonArray.get(0));
                        persistedFaceId = jsonObject.getString("persistedFaceId");
                    } else if (jsonString.charAt(0) == '{') {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        persistedFaceId = jsonObject.getString("persistedFaceId");
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
