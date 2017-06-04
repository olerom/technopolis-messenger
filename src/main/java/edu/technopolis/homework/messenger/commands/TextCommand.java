package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.messages.TextMessage;
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
 * Date: 05.05.17
 *
 * @author olerom
 */
public class TextCommand implements Command {

    private static Logger LOGGER = LogManager.getLogger(TextCommand.class.getName());
    private MessageStore messageStore;

    public TextCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    //    Looks really ugly
    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        TextMessage textMessage = (TextMessage) message;
        if (!isLoggedIn(session)) {
            return;
        }

        try {
            messageStore.addMessage(textMessage.getChatId(), textMessage);

            List<Long> receiverIds = messageStore.getUserIdsByChatId(message.getChatId());

            ChatManager chatManager = new ChatManager(sessions, receiverIds, textMessage);

            new Thread(chatManager).start();

            StatusMessage statusMessage = new StatusMessage("Your message is delivered to chat "
                    + message.getChatId());

            session.send(statusMessage);
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't add message to DB: ", e);
        } catch (ProtocolException | IOException e) {
            LOGGER.log(Level.WARN, "Can't send message: ", e);
        }
    }
}
