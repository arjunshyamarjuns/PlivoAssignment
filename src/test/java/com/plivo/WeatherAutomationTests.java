package com.plivo;

import static org.testng.Assert.*;

import com.plivo.Scenario2.EnvConfig;
import com.plivo.Scenario2.FetchLatLon;
import com.plivo.Scenario2.FetchWeatherData;
import com.plivo.Scenario2.GetTopNCities;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WeatherAutomationTests extends BaseClass {
    @BeforeClass
    public void setup() {
        super.setUpWeatherAutomationTest();
        logger.info("Weather automation test setup completed.");
    }

    @Test
    public void testApiKeyValid() {
        logger.info("Starting testApiKeyValid...");
        assertNotNull(EnvConfig.getApiKey(), "API key should not be null");
        logger.info("testApiKeyValid passed.");
    }

    @Test
    public void testInvalidApiKey() {
        logger.info("Starting testInvalidApiKey...");
        String originalApiKey = EnvConfig.getApiKey();
        EnvConfig.setOverrideApiKey("invalid_api_key");
        try {
            double[] coords = FetchLatLon.getLatLon("Chicago");
            fail("Expected an IOException to be thrown due to invalid API key");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unauthorized"));
            logger.error("IOException caught as expected: " + e.getMessage());
        } finally {
            // Restore the original API key after the test
            EnvConfig.clearOverrideApiKey();
        }
        logger.info("testInvalidApiKey passed.");
    }

    @Test
    public void testCityCoords() {
        logger.info("Starting testCityCoords...");
        Map<String, double[]> cityCoords = FetchLatLon.cityCoords;
        assertNotNull(cityCoords.get("Chicago"), "City coordinates should not be null");
        assertEquals(2, cityCoords.get("Chicago").length, "City coordinates should have length of 2 (latitude and longitude)");
        logger.info("testCityCoords passed.");
    }

    @Test
    public void testCityCoordsNonExistentCity() {
        logger.info("Starting testCityCoordsNonExistentCity...");
        try {
            double[] coords = FetchLatLon.getLatLon("NonExistentCity");
            fail("Expected an IOException to be thrown for a nonexistent city");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("City not found: NonExistentCity"));
            logger.error("IOException caught as expected: " + e.getMessage());
        }
        logger.info("testCityCoordsNonExistentCity passed.");
    }

    @Test
    public void testWeatherData() {
        logger.info("Starting testWeatherData...");
        Map<String, JSONObject> weatherData = FetchWeatherData.weatherData;
        assertNotNull(weatherData.get("Chicago"), "Weather data should not be null for Chicago");
        assertTrue(weatherData.get("Chicago").has("current"), "Weather data should contain 'current' key");
        logger.info("testWeatherData passed.");
    }

    @Test
    public void testWeatherDataInvalidCoordinates() {
        logger.info("Starting testWeatherDataInvalidCoordinates...");
        try {
            JSONObject data = FetchWeatherData.getWeatherData(0, 0);
            fail("Expected an IOException to be thrown for invalid coordinates");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Error fetching weather data"));
            logger.error("IOException caught as expected: " + e.getMessage());
        }
        logger.info("testWeatherDataInvalidCoordinates passed.");
    }

    @Test
    public void testTopNCities() {
        logger.info("Starting testTopNCities...");
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
            logger.error("IOException caught: " + e.getMessage());
        }

        assertFalse(weatherStats.isEmpty(), "Weather stats should not be empty");
        logger.info("testTopNCities passed.");
    }

    @Test
    public void testTopNCitiesEmptyFile() {
        logger.info("Starting testTopNCitiesEmptyFile...");
        try (FileWriter writer = new FileWriter("data/city_stats.csv")) {
            writer.append("City,Temperature,Humidity\n"); // Write header only
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException caught: " + e.getMessage());
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
            logger.error("IOException caught: " + e.getMessage());
        }

        assertTrue(weatherStats.isEmpty(), "Weather stats should be empty for an empty file");
        logger.info("testTopNCitiesEmptyFile passed.");
    }
}
