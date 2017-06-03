package edu.technopolis.homework.messenger.messages;

import java.util.List;

/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class ChatHistoryResultMessage extends Message {

    private List<String> messages;

    public ChatHistoryResultMessage() {
        super(null, Type.MSG_CHAT_HIST_RESULT);
    }

    public ChatHistoryResultMessage(List<String> messages) throws InstantiationException {
        super(null, Type.MSG_CHAT_HIST_RESULT);
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
