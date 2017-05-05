package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class TextCommand implements Command {
    @Override
    public void execute(Session session, Message message, UserStore userStore, MessageStore messageStore) throws CommandException {

    }
}
