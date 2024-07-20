package com.plivo.Scenario2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class FetchWeatherData {
    public static Map<String, JSONObject> weatherData = new HashMap<>();

    public static void main(String[] args) {
        Map<String, double[]> cityCoords = FetchLatLon.cityCoords;
        cityCoords.forEach((city, coords) -> {
            try {
                JSONObject data = getWeatherData(coords[0], coords[1]);
                weatherData.put(city, data);
            } catch (IOException e) {
                System.err.println("Error fetching weather data for " + city + ": " + e.getMessage());
            }
        });

        saveWeatherData(weatherData);

        weatherData.forEach((city, data) -> {
            System.out.println(city + ": " + data.toString());
        });
    }

    public static JSONObject getWeatherData(double lat, double lon) throws IOException {
        String apiKey = EnvConfig.getApiKey();
        System.out.println("API Key: " + apiKey);
        String urlString = String.format("http://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&appid=%s", lat, lon, apiKey);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new IOException("Error fetching weather data: " + conn.getResponseMessage());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return new JSONObject(content.toString());
    }

    private static void saveWeatherData(Map<String, JSONObject> weatherData) {
        try (FileWriter writer = new FileWriter("data/city_stats.csv")) {
            writer.append("City,Temperature,Humidity\n");
            weatherData.forEach((city, data) -> {
                try {
                    double temp = data.getJSONObject("current").getDouble("temp");
                    int humidity = data.getJSONObject("current").getInt("humidity");
                    writer.append(String.format("%s,%f,%d\n", city, temp, humidity));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
