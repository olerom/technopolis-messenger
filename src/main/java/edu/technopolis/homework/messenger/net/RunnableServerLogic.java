package edu.technopolis.homework.messenger.net;

import edu.technopolis.homework.messenger.store.Database;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public class RunnableServerLogic implements Runnable {
    private Socket clientSocket;
    private Protocol protocol;
    private Database database;

    public RunnableServerLogic(Socket clientSocket, Database database) {
        this.clientSocket = clientSocket;
        this.protocol = new StringProtocol();
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
                        System.out.println(protocol.decode(Arrays.copyOf(buffer, read)));
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
