package edu.technopolis.homework.messenger.store;

import edu.technopolis.homework.messenger.User;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.TextMessage;
import edu.technopolis.homework.messenger.store.executor.Executor;

import java.sql.SQLException;
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
    public List<Long> getChatsByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Long> getMessagesFromChat(Long chatId) {
        return null;
    }

    @Override
    public Message getMessageById(Long messageId) {
//        return executor.execQuery("SELECT * FROM MESSAGE WHERE message_id='" + messageId + '\'',
//                result -> {
//                    result.next();
//                    return new TextMessage(result.getLong(1),
//                            result.getString(2), result.getString(3));
//                });
        return null;
    }

    @Override
    public void addMessage(Long chatId, Message message) throws SQLException {
        TextMessage textMessage = (TextMessage) message;
        executor.execUpdate("INSERT INTO MESSAGE (chat_id, owner_id, text) values ('"
                + chatId + "', '" + message.getOwnerId() + "', '"
                + textMessage.getText() + "')");

    }

    @Override
    public void addUserToChat(Long userId, Long chatId) throws SQLException {
        executor.execUpdate("INSERT INTO USER_CHAT (user_id, chat_id) values ('"
                + userId + "', '" + chatId + "')");
    }
}
