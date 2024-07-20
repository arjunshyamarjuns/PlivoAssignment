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
        super.setUpPlivoTest();
        CreateCustomerCSV.createCustomerCSV(CSV_FILE_PATH);
        logger.info("Customer CSV file created.");
    }

    @Test
    public void testCreateCustomerCSV() {
        List<Message> messages = ReadCustomerData.readCustomerData(CSV_FILE_PATH, List.of(1, 2, 3, 4));
        assertEquals(messages.size(), 4);
        logger.info("testCreateCustomerCSV passed.");

    }

    @Test
    public void testGetCustomerIDs() {
        List<Integer> ids = GetCustomerIDs.getCustomerIDs("1,2,3");
        assertEquals(ids, List.of(1, 2, 3));
        logger.info("testGetCustomerIDs passed.");

    }

    @Test
    public void testReadCustomerData() {
        List<Integer> customerIDs = List.of(1, 3);
        List<Message> messages = ReadCustomerData.readCustomerData(CSV_FILE_PATH, customerIDs);
        assertEquals(messages.size(), 2);
        assertEquals(messages.get(0).src, "+14155552671");
        assertEquals(messages.get(0).dst, "+916235982345");
        logger.info("testReadCustomerData passed.");

    }

    @Test
    public void testSendSMSAndFetchDetails() {
        List<Integer> customerIDs = List.of(1, 2);
        List<Message> messages = ReadCustomerData.readCustomerData(CSV_FILE_PATH, customerIDs);
        logger.info("Messages to be sent: " + messages);
        try {
            List<String> messageUUIDs = SendSMS.sendSMS(messages, AUTH_ID, AUTH_TOKEN);
            FetchMessageDetails.fetchAndWriteMessageDetails(messageUUIDs, AUTH_ID, AUTH_TOKEN, RESULT_FILE_PATH);
            logger.info("testSendSMSAndFetchDetails passed.");
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
        logger.error("testInvalidPhoneNumberFormat failed due to invalid phone number format.");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testEmptyMessageText() {
        List<Message> messages = List.of(
                new Message("+14155552671", "+16235982345", "")
        );
        SendSMS.sendSMS(messages, AUTH_ID, AUTH_TOKEN);
        logger.error("testEmptyMessageText failed due to empty message text.");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testNetworkFailureSimulation() {
        List<Message> messages = List.of(
                new Message("+14155552671", "+16235982345", "Network failure simulation")
        );
        // Simulate network failure by using invalid auth credentials
        SendSMS.sendSMS(messages, "invalid_auth_id", "invalid_auth_token");
        logger.error("testNetworkFailureSimulation failed due to simulated network failure.");
    }
}
