package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.Session;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class LoginCommand implements Command {
    @Override
    public void execute(Session session, Message message) throws CommandException {
        LoginMessage loginMessage = (LoginMessage) message;

    }
}
