package com.mkhang.controller;

import com.google.gson.Gson;
import com.mkhang.model.WeatherData;
import com.mkhang.util.ApiClient;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {

    private static final String OPENWEATHER_API_KEY = System.getenv("OPENWEATHER_API_KEY");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String city = request.getParameter("city");

        if (city == null || city.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"City parameter is required\"}");
            return;
        }

        try {
            // Call OpenWeatherMap API
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + OPENWEATHER_API_KEY + "&units=metric";
            String weatherJson = ApiClient.getJsonFromUrl(weatherUrl);

            JSONObject weatherObj = new JSONObject(weatherJson);

            String cityName = weatherObj.getString("name");
            JSONObject sys = weatherObj.getJSONObject("sys");
            String countryCode = sys.getString("country");

            JSONObject main = weatherObj.getJSONObject("main");
            double temp = main.getDouble("temp");
            int humidity = main.getInt("humidity");
            int pressure = main.getInt("pressure");

            JSONObject clouds = weatherObj.getJSONObject("clouds");
            int cloudPercent = clouds.getInt("all");

            JSONObject weather = weatherObj.getJSONArray("weather").getJSONObject(0);
            String mainWeather = weather.getString("main");
            String description = weather.getString("description");

            int timezoneOffset = weatherObj.getInt("timezone");

            // Call RESTCountries API to get flag
            String countryUrl = "https://restcountries.com/v3.1/alpha/" + countryCode;
            String countryJson = ApiClient.getJsonFromUrl(countryUrl);
            JSONObject countryObj = new JSONObject(countryJson.substring(1, countryJson.length() - 1)); // vì trả về mảng []

            String flagUrl = countryObj.getJSONObject("flags").getString("png");

            // Local time
            LocalDateTime localDateTime = LocalDateTime.ofInstant(
                    Instant.now(),
                    ZoneOffset.ofTotalSeconds(timezoneOffset)
            );
            String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"));

            // Create object WeatherData
            WeatherData weatherData = new WeatherData(
                    cityName, countryCode, flagUrl, temp, description,
                    mainWeather, cloudPercent, humidity, pressure, formattedTime
            );

            // Return JSON to frontend
            Gson gson = new Gson();
            out.print(gson.toJson(weatherData));

        } catch (RuntimeException e) {
            e.printStackTrace();
            // Check if it's an authentication error
            if (e.getMessage().contains("401")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"API authentication failed. Please check your API key.\"}");
            } else if (e.getMessage().contains("404")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"City not found.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"API error: " + e.getMessage() + "\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Internal server error: " + e.getMessage() + "\"}");
        }
    }
}