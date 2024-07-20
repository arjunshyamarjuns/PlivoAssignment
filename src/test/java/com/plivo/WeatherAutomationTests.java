package com.plivo;

import static org.testng.Assert.*;

import com.plivo.Scenario2.EnvConfig;
import com.plivo.Scenario2.FetchLatLon;
import com.plivo.Scenario2.FetchWeatherData;
import com.plivo.Scenario2.GetTopNCities;
import org.json.JSONObject;
import org.testng.annotations.Test;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WeatherAutomationTests extends BaseClass {

    @Test
    public void testApiKeyValid() {
        assertNotNull(EnvConfig.getApiKey(), "API key should not be null");
    }

    @Test
    public void testInvalidApiKey() {
        String originalApiKey = EnvConfig.getApiKey();
        EnvConfig.setOverrideApiKey("invalid_api_key");
        try {
            double[] coords = FetchLatLon.getLatLon("Chicago");
            fail("Expected an IOException to be thrown due to invalid API key");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unauthorized"));
        } finally {
            // Restore the original API key after the test
            EnvConfig.clearOverrideApiKey();
        }
    }

    @Test
    public void testCityCoords() {
        Map<String, double[]> cityCoords = FetchLatLon.cityCoords;
        assertNotNull(cityCoords.get("Chicago"), "City coordinates should not be null");
        assertEquals(2, cityCoords.get("Chicago").length, "City coordinates should have length of 2 (latitude and longitude)");
    }

    @Test
    public void testCityCoordsNonExistentCity() {
        try {
            double[] coords = FetchLatLon.getLatLon("NonExistentCity");
            fail("Expected an IOException to be thrown for a nonexistent city");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("City not found: NonExistentCity"));
        }
    }

    @Test
    public void testWeatherData() {
        Map<String, JSONObject> weatherData = FetchWeatherData.weatherData;
        assertNotNull(weatherData.get("Chicago"), "Weather data should not be null for Chicago");
        assertTrue(weatherData.get("Chicago").has("current"), "Weather data should contain 'current' key");
    }

    @Test
    public void testWeatherDataInvalidCoordinates() {
        try {
            JSONObject data = FetchWeatherData.getWeatherData(0, 0);
            fail("Expected an IOException to be thrown for invalid coordinates");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Error fetching weather data"));
        }
    }

    @Test
    public void testTopNCities() {
        List<GetTopNCities.CityWeather> weatherStats = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/city_stats.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String city = data[0];
                double temp = Double.parseDouble(data[1]);
                int humidity = Integer.parseInt(data[2]);
                weatherStats.add(new GetTopNCities.CityWeather(city, temp, humidity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertFalse(weatherStats.isEmpty(), "Weather stats should not be empty");
    }

    @Test
    public void testTopNCitiesEmptyFile() {
        try (FileWriter writer = new FileWriter("data/city_stats.csv")) {
            writer.append("City,Temperature,Humidity\n"); // Write header only
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<GetTopNCities.CityWeather> weatherStats = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/city_stats.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String city = data[0];
                double temp = Double.parseDouble(data[1]);
                int humidity = Integer.parseInt(data[2]);
                weatherStats.add(new GetTopNCities.CityWeather(city, temp, humidity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(weatherStats.isEmpty(), "Weather stats should be empty for an empty file");
    }
}
