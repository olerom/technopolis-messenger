package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.messages.User;
import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.net.SessionImpl;
import edu.technopolis.homework.messenger.store.UserStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class LoginCommand implements Command {
    private UserStore userStore;

    public LoginCommand(UserStore userStore) {
        this.userStore = userStore;
    }

    //    TODO : fix
    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {

        try {
            LoginMessage loginMessage = (LoginMessage) message;
            User user = userStore.getUser(loginMessage.getLogin(), loginMessage.getPassword());
            session.setUser(user);
            loginMessage.setSenderId(user.getId());

            session.send(loginMessage);
        } catch (SQLException e) {
            StatusMessage statusMessage = new StatusMessage(
                    "Can't find user with this login and password");

            try {
                session.send(statusMessage);
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (ProtocolException | IOException e) {
            e.printStackTrace();
        }
    }
}
