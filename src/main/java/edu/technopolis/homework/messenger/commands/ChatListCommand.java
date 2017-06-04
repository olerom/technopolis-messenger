package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.ChatListMessage;
import edu.technopolis.homework.messenger.messages.ChatListResultMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class ChatListCommand implements Command {

    private static Logger LOGGER = LogManager.getLogger(ChatListCommand.class.getName());
    private MessageStore messageStore;

    public ChatListCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        if (!isLoggedIn(session)) {
            return;
        }

        try {
            ChatListMessage chatListMessage = (ChatListMessage) message;
            List<Long> chats = messageStore.getChatsByUserId(chatListMessage.getSenderId());
            Message chatList = new ChatListResultMessage(chats);

            session.send(chatList);

        } catch (SQLException | ProtocolException | IOException e) {
            LOGGER.log(Level.WARN, "Can't chat ids: ", e);
            try {
                session.send(new StatusMessage("Can't get chats for you."));
            } catch (ProtocolException | IOException e1) {
                LOGGER.log(Level.WARN, "Can't send message: ", e);
            }
        } catch (ClassCastException e) {
            LOGGER.log(Level.WARN, "Can't cast message: ", e);
        }

    }
}
