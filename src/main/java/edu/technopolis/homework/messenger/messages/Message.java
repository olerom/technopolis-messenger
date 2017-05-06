package edu.technopolis.homework.messenger.messages;

import java.io.Serializable;

/**
 *
 */
public abstract class Message implements Serializable {

    private Long ownerId;
    private Long receiverId;
    private Type type;

    public Message(){

    }

    public Message(User owner, Long receiverId, Type type) {
        if (owner != null)
            this.ownerId = owner.getId();

        this.receiverId = receiverId;
        this.type = type;
    }

    public Message(Long ownerid, Long receiverId, Type type) {
        this.ownerId = ownerid;
        this.receiverId = receiverId;
        this.type = type;
    }

    public Message(User owner, Type type) {
        if (owner != null)
            this.ownerId = owner.getId();

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

    @Override
    public String toString() {
        return "Message{" +
                "ownerId=" + ownerId +
                ", receiverId=" + receiverId +
                ", type=" + type +
                '}';
    }
}
