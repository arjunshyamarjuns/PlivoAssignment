package com.plivo;

import com.plivo.Scenario2.FetchLatLon;
import com.plivo.Scenario2.FetchWeatherData;
import io.github.cdimascio.dotenv.Dotenv;
import org.testng.annotations.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BaseClass {
    protected static final Logger logger = LoggerFactory.getLogger(BaseClass.class);
    protected static Dotenv dotenv;
    protected static String AUTH_ID;
    protected static String AUTH_TOKEN;
    public void setUpPlivoTest() {
        dotenv = Dotenv.load();
        AUTH_ID = dotenv.get("PLIVO_AUTH_ID");
        AUTH_TOKEN = dotenv.get("PLIVO_AUTH_TOKEN");
        logger.info("BaseClass setup completed.");

    }
    public void setUpWeatherAutomationTest() {
        createCityCSV();
        FetchLatLon.main(new String[]{});
        FetchWeatherData.main(new String[]{});
        logger.info("BaseClass setup completed.");

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
                    writer.append("Chicago\n");
                    writer.append("Houston\n");
                    writer.append("Bangalore\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDown() {
        File customerMessage = new File("src/main/resources/customer_message.csv");
        if (customerMessage.exists()) {
            customerMessage.delete();
        }
        logger.info("BaseClass teardown completed.");

    }
}
