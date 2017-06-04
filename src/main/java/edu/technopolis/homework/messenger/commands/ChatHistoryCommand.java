package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.ChatHistoryMessage;
import edu.technopolis.homework.messenger.messages.ChatHistoryResultMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.StatusMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class ChatHistoryCommand implements Command {
    private static Logger LOGGER = LogManager.getLogger(ChatHistoryCommand.class.getName());

    private MessageStore messageStore;

    public ChatHistoryCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        if (!isLoggedIn(session)) {
            return;
        }
        try {
            ChatHistoryMessage chatHistoryMessage = (ChatHistoryMessage) message;

            Long chatId = chatHistoryMessage.getChatId();
            List<String> historyMessages = this.messageStore.getHistoryMessagesByChatId(chatId);

            ChatHistoryResultMessage chatHistoryResultMessage = new ChatHistoryResultMessage(historyMessages);

            session.send(chatHistoryResultMessage);
        } catch (ClassCastException e) {
            LOGGER.log(Level.WARN, "Can't cast message: ", e);
        } catch (SQLException | InstantiationException | ProtocolException | IOException e) {
            LOGGER.log(Level.WARN, "Can't get history: ", e);
            try {
                session.send(new StatusMessage("Can't find history for your chat"));
            } catch (ProtocolException | IOException e1) {
                LOGGER.log(Level.WARN, "Can't send message: ", e);
            }
        }


    }
}
