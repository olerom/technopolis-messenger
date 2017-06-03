package edu.technopolis.homework.messenger.store;

import java.sql.SQLException;
import java.util.List;

import edu.technopolis.homework.messenger.messages.Chat;
import edu.technopolis.homework.messenger.messages.Message;

public interface MessageStore {
    /**
     * Получаем список ид чатов заданного пользователя
     */

    List<String> getHistoryMessagesByChatId(Long chatId) throws SQLException;

    /**
     * Получаем список ид чатов заданного пользователя
     */
    List<Long> getChatsByUserId(Long userId) throws SQLException;

    /**
     * Получаем список ид пользователей заданного чата
     */
    List<Long> getUserIdsByChatId(Long chatId) throws SQLException;

    /**
     * Получить информацию о чате
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

    /**
     * Добавить админа чата в чат
     */
    void addAdminToChat(Long adminId, Long chatId) throws SQLException;
}
