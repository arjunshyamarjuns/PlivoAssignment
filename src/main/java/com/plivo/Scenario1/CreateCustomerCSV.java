package com.plivo.Scenario1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CreateCustomerCSV {
    public static void createCustomerCSV(String filePath) {
        String[] header = { "ID", "SourceNumber", "DestinationNumber", "Message" };
        List<String[]> rows = Arrays.asList(
                new String[]{ "1", "+14155552671", "+916235982345", "Sending the SMS to customer ID 1" },
                new String[]{ "2", "+14155552670", "+916235982345", "Sending the SMS to customer ID 2" },
                new String[]{ "3", "src", "dst3", "Sending the SMS to customer ID 3" },
                new String[]{ "4", "src", "dst4", "Sending the SMS to customer ID 4" }
        );

        try (FileWriter csvWriter = new FileWriter(filePath)) {
            csvWriter.append(String.join(",", header)).append("\n");
            for (String[] row : rows) {
                csvWriter.append(String.join(",", row)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
