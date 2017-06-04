package edu.technopolis.homework.messenger.store.datasets;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.store.datasets.User;

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

    public long getId() {
        return id;
    }

    public List<Long> getMessageIds() {
        return messageIds;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public long getAdminId() {
        return adminId;
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
