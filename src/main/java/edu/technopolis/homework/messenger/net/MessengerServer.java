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
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class MessengerServer {
    private int port;
    private ConcurrentLinkedQueue<Session> sessions = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        new MessengerServer().run();
    }

    private void run() {
        initPort();
        Database database = new DatabaseImpl();
        try {
            database.initMessages();
            database.initUsers();
            database.initUserChat();
            database.initAdminChat();
//            database.dropMessages();
//            database.dropUsers();
        } catch (SQLException e) {
            System.out.println("Couldn't create tables");
            e.printStackTrace();
            System.exit(1);

        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();

                Thread clientSocket = new Thread(new RunnableServerLogic(socket, database.getExecutor(), sessions));
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
            System.out.println("Couldn't read properties");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
