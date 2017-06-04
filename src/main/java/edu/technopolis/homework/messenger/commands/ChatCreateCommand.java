package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.ChatCreateMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 06.05.17
 *
 * @author olerom
 */
public class ChatCreateCommand implements Command {

    private static Logger LOGGER = LogManager.getLogger(ChatCreateCommand.class.getName());
    private MessageStore messageStore;

    public ChatCreateCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        if (!isLoggedIn(session)) {
            return;
        }

        try {
            ChatCreateMessage chatCreateMessage = (ChatCreateMessage) message;

            messageStore.createChat(message.getSenderId(), chatCreateMessage.getParticipants());
        } catch (ClassCastException e) {
            LOGGER.log(Level.WARN, "Can't cast message: ", e);
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't create chat: ", e);
        }
    }
}
