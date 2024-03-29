package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
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
            } catch (ProtocolException | IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}