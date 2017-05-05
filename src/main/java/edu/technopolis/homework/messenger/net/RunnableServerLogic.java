package edu.technopolis.homework.messenger.net;

import edu.technopolis.homework.messenger.User;
import edu.technopolis.homework.messenger.commands.Command;
import edu.technopolis.homework.messenger.commands.CommandFactory;
import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
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
        Session session = new Session(clientSocket);
        try {
            InputStream in = clientSocket.getInputStream();
            while (true) {
                final byte[] buffer = new byte[1024 * 64];
                int read = in.read(buffer);
                if (read > 0) {
                    try {

                        Message msg = protocol.decode(Arrays.copyOf(buffer, read));

                        Command command = new CommandFactory().get(msg.getType());

                        command.execute(session, msg);

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
