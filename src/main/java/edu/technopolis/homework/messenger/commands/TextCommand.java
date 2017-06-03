package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.messages.TextMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.net.SessionImpl;
import edu.technopolis.homework.messenger.store.MessageStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class TextCommand implements Command {

    private MessageStore messageStore;

    public TextCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    //    Looks really ugly
    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        TextMessage textMessage = (TextMessage) message;
        if (!isLoggedIn(session)) {
            return;
        }

        try {
            messageStore.addMessage(textMessage.getChatId(), textMessage);
        } catch (SQLException e) {
            System.out.println("Can't add message to DB");
            e.printStackTrace();
        }
        try {

            List<Long> recieverIds = messageStore.getUserIdsByChatId(message.getChatId());

//          ¯\_(ツ)_/¯
            for (Session participantSession : sessions) {
                for (long userId : recieverIds) {
                    if (participantSession.getUser() != null && participantSession.getUser().getId() == userId) {
                        try {
                            participantSession.send(textMessage);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StatusMessage statusMessage = new StatusMessage("Your message is delivered to chat "
                + message.getChatId());
        try {
            session.send(statusMessage);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
