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
 * Date: 07.05.17
 *
 * @author olerom
 */
public class RegisterCommand implements Command {

    private static Logger LOGGER = LogManager.getLogger(RegisterCommand.class.getName());

    private UserStore userStore;

    public RegisterCommand(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {

        try {
            LoginMessage loginMessage = (LoginMessage) message;

            User tmpUser = new User(-1L, loginMessage.getLogin(), loginMessage.getPassword());
            tmpUser = userStore.addUser(tmpUser);
            session.setUser(tmpUser);

            session.send(new LoginMessage(tmpUser));
        } catch (SQLException | ProtocolException | IOException e) {
            LOGGER.log(Level.WARN, "Can't login user: ", e);
            try {
                session.send(new StatusMessage("Can't register user with this login and password"));
            } catch (ProtocolException | IOException e1) {
                LOGGER.log(Level.WARN, "Can't send message: ", e);
            }
        }

    }
}
