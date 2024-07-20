package com.plivo.Scenario1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GetCustomerIDs {
    public static List<Integer> getCustomerIDs(String input) {
        String[] ids = input.split(",");
        List<Integer> customerIDs = new ArrayList<>();
        for (String id : ids) {
            customerIDs.add(Integer.parseInt(id.trim()));
        }
        return customerIDs;
    }
}
