package edu.technopolis.homework.messenger.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.technopolis.homework.messenger.messages.*;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

/**
 * Простейший протокол передачи данных
 */
public class StringProtocol implements Protocol {

    private static final String DELIMITER = "@@@";
    private ObjectMapper mapper = new ObjectMapper();
    private static Logger LOGGER = LogManager.getLogger(StringProtocol.class.getName());

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        String decodedString = new String(bytes);
        LOGGER.log(Level.INFO, "Decoded: " + decodedString);

        String[] tokens = decodedString.split(DELIMITER);

        if (tokens.length != 2) {
            LOGGER.log(Level.WARN, "Invalid length of " + decodedString + ". Length should be 2.");
            throw new ProtocolException("Invalid length of " + decodedString + ". Length should be 2.");
        }

        Type type;
        try {
            type = Type.valueOf(tokens[0]);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARN, "Invalid type", e);

            throw new ProtocolException("Invalid type: " + tokens[0] +
                    ". Should be from edu.technopolis.homework.messenger.messages package");
        }

        String jsonMessage = tokens[1];
        switch (type) {
            case MSG_TEXT:
                try {
                    return mapper.readValue(jsonMessage, TextMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize TextMessage from json: "
                            + jsonMessage);
                }

            case MSG_LOGIN:
            case MSG_REGISTER:
                try {
                    return mapper.readValue(jsonMessage, LoginMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize LoginMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_CREATE:
                try {
                    return mapper.readValue(jsonMessage, ChatCreateMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize ChatCreateMessage from json: "
                            + jsonMessage);
                }

            case MSG_INFO_RESULT:
            case MSG_STATUS:
                try {
                    return mapper.readValue(jsonMessage, StatusMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize StatusMessage from json: "
                            + jsonMessage);
                }

            case MSG_INFO:
                try {
                    return mapper.readValue(jsonMessage, UserInfoMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize UserInfoMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_LIST:
                try {
                    return mapper.readValue(jsonMessage, ChatListMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize ChatListMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_LIST_RESULT:
                try {
                    return mapper.readValue(jsonMessage, ChatListResultMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize ChatListResultMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_HIST:
                try {
                    return mapper.readValue(jsonMessage, ChatHistoryMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize ChatHistoryMessage from json: "
                            + jsonMessage);
                }

            case MSG_CHAT_HIST_RESULT:
                try {
                    return mapper.readValue(jsonMessage, ChatHistoryResultMessage.class);
                } catch (IOException e) {
                    LOGGER.log(Level.WARN, "Invalid type", e);
                    throw new ProtocolException("Cannot deserialize ChatHistoryResultMessage from json: "
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
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize TextMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to TextMessage");
                }
                break;

            case MSG_LOGIN:
            case MSG_REGISTER:
                try {
                    LoginMessage loginMessage = (LoginMessage) msg;
                    encodedString.append(mapper.writeValueAsString(loginMessage));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize LoginMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to LoginMessage");
                }
                break;

            case MSG_CHAT_CREATE:
                try {
                    ChatCreateMessage chatCreateMessage = (ChatCreateMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatCreateMessage));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize ChatCreateMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to ChatCreateMessage");
                }
                break;

            case MSG_INFO_RESULT:
            case MSG_STATUS:
                try {
                    StatusMessage statusMessage = (StatusMessage) msg;
                    encodedString.append(mapper.writeValueAsString(statusMessage));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize StatusMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to StatusMessage");
                }
                break;

            case MSG_INFO:
                try {
                    UserInfoMessage userInfoMessage = (UserInfoMessage) msg;
                    encodedString.append(mapper.writeValueAsString(userInfoMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to UserInfoMessage");
                }
                break;

            case MSG_CHAT_LIST:
                try {
                    ChatListMessage chatListMessage = (ChatListMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatListMessage));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize ChatListMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to ChatListMessage");
                }
                break;

            case MSG_CHAT_LIST_RESULT:
                try {
                    ChatListResultMessage chatListResultMessage = (ChatListResultMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatListResultMessage));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize ChatListResultMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to ChatListResultMessage");
                }
                break;

            case MSG_CHAT_HIST:
                try {
                    ChatHistoryMessage chatHistoryMessage = (ChatHistoryMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatHistoryMessage));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize ChatHistoryMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to ChatHistoryMessage");
                }
                break;

            case MSG_CHAT_HIST_RESULT:
                try {
                    ChatHistoryResultMessage chatHistoryResultMessage = (ChatHistoryResultMessage) msg;
                    encodedString.append(mapper.writeValueAsString(chatHistoryResultMessage));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.WARN, "Serialization problems: ", e);
                    throw new ProtocolException("Cant serialize ChatHistoryResultMessage");
                } catch (ClassCastException classCastException) {
                    LOGGER.log(Level.WARN, "Class cast problems: ", classCastException);
                    throw new ProtocolException("Can't cast Message to ChatHistoryResultMessage");
                }
                break;

            default:
                LOGGER.log(Level.WARN, "Probably wrong type");
                throw new ProtocolException("Invalid type: " + type);
        }

        LOGGER.log(Level.INFO, "Encoded: " + encodedString);
        return encodedString.toString().getBytes();
    }
}