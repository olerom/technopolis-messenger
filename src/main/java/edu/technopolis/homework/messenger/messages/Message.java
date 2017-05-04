package edu.technopolis.homework.messenger.messages;

import java.io.Serializable;

/**
 *
 */
public abstract class Message implements Serializable {

    private Long ownerId;
    private Long receiverId;
    private Type type;

    public Message(Long ownerId, Long receiverId, Type type) {
        this.ownerId = ownerId;
        this.receiverId = receiverId;
        this.type = type;
    }

    public Message(Long ownerId, Type type) {
        this.ownerId = ownerId;
        this.type = type;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
