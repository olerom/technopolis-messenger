package edu.technopolis.homework.messenger.messages;

import edu.technopolis.homework.messenger.User;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public class LoginMessage extends Message {

    private String login;
    private String password;

    public LoginMessage(User owner, String login, String password){
        super(owner.getId(), Type.MSG_LOGIN);

        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
