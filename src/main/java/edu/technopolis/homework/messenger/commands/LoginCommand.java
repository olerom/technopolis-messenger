package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.UserStore;
import edu.technopolis.homework.messenger.store.datasets.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class LoginCommand implements Command {

    private static Logger LOGGER = LogManager.getLogger(LoginCommand.class.getName());

    private UserStore userStore;

    public LoginCommand(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {

        try {
            LoginMessage loginMessage = (LoginMessage) message;
            User user = userStore.getUser(loginMessage.getLogin(), loginMessage.getPassword());
            session.setUser(user);
            loginMessage.setSenderId(user.getId());

            session.send(loginMessage);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Can't find user with this login and password: "
                    + ((LoginMessage) message).getLogin() + ", "
                    + ((LoginMessage) message).getPassword(), e);

            StatusMessage statusMessage = new StatusMessage(
                    "Can't find user with this login and password");

            try {
                session.send(statusMessage);
            } catch (ProtocolException | IOException e1) {
                LOGGER.log(Level.WARN, "Can't send message: ", e1);
            }

        } catch (ProtocolException | IOException e) {
            LOGGER.log(Level.WARN, "Can't manage this command: ", e);
        }
    }
}
