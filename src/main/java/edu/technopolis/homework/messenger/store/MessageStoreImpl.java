package edu.technopolis.homework.messenger.store;

import edu.technopolis.homework.messenger.messages.Chat;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.TextMessage;
import edu.technopolis.homework.messenger.store.executor.Executor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public class MessageStoreImpl implements MessageStore {

    private final Executor executor;

    public MessageStoreImpl(Executor executor) {
        this.executor = executor;
    }

    @Override
    public List<String> getHistoryMessagesByChatId(Long chatId) throws SQLException {
        return executor.execQuery("SELECT * FROM MESSAGE WHERE chat_id='" + chatId + '\'', result -> {
            ArrayList<String> historyMessages = new ArrayList<>();
            while (!result.isLast()) {
                result.next();
                String fullMessage = "";
                fullMessage += result.getLong(3) + " wrote: ";
                fullMessage += result.getString(4);
                historyMessages.add(fullMessage);
            }
            return historyMessages;
        });
    }

    @Override
    public List<Long> getChatsByUserId(Long userId) throws SQLException {
        return executor.execQuery("SELECT chat_id FROM USER_CHAT WHERE user_id='" + userId + '\'', result -> {
            ArrayList<Long> chats = new ArrayList<>();
            while (!result.isLast()) {
                result.next();
                chats.add((result.getLong(1)));
            }
            return chats;
        });
    }

    @Override
    public List<Long> getUserIdsByChatId(Long chatId) throws SQLException {
        return executor.execQuery("SELECT user_id FROM USER_CHAT WHERE chat_id='" + chatId + '\'', result -> {
            ArrayList<Long> users = new ArrayList<>();
            while (!result.isLast()) {
                result.next();
                users.add((result.getLong(1)));
            }
            return users;
        });
    }

    @Override
    public Chat getChatById(Long chatId) throws SQLException {
        ArrayList<Long> participants = new ArrayList<>();

        executor.execQuery("SELECT user_id FROM USER_CHAT WHERE chat_id='" + chatId + '\'', result -> {
            while (!result.isLast()) {
                result.next();
                participants.add((result.getLong(1)));
            }
            return null;
        });

        ArrayList<Long> messages = new ArrayList<>();

        executor.execQuery("SELECT message_id FROM MESSAGE WHERE chat_id='" + chatId + '\'', result -> {
            while (!result.isLast()) {
                result.next();
                messages.add((result.getLong(1)));
            }
            return null;
        });

        long adminId = executor.execQuery("SELECT user_id FROM ADMIN_CHAT WHERE chat_id='" + chatId + '\'', result -> {
            result.next();
            return (result.getLong(1));
        });

        return new Chat(chatId, messages, participants, adminId);
    }

    @Override
    public List<Long> getMessagesFromChat(Long chatId) throws SQLException {
        return executor.execQuery("SELECT message_id FROM MESSAGE WHERE chat_id='" + chatId + '\'', result -> {
            ArrayList<Long> messages = new ArrayList<>();
            while (!result.isLast()) {
                result.next();
                messages.add((result.getLong(1)));
            }
            return messages;
        });
    }

    @Override
    public Message getMessageById(Long messageId) throws SQLException {
        return executor.execQuery("SELECT owner_id, chat_id, text FROM MESSAGE WHERE message_id='" + messageId + '\'', result -> {
            result.next();
            return new TextMessage(result.getLong(1),
                    result.getLong(2), result.getString(3));
        });
    }

    @Override
    public void addMessage(Long chatId, Message message) throws SQLException {
        TextMessage textMessage = (TextMessage) message;
        executor.execUpdate("INSERT INTO MESSAGE (chat_id, owner_id, text) values ('"
                + chatId + "', '" + message.getSenderId() + "', '"
                + textMessage.getText() + "')");

    }

    @Override
    public void addUserToChat(Long userId, Long chatId) throws SQLException {
        executor.execUpdate("INSERT INTO USER_CHAT (user_id, chat_id) values ('"
                + userId + "', '" + chatId + "')");
    }

    @Override
    public void createChat(Long creatorId, List<Long> friends) throws SQLException {
        long chatId = maxChatId();
        chatId++;

        executor.execUpdate("INSERT INTO ADMIN_CHAT (user_id, chat_id) values ('"
                + creatorId + "', '" + chatId + "')");

        executor.execUpdate("INSERT INTO USER_CHAT (user_id, chat_id) values ('"
                + creatorId + "', '" + chatId + "')");

        for (long friend : friends) {
            executor.execUpdate("INSERT INTO USER_CHAT (user_id, chat_id) values ('"
                    + friend + "', '" + chatId + "')");
        }
    }

    @Override
    public void addAdminToChat(Long adminId, Long chatId) throws SQLException {
        executor.execUpdate("INSERT INTO ADMIN_CHAT (user_id, chat_id) values ('"
                + adminId + "', '" + chatId + "')");
    }

    private long maxChatId() {
        try {
            return executor.execQuery("SELECT MAX(chat_id) FROM ADMIN_CHAT", result -> {
                result.next();
                return result.getLong(1);
            });
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Table is probably empty");
            return 0L;
        }
    }


}
