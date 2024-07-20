package com.plivo.Scenario1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.plivo.api.Plivo;
import com.plivo.api.exceptions.InvalidRequestException;
import com.plivo.api.exceptions.PlivoRestException;
import com.plivo.api.models.message.MessageCreateResponse;

public class SendSMS {

    public static List<String> sendSMS(List<Message> messages, String authId, String authToken) {
        Plivo.init(authId, authToken);
        List<String> messageUUIDs = new ArrayList<>();

        for (Message message : messages) {
            try {
                validatePhoneNumber(message.src);
                validatePhoneNumber(message.dst);

                MessageCreateResponse response = com.plivo.api.models.message.Message.creator(message.src, message.dst, message.text)
                        .create();
                System.out.println("Message UUID: " + response.getMessageUuid());
                messageUUIDs.add(response.getMessageUuid().get(0));
            } catch (InvalidRequestException e) {
                throw new IllegalArgumentException("Invalid phone number format: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("SMS sending failed: " + e.getMessage(), e);
            } catch (PlivoRestException e) {
                throw new RuntimeException(e);
            }
        }
        return messageUUIDs;
    }

    private static void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^\\+?[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }
    }
}
