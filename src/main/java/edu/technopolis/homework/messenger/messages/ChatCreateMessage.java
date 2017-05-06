package edu.technopolis.homework.messenger.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 06.05.17
 *
 * @author olerom
 */
public class ChatCreateMessage extends Message {
    private List<Long> participants = new ArrayList<>();

    public List<Long> getParticipants() {
        return participants;
    }

    public ChatCreateMessage() {
    }

    public ChatCreateMessage(User creator, List<Long> participants) throws InstantiationException {
        super(creator, Type.MSG_CHAT_CREATE);

        this.participants = participants;
    }
}
