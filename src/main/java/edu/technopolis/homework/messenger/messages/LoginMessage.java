package edu.technopolis.homework.messenger.messages;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public class LoginMessage extends Message {

    private String login;
    private String password;

    public LoginMessage(){

    }

    public LoginMessage(User owner, String login, String password) {
        super(owner, Type.MSG_LOGIN);

        this.login = login;
        this.password = password;
    }

    public LoginMessage(Long ownerId, Long receiverId, Type type, String login, String password) {
        super(ownerId, receiverId, Type.MSG_LOGIN);

        if (type != Type.MSG_LOGIN)
            System.out.println("Smth goes wrong");

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

    @Override
    public String toString() {
        return "LoginMessage{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}' + "\n" + super.toString();
    }
}
