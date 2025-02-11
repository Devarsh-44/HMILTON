// Config.java
package knowledge;

public class Config {
    private static final String USERNAME = System.getenv("API_USERNAME");
    private static final String PASSWORD = System.getenv("API_PASSWORD");

    public static String getUsername() {
        if (USERNAME == null || USERNAME.isEmpty()) {
            throw new IllegalStateException("API Username is missing. Please set the API_USERNAME environment variable.");
        }
        return USERNAME;
    }

    public static String getPassword() {
        if (PASSWORD == null || PASSWORD.isEmpty()) {
            throw new IllegalStateException("API Password is missing. Please set the API_PASSWORD environment variable.");
        }
        return PASSWORD;
    }
}