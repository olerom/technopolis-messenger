package edu.technopolis.homework.messenger.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;

/**
 *
 */
public class MessengerServer {
    private int port;

    public static void main(String[] args) {
        new MessengerServer().run();
    }

    private void run() {
        initPort();

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new RunnableServerLogic(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPort() {
        try (InputStream input = new FileInputStream("./src/main/resources/portConfiguration.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            this.port = Integer.valueOf(properties.getProperty("port"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
