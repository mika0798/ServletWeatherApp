package com.mkhang.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    // GET API data and return JSON string
    public static String getJsonFromUrl(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        // Check server response code
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            // Read error response for better debugging
            String errorMessage = "HTTP GET Request Failed. Error Code: " + responseCode;
            if (responseCode == 401) {
                errorMessage += " - Unauthorized. Please check your API key.";
            } else if (responseCode == 404) {
                errorMessage += " - Resource not found.";
            }

            // Try to read error response body
            try {
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream())
                );
                StringBuilder errorBody = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorBody.append(line);
                }
                errorReader.close();
                if (errorBody.length() > 0) {
                    errorMessage += " Details: " + errorBody.toString();
                }
            } catch (Exception ignored) {
                // If we can't read error stream, continue with basic message
            }

            throw new RuntimeException(errorMessage);
        }

        // Read JSON response data
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        conn.disconnect();

        return jsonBuilder.toString();
    }
}