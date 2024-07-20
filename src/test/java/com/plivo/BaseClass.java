package com.plivo;

import com.plivo.Scenario2.FetchLatLon;
import com.plivo.Scenario2.FetchWeatherData;
import io.github.cdimascio.dotenv.Dotenv;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BaseClass {
    protected static Dotenv dotenv;
    protected static String AUTH_ID;
    protected static String AUTH_TOKEN;

    @BeforeClass
    public void setUp() {
        dotenv = Dotenv.load();
        AUTH_ID = dotenv.get("PLIVO_AUTH_ID");
        AUTH_TOKEN = dotenv.get("PLIVO_AUTH_TOKEN");
    }
    @BeforeClass
    public void setUpWeatherAutomationTest() {
        createCityCSV();
        FetchLatLon.main(new String[]{});
        FetchWeatherData.main(new String[]{});
    }
    private void createCityCSV() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }

            File cityFile = new File("data/city.csv");
            if (!cityFile.exists()) {
                try (FileWriter writer = new FileWriter(cityFile)) {
                    writer.append("City\n");
                    writer.append("New York\n");
                    writer.append("Los Angeles\n");
                    writer.append("Chicago\n");
                    writer.append("Houston\n");
                    writer.append("Phoenix\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDown() {
        // Add any necessary teardown operations
    }
}
