
package com.plivo.Scenario2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetTopNCities {

    public static void main(String[] args) {
        List<CityWeather> weatherStats = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/city_stats.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String city = data[0];
                double temp = Double.parseDouble(data[1]);
                int humidity = Integer.parseInt(data[2]);
                weatherStats.add(new CityWeather(city, temp, humidity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<CityWeather> topNColdest = weatherStats.stream()
                .sorted(Comparator.comparingDouble(CityWeather::getTemperature))
                .limit(3)
                .collect(Collectors.toList());

        List<CityWeather> topNHumid = weatherStats.stream()
                .sorted(Comparator.comparingInt(CityWeather::getHumidity).reversed())
                .limit(3)
                .collect(Collectors.toList());

        System.out.println("Top N Coldest Cities:");
        topNColdest.forEach(System.out::println);

        System.out.println("Top N Most Humid Cities:");
        topNHumid.forEach(System.out::println);
    }

    public static class CityWeather {
        private final String city;
        private final double temperature;
        private final int humidity;

        public CityWeather(String city, double temperature, int humidity) {
            this.city = city;
            this.temperature = temperature;
            this.humidity = humidity;
        }

        public String getCity() {
            return city;
        }

        public double getTemperature() {
            return temperature;
        }

        public int getHumidity() {
            return humidity;
        }

        @Override
        public String toString() {
            return "CityWeather{" +
                    "city='" + city + '\'' +
                    ", temperature=" + temperature +
                    ", humidity=" + humidity +
                    '}';
        }
    }
}
