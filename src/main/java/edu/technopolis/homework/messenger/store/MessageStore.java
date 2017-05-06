package edu.technopolis.homework.messenger.store;

import java.sql.SQLException;
import java.util.List;

import edu.technopolis.homework.messenger.messages.Chat;
import edu.technopolis.homework.messenger.messages.Message;

public interface MessageStore {
    /**
     * получаем список ид пользователей заданного чата
     */
    List<Long> getChatsByUserId(Long userId) throws SQLException;

    /**
     * получить информацию о чате
     */
    Chat getChatById(Long chatId) throws SQLException;

    /**
     * Список сообщений из чата
     */
    List<Long> getMessagesFromChat(Long chatId) throws SQLException;

    /**
     * Получить информацию о сообщении
     */
    Message getMessageById(Long messageId) throws SQLException;

    /**
     * Добавить сообщение в чат
     */
    void addMessage(Long chatId, Message message) throws SQLException;

    /**
     * Добавить пользователя к чату
     */
    void addUserToChat(Long userId, Long chatId) throws SQLException;

    /**
     * Создать чат от имени пользователя
     */
    void createChat(Long creatorId, List<Long> friends) throws SQLException;
}
