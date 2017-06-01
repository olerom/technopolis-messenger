package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public interface Command {

    /**
     * Реализация паттерна Команда. Метод execute() вызывает соответствующую реализацию,
     * для запуска команды нужна сессия, чтобы можно было сгенерить ответ клиенту и провести валидацию
     * сессии.
     *
     * @param session - текущая сессия
     * @param message - сообщение для обработки
     * @throws CommandException - все исключения перебрасываются как CommandException
     */
    void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException;

    default boolean isLoggedIn(Session session) {
        if (session.getUser() == null) {
            try {
                session.send(new StatusMessage("You are not logged in"));
                return false;
            } catch (ProtocolException | IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}