package edu.technopolis.homework.messenger.messages;

import edu.technopolis.homework.messenger.store.datasets.User;

/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class ChatListMessage extends Message {

    public ChatListMessage() {
        super(null, Type.MSG_CHAT_LIST_RESULT);
    }

    public ChatListMessage(User creator) throws InstantiationException {
        super(creator, Type.MSG_CHAT_LIST);
    }
}
