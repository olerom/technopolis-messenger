package edu.technopolis.homework.messenger.messages;

import java.util.List;

/**
 * Date: 25.04.17
 *
 * @author olerom
 */
public class Chat {
    private long id;
    private List<Long> messageIds;
    private List<Long> participantIds;
    private long adminId;

    public Chat(long id, List<Long> messageIds, List<Long> participantIds, long adminId) {
        this.id = id;
        this.messageIds = messageIds;
        this.participantIds = participantIds;
        this.adminId = adminId;
    }

    public void addMessage(Message message) {

    }

    public void addParticipant(User participant) {

    }

    public void removeParticipant(User participant) {

    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", messageIds=" + messageIds +
                ", participantIds=" + participantIds +
                '}';
    }
}
