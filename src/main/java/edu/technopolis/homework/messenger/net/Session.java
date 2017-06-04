package edu.technopolis.homework.messenger.net;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.store.datasets.User;

import java.io.IOException;

/**
 * Date: 03.06.17
 *
 * @author olerom
 */
public interface Session {

    void send(Message msg) throws ProtocolException, IOException;

    void setUser(User user);

    User getUser();
}
