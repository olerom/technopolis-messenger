package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.ChatCreateMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Date: 06.05.17
 *
 * @author olerom
 */
public class ChatCreateCommand implements Command {
    @Override
    public void execute(Session session, Message message, UserStore userStore, MessageStore messageStore, ConcurrentLinkedQueue<Session> sessions) throws CommandException {
        ChatCreateMessage chatCreateMessage = (ChatCreateMessage) message;

        try {
            messageStore.createChat(message.getSenderId(), chatCreateMessage.getParticipants());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
