package knowledge;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class F1KnowledgeBase {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";
    private static final String API_KEY = System.getenv("GEMINI_API_KEY"); // Securely fetch API key from environment variables

    private String userLogin;

    public F1KnowledgeBase(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getAnswer(String question) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            return "API Key is missing. Please set the GEMINI_API_KEY environment variable.";
        }
        try {
            return callGeminiAPI(question);
        } catch (Exception e) {
            return "Error getting answer: " + e.getMessage();
        }
    }

    private String callGeminiAPI(String question) throws Exception {
        String jsonInputString = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", question)))))
                .put("userLogin", userLogin) // Include user login in the request
                .toString();

        URL url = new URL(GEMINI_API_URL + "?key=" + API_KEY);
        HttpURLConnection con = null;  // Initialize to null
        int responseCode = -1;         // Set an invalid default response code
        int waitTime = 1000;           // Start with 1 second for backoff

        for (int i = 0; i < 5; i++) {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInputString.getBytes("utf-8"));
            }

            responseCode = con.getResponseCode();

            if (responseCode == 429) { // Rate-limited, retry with backoff
                Thread.sleep(waitTime);
                waitTime *= 2;
            } else {
                break; // Exit loop if not a 429 error
            }
        }

        if (con != null && responseCode == 200) { // Successful response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return parseResponse(response.toString());
        } else {
            return "Error getting answer: HTTP " + responseCode;
        }
    }

    private String parseResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .trim();
        } catch (JSONException e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}