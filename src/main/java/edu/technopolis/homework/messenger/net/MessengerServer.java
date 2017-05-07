package edu.technopolis.homework.messenger.net;

import edu.technopolis.homework.messenger.store.Database;
import edu.technopolis.homework.messenger.store.DatabaseImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class MessengerServer {
    private int port;
    private BlockingQueue<Session> sessions = new ArrayBlockingQueue<>(100);
    private ServerSocket serverSocket;

    public void setPort(int port) {
        this.port = port;
    }

    public void setSessions(BlockingQueue<Session> sessions) {
        this.sessions = sessions;
    }

    private void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    private ServerSocket getServerSocket() {
        return serverSocket;
    }

    private BlockingQueue<Session> getSessions() {
        return sessions;
    }

    public static void main(String[] args) {
        MessengerServer messengerServer = new MessengerServer();
        messengerServer.startUp();
    }

    public void startUp() {
        initPort();
        Database database = new DatabaseImpl();

        try {
            database.initMessages();
            database.initUsers();
            database.initUserChat();
            database.initAdminChat();
//            database.dropMessages();
//            database.dropUsers();
//            database.dropUserChat();
//            database.dropAdminChat();
        } catch (SQLException e) {
            System.out.println("Couldn't create tables. Quitting...");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            setServerSocket(new ServerSocket(port));
            ExecutorService executorService = Executors.newCachedThreadPool();
            while (true) {
                Socket socket = getServerSocket().accept();

                executorService.submit(new Thread(new RunnableServerLogic(socket,
                        database.getExecutor(), getSessions())));
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
