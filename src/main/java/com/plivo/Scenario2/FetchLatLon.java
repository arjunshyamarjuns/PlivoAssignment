package com.plivo.Scenario2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchLatLon {
    public static Map<String, double[]> cityCoords = new HashMap<>();

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/city.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String city = line.trim();
                double[] coords = getLatLon(city);
                cityCoords.put(city, coords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        cityCoords.forEach((city, coords) -> {
            System.out.println(city + ": " + coords[0] + ", " + coords[1]);
        });
    }

    public static double[] getLatLon(String city) throws IOException {
        String apiKey = EnvConfig.getApiKey();
        String urlString = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", city, apiKey);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new IOException("Error fetching coordinates for city: " + city + " - " + conn.getResponseMessage());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        JSONArray jsonArray = new JSONArray(content.toString());
        if (jsonArray.length() > 0) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            double lat = jsonObject.getDouble("lat");
            double lon = jsonObject.getDouble("lon");
            return new double[]{lat, lon};
        } else {
            throw new IOException("City not found: " + city);
        }
    }
}
