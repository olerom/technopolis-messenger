package edu.technopolis.homework.messenger.messages;

/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class ChatHistoryMessage extends Message {

    private Long chatId;

    public ChatHistoryMessage() {
        super(null, Type.MSG_CHAT_HIST);
    }

    public ChatHistoryMessage(User creator, Long chatId) throws InstantiationException {
        super(creator, Type.MSG_CHAT_HIST);
        super.setChatId(chatId);
    }

    public Long getChatId() {
        return super.getChatId();
    }
}
