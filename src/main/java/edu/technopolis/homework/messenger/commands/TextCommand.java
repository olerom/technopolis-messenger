package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.User;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.TextMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class TextCommand implements Command {

//    Hollyshitt
    @Override
    public void execute(Session session, Message message, UserStore userStore,
                        MessageStore messageStore, ConcurrentLinkedQueue<Session> sessions) throws CommandException {
        TextMessage textMessage = (TextMessage) message;
        User user = session.getUser();

        try {
            messageStore.addMessage(textMessage.getReceiverId(), textMessage);
        } catch (SQLException e) {
            System.out.println("Can't add message to DB");
            e.printStackTrace();
        }

        for (Session session1 : sessions) {
            if (session1.getUser() != null && session1.getUser().getId() == message.getReceiverId()) {
                try {
                    session1.send(message);
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            session.send(message);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
