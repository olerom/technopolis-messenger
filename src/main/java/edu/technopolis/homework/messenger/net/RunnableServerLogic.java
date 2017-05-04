package edu.technopolis.homework.messenger.net;

import edu.technopolis.homework.messenger.User;
import edu.technopolis.homework.messenger.store.*;
import edu.technopolis.homework.messenger.store.executor.Executor;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public class RunnableServerLogic implements Runnable {
    private Socket clientSocket;
    private Protocol protocol;
    private UserStore userStore;
    private MessageStore messageStore;

    public RunnableServerLogic(Socket clientSocket, Executor executor) {
        this.clientSocket = clientSocket;
        this.protocol = new StringProtocol();
        this.userStore = new UserStoreImpl(executor);
        this.messageStore = new MessageStoreImpl(executor);
    }

    @Override
    public void run() {
        try {
            InputStream in = clientSocket.getInputStream();
            while (true) {
                final byte[] buffer = new byte[1024 * 64];
                int read = in.read(buffer);
                if (read > 0) {
                    try {
                        userStore.addUser(new User(0, "oo", "oo"));
                        System.out.println(userStore.getUser("oo", "oo"));
                        System.out.println(protocol.decode(Arrays.copyOf(buffer, read)));
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
