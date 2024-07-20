
package com.plivo.Scenario2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CreateCityCSV {

    public static void main(String[] args) {
        List<String> cities = Arrays.asList("New York", "Los Angeles", "Chicago", "Houston", "Phoenix");

        try (FileWriter writer = new FileWriter("data/city.csv")) {
            writer.append("City\n");
            for (String city : cities) {
                writer.append(city).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
