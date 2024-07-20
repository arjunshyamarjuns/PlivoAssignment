package com.plivo;

import com.plivo.Scenario1.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class PlivoSMSTest extends BaseClass {

    private static final String CSV_FILE_PATH = "src/main/resources/customer_message.csv";
    private static final String RESULT_FILE_PATH = "result.txt";

    @BeforeClass
    public void setup() {
        super.setUp();
        CreateCustomerCSV.createCustomerCSV(CSV_FILE_PATH);
    }

    @Test
    public void testCreateCustomerCSV() {
        List<Message> messages = ReadCustomerData.readCustomerData(CSV_FILE_PATH, List.of(1, 2, 3, 4));
        assertEquals(messages.size(), 4);
    }

    @Test
    public void testGetCustomerIDs() {
        List<Integer> ids = GetCustomerIDs.getCustomerIDs("1,2,3");
        assertEquals(ids, List.of(1, 2, 3));
    }

    @Test
    public void testReadCustomerData() {
        List<Integer> customerIDs = List.of(1, 3);
        List<Message> messages = ReadCustomerData.readCustomerData(CSV_FILE_PATH, customerIDs);
        assertEquals(messages.size(), 2);
        assertEquals(messages.get(0).src, "+14155552671");
        assertEquals(messages.get(0).dst, "+16235982345");
    }

    @Test
    public void testSendSMSAndFetchDetails() {
        List<Integer> customerIDs = List.of(1, 2);
        List<Message> messages = ReadCustomerData.readCustomerData(CSV_FILE_PATH, customerIDs);
        System.out.println(messages);
        try {
            List<String> messageUUIDs = SendSMS.sendSMS(messages, AUTH_ID, AUTH_TOKEN);
            FetchMessageDetails.fetchAndWriteMessageDetails(messageUUIDs, AUTH_ID, AUTH_TOKEN, RESULT_FILE_PATH);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("SMS sending failed with error: " + e.getMessage());
        }
        assertTrue(true);
    }

    // Negative Test Cases

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidPhoneNumberFormat() {
        List<Message> messages = List.of(
                new Message("+14155552671", "6235982345", "Invalid destination number format"),
                new Message("4155552671", "+16235982345", "Invalid source number format")
        );
        SendSMS.sendSMS(messages, AUTH_ID, AUTH_TOKEN);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testEmptyMessageText() {
        List<Message> messages = List.of(
                new Message("+14155552671", "+16235982345", "")
        );
        SendSMS.sendSMS(messages, AUTH_ID, AUTH_TOKEN);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testNetworkFailureSimulation() {
        List<Message> messages = List.of(
                new Message("+14155552671", "+16235982345", "Network failure simulation")
        );
        // Simulate network failure by using invalid auth credentials
        SendSMS.sendSMS(messages, "invalid_auth_id", "invalid_auth_token");
    }
}
