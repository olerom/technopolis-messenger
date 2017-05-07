package edu.technopolis.homework.messenger.messages;

/**
 * Date: 08.05.17
 *
 * @author olerom
 */
public class UserInfoMessage extends Message {
    private Long userId;

    public UserInfoMessage() {
        super(null, Type.MSG_INFO);
    }

    public UserInfoMessage(User sender, Long userId) {
        super(sender, Type.MSG_INFO);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
