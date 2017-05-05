package edu.technopolis.homework.messenger.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.technopolis.homework.messenger.User;
import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.TextMessage;
import edu.technopolis.homework.messenger.messages.Type;

import java.io.IOException;

/**
 * Простейший протокол передачи данных
 */
public class StringProtocol implements Protocol {

    public static final String DELIMITER = "~";

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        String str = new String(bytes);
        System.out.println("decoded: " + str);
        String[] tokens = str.split(DELIMITER);

        Type type = Type.valueOf(tokens[0]);
        ObjectMapper mapper = new ObjectMapper();
        switch (type) {
            case MSG_TEXT:
                try {
                    return mapper.readValue(tokens[1], TextMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case MSG_LOGIN:
                try {
                    return mapper.readValue(tokens[1], LoginMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                throw new ProtocolException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        StringBuilder builder = new StringBuilder();
        Type type = msg.getType();
        builder.append(type).append(DELIMITER);
        ObjectMapper mapper = new ObjectMapper();
        switch (type) {
            case MSG_TEXT:
                TextMessage sendMessage = (TextMessage) msg;
                try {
                    builder.append(mapper.writeValueAsString(sendMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;

            case MSG_LOGIN:
                LoginMessage loginMessage = (LoginMessage) msg;
                try {
                    builder.append(mapper.writeValueAsString(loginMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new ProtocolException("Invalid type: " + type);


        }
        System.out.println("encoded: " + builder);
        return builder.toString().getBytes();
    }

    private Long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            System.err.println("Error with parsing long");
            e.printStackTrace();
        }
        return null;
    }
}