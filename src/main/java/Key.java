public class Key {
    private static final String key = System.getenv("SUBSCRIPTIONKEY");
    private static final String endpoint = System.getenv("ENDPOINT");

    public static String getKey() {
        return key;
    }

    public static String getEndpoint() {
        return endpoint;
    }
}
