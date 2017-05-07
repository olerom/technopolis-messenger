package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.*;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 08.05.17
 *
 * @author olerom
 */
public class UserInfoCommand implements Command {
    @Override
    public void execute(Session session, Message message, UserStore userStore, MessageStore messageStore, BlockingQueue<Session> sessions) throws CommandException {
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
            e.printStackTrace();
            try {
                session.send(new StatusMessage("Can't find user with this id"));
            } catch (ProtocolException | IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
