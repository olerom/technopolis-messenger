package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.*;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.store.MessageStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.List;

/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class ChatListCommand implements Command {

    private MessageStore messageStore;

    public ChatListCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute(Session session, Message message, BlockingQueue<Session> sessions) throws CommandException {
        ChatListMessage chatListMessage = (ChatListMessage) message;


        try {
            List<Long> chats = messageStore.getChatsByUserId(chatListMessage.getSenderId());
            Message chatList = new ChatListResultMessage(chats);

            session.send(chatList);

        } catch (SQLException | ProtocolException | IOException e) {
            e.printStackTrace();
            try {
                session.send(new StatusMessage("Can't get chats for you."));
            } catch (ProtocolException | IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
