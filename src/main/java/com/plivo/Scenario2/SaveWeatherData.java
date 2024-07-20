// src/main/java/com/weather/SaveWeatherData.java

package com.plivo.Scenario2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;

public class SaveWeatherData {

    public static void main(String[] args) {
        Map<String, JSONObject> weatherData = FetchWeatherData.weatherData;

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
