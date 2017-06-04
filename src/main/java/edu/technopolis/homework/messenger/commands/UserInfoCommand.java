package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.*;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.UserStore;
import edu.technopolis.homework.messenger.store.datasets.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Date: 08.05.17
 *
 * @author olerom
 */
public class UserInfoCommand implements Command {

    private static Logger LOGGER = LogManager.getLogger(UserInfoCommand.class.getName());
    private UserStore userStore;

    public UserInfoCommand(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        if (!isLoggedIn(session)) {
            return;
        }

        try {
            UserInfoMessage userInfoMessage = (UserInfoMessage) message;

            User user;
            if (userInfoMessage.getUserId() == null) {
                user = userStore.getUserById(session.getUser().getId());
            } else {
                user = userStore.getUserById(userInfoMessage.getUserId());
            }

            Message info = new StatusMessage(user.toString());
            info.setType(Type.MSG_INFO_RESULT);

            session.send(info);
        } catch (SQLException | ProtocolException | IOException e) {
            LOGGER.log(Level.WARN, "Can't get user info: ", e);
            try {
                session.send(new StatusMessage("Can't find user with this id"));
            } catch (ProtocolException | IOException e1) {
                LOGGER.log(Level.WARN, "Can't send message: ", e1);
            }
        }

    }
}
