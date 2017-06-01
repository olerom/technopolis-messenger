package edu.technopolis.homework.messenger.net;

import edu.technopolis.homework.messenger.commands.Command;
import edu.technopolis.homework.messenger.commands.CommandFactory;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.store.*;
import edu.technopolis.homework.messenger.store.executor.Executor;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    private BlockingQueue<Session> sessions;


    public RunnableServerLogic(Socket clientSocket, Executor executor, BlockingQueue<Session> sessions) {
        this.clientSocket = clientSocket;
        this.protocol = new StringProtocol();
        this.userStore = new UserStoreImpl(executor);
        this.messageStore = new MessageStoreImpl(executor);
        this.sessions = sessions;
    }

    @Override
    public void run() {
        Session session = new Session(clientSocket);
        sessions.add(session);
        try {
            InputStream in = clientSocket.getInputStream();
            while (true) {
                final byte[] buffer = new byte[1024 * 64];
                int read = in.read(buffer);
                if (read > 0) {
                    try {

                        Message msg = protocol.decode(Arrays.copyOf(buffer, read));

                        Command command = new CommandFactory(userStore, messageStore).get(msg.getType());

                        command.execute(session, msg, sessions);

//                        System.out.println(messageStore.getChatById(msg.getChatId()));
                    } catch (ProtocolException e) {
                        e.printStackTrace();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
