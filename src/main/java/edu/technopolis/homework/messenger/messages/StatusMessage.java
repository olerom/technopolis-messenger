package edu.technopolis.homework.messenger.messages;

/**
 * Date: 06.05.17
 *
 * @author olerom
 */
public class StatusMessage extends Message {
    private String info;

    public StatusMessage() {
    }

    public StatusMessage(String info){
        super(null, Type.MSG_STATUS);
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
