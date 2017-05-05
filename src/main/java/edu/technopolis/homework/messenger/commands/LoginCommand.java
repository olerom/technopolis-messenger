package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.User;
import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;

import java.io.IOException;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class LoginCommand implements Command {
    @Override
    public void execute(Session session, Message message) throws CommandException {
        LoginMessage loginMessage = (LoginMessage) message;
        session.setUser(new User(1, loginMessage.getLogin(), loginMessage.getPassword()));

        try {
            session.send(message);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
