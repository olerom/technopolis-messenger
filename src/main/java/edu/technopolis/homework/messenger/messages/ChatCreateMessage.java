package edu.technopolis.homework.messenger.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public ChatCreateMessage(User creator, String friends) throws InstantiationException {
        super(creator, Type.MSG_CHAT_CREATE);

        for (String participant : friends.split(",")) {
            participants.add(Long.valueOf(participant));
        }
    }
}
