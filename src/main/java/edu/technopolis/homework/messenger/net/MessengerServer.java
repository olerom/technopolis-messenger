package edu.technopolis.homework.messenger.net;

import edu.technopolis.homework.messenger.store.Database;
import edu.technopolis.homework.messenger.store.DatabaseImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        Database database = new DatabaseImpl();
        try {
            database.initMessages();
            database.initUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            List<Thread> socketThreads = new ArrayList<>();

            while (true) {
                Socket socket = serverSocket.accept();

                Thread clientSocket = new Thread(new RunnableServerLogic(socket, database));
                socketThreads.add(clientSocket);
                clientSocket.start();
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
