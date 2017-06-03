package edu.technopolis.homework.messenger.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.technopolis.homework.messenger.messages.*;

import java.io.IOException;

/**
 * Простейший протокол передачи данных
 */
public class StringProtocol implements Protocol {

    private static final String DELIMITER = "@@@";
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        String decodedString = new String(bytes);
        System.err.println("Decoded: " + decodedString);

        String[] tokens = decodedString.split(DELIMITER);

        if (tokens.length != 2) {
            throw new ProtocolException("Invalid length of " + decodedString + ". Length should be 2.");
        }

        Type type;
        try {
            type = Type.valueOf(tokens[0]);
        } catch (IllegalArgumentException e) {
            throw new ProtocolException("Invalid type: " + tokens[0] +
                    ". Should be from edu.technopolis.homework.messenger.messages package");
        }

        String jsonMessage = tokens[1];
        switch (type) {
            case MSG_TEXT:
                try {
                    return mapper.readValue(jsonMessage, TextMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cannot deserialize TextMessage from json: "
                            + jsonMessage);
                }

            case MSG_LOGIN:
            case MSG_REGISTER:
                try {
                    return mapper.readValue(jsonMessage, LoginMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cannot deserialize LoginMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_CREATE:
                try {
                    return mapper.readValue(jsonMessage, ChatCreateMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cannot deserialize ChatCreateMessage from json: "
                            + jsonMessage);
                }

            case MSG_INFO_RESULT:
            case MSG_STATUS:
                try {
                    return mapper.readValue(jsonMessage, StatusMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cannot deserialize StatusMessage from json: "
                            + jsonMessage);
                }

            case MSG_INFO:
                try {
                    return mapper.readValue(jsonMessage, UserInfoMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cannot deserialize UserInfoMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_LIST:
                try {
                    return mapper.readValue(jsonMessage, ChatListMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cannot deserialize ChatListMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_LIST_RESULT:
                try {
                    return mapper.readValue(jsonMessage, ChatListResultMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cannot deserialize ChatListMessageResult from json: "
                            + jsonMessage);
                }

            default:
                throw new ProtocolException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        StringBuilder encodedString = new StringBuilder();

        Type type = msg.getType();
        encodedString.append(type).append(DELIMITER);

        switch (type) {
            case MSG_TEXT:
                try {
                    TextMessage sendMessage = (TextMessage) msg;
                    encodedString.append(mapper.writeValueAsString(sendMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cant serialize TextMessage");
                } catch (ClassCastException classCastException) {
                    classCastException.printStackTrace();
                    throw new ProtocolException("Can't cast Message to TextMessage");
                }
                break;

            case MSG_LOGIN:
            case MSG_REGISTER:
                try {
                    LoginMessage loginMessage = (LoginMessage) msg;
                    encodedString.append(mapper.writeValueAsString(loginMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cant serialize LoginMessage");
                } catch (ClassCastException classCastException) {
                    classCastException.printStackTrace();
                    throw new ProtocolException("Can't cast Message to LoginMessage");
                }
                break;

            case MSG_CHAT_CREATE:
                try {
                    ChatCreateMessage chatCreateMessage = (ChatCreateMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatCreateMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cant serialize ChatCreateMessage");
                } catch (ClassCastException classCastException) {
                    classCastException.printStackTrace();
                    throw new ProtocolException("Can't cast Message to ChatCreateMessage");
                }
                break;

            case MSG_INFO_RESULT:
            case MSG_STATUS:
                try {
                    StatusMessage statusMessage = (StatusMessage) msg;
                    encodedString.append(mapper.writeValueAsString(statusMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cant serialize StatusMessage");
                } catch (ClassCastException classCastException) {
                    classCastException.printStackTrace();
                    throw new ProtocolException("Can't cast Message to StatusMessage");
                }
                break;

            case MSG_INFO:
                try {
                    UserInfoMessage userInfoMessage = (UserInfoMessage) msg;
                    encodedString.append(mapper.writeValueAsString(userInfoMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cant serialize UserInfoMessage");
                } catch (ClassCastException classCastException) {
                    classCastException.printStackTrace();
                    throw new ProtocolException("Can't cast Message to UserInfoMessage");
                }
                break;

            case MSG_CHAT_LIST:
                try {
                    ChatListMessage chatListMessage = (ChatListMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatListMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cant serialize ChatListMessage");
                } catch (ClassCastException classCastException) {
                    classCastException.printStackTrace();
                    throw new ProtocolException("Can't cast Message to ChatListMessage");
                }
                break;

            case MSG_CHAT_LIST_RESULT:
                try {
                    ChatListResultMessage chatListResultMessage = (ChatListResultMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatListResultMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new ProtocolException("Cant serialize ChatListResultMessage");
                } catch (ClassCastException classCastException) {
                    classCastException.printStackTrace();
                    throw new ProtocolException("Can't cast Message to ChatListResultMessage");
                }
                break;

            default:
                throw new ProtocolException("Invalid type: " + type);
        }

        System.err.println("Encoded: " + encodedString);
        return encodedString.toString().getBytes();
    }
}