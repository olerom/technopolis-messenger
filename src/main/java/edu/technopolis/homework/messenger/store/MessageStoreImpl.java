package edu.technopolis.homework.messenger.store;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.store.executor.Executor;

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
        return null;
    }

    @Override
    public void addMessage(Long chatId, Message message) {

    }

    @Override
    public void addUserToChat(Long userId, Long chatId) {

    }
}
