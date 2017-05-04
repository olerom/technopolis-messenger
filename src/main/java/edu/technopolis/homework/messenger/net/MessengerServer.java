package edu.technopolis.homework.messenger.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 *
 */
public class MessengerServer {
    private static final int PORT = 19000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new RunnableServerLogic(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
