package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.User;
import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;
import edu.technopolis.homework.messenger.store.executor.Executor;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class LoginCommand implements Command {
    @Override
    public void execute(Session session, Message message, UserStore userStore, MessageStore messageStore) throws CommandException {
        LoginMessage loginMessage = (LoginMessage) message;
        User user;
        try {
            user = userStore.getUser(loginMessage.getLogin(), loginMessage.getPassword());
            session.setUser(user);
            message.setOwnerId(user.getId());
        } catch (SQLException e) {
            User tmpUser = new User(-1L, loginMessage.getLogin(), loginMessage.getPassword());
            try {
                userStore.addUser(tmpUser);
                user = userStore.getUser(loginMessage.getLogin(), loginMessage.getPassword());
                session.setUser(user);
                message.setOwnerId(user.getId());
            } catch (SQLException e1) {
                e1.printStackTrace();
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
