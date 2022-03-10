import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParsing {
    public static void main(String[] args) {
        String json = "[{ \"faceRectangle\": { \"top\": 131, \"left\": 177, \"width\": 162, \"height\": 162 }, \"faceId\": \"5bcf6777-75d4-4658-8787-c1d42029f6a4\" }]";
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
        System.out.println(jsonObject.getString("faceId"));
    }
}
