package com.plivo.Scenario1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.plivo.api.Plivo;
import com.plivo.api.exceptions.PlivoRestException;
import com.plivo.api.exceptions.PlivoValidationException;
import com.plivo.api.models.message.Message;

public class FetchMessageDetails {

    public static void fetchAndWriteMessageDetails(List<String> messageUUIDs, String authId, String authToken, String filePath) {
        Plivo.init(authId, authToken);

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String uuid : messageUUIDs) {
                try {
                    Message message = Message.getter(uuid).get();
                    writer.write("Message UUID: " + message.getMessageUuid() + "\n");
                    writer.write("From: " + message.getFromNumber() + "\n");
                    writer.write("To: " + message.getToNumber() + "\n");
                    writer.write("Status: " + message.getMessageState() + "\n");
                    writer.write("Error Code: " + message.getErrorCode() + "\n");
                    writer.write("Message Time: " + message.getMessageTime() + "\n\n");
                } catch (PlivoRestException | IOException e) {
                    e.printStackTrace();
                } catch (PlivoValidationException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
