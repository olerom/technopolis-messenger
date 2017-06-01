package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.ChatCreateMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 06.05.17
 *
 * @author olerom
 */
public class ChatCreateCommand implements Command {

    private MessageStore messageStore;

    public ChatCreateCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        if (!isLoggedIn(session)) {
            return;
        }

        try {
            ChatCreateMessage chatCreateMessage = (ChatCreateMessage) message;

            messageStore.createChat(message.getSenderId(), chatCreateMessage.getParticipants());
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
