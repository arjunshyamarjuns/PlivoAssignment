package com.plivo.Scenario1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCustomerData {
    public static List<Message> readCustomerData(String filePath, List<Integer> customerIDs) {
        List<Message> messages = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0].trim());
                if (customerIDs.contains(id)) {
                    messages.add(new Message(values[1], values[2], values[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
