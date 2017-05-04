package edu.technopolis.homework.messenger.teacher.client;


import edu.technopolis.homework.messenger.User;
import edu.technopolis.homework.messenger.messages.LoginMessage;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.TextMessage;
import edu.technopolis.homework.messenger.messages.Type;
import edu.technopolis.homework.messenger.net.Protocol;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.StringProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;


/**
 *
 */
public class MessengerClient {

    private static final int PORT = 19000;
    private static final String HOST = "localhost";

    /**
     * Протокол, хост и порт инициализируются из конфига
     */
    private Protocol protocol;
    private int port;
    private String host;
    private User user;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void initSocket() throws IOException {
        Socket socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        // Тред "слушает" сокет на наличие входящих сообщений от сервера
        Thread socketListenerThread = new Thread(() -> {
            final byte[] buf = new byte[1024 * 64];
            System.out.println("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Здесь поток блокируется на ожидании данных
                    int read = in.read(buf);
                    if (read > 0) {

                        // По сети передается поток байт, его нужно раскодировать с помощью протокола
                        Message msg = protocol.decode(Arrays.copyOf(buf, read));
                        onMessage(msg);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to process connection: " + e);
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        socketListenerThread.start();
    }

    /**
     * Реагируем на входящее сообщение
     */
    public void onMessage(Message msg) {
        System.err.println("Message received:  " + msg);
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ");
        System.out.println("Tokens: " + Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "/login":

                if (tokens.length != 3) {
                    System.out.println("Not enough arguments. Make sure that you mention login and password");
                } else {
                    String login = tokens[1];
                    String password = tokens[2];
                    Message loginMessage = new LoginMessage(this.user, login, password);
                    send(loginMessage);
                }
                break;
            case "/help":
                String printHelp = "/login <логин_пользователя> <пароль>\n" +
                        "/info [id]\n" +
                        "/chat_list\n" +
                        "/chat_create <user_id list>\n" +
                        "/chat_history <chat_id>\n" +
                        "/text <id> <message>\n";

                System.out.println(printHelp);
                break;
            case "/text":

                TextMessage sendMessage = new TextMessage(tokens[1], Type.MSG_TEXT, 0L, 1L);
                sendMessage.setType(Type.MSG_TEXT);
                sendMessage.setText(tokens[1]);
                send(sendMessage);
                break;
            // TODO: implement another types from wiki

            default:
                System.err.println("Invalid input: " + line);
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void send(Message msg) throws IOException, ProtocolException {
        System.out.println(msg);
        out.write(protocol.encode(msg));
        out.flush(); // принудительно проталкиваем буфер с данными
    }

    public static void main(String[] args) throws Exception {
        run(args);
    }

    private static void run(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();
        client.setHost(HOST);
        client.setPort(PORT);
        client.setProtocol(new StringProtocol());


        try {
            client.initSocket();
            FastScanner scanner = new FastScanner();

            // Цикл чтения с консоли
            System.out.println("$");
            while (true) {
                String input = scanner.readLine();
                if ("q".equals(input)) {
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    System.err.println("Failed to process user input " + e);
                }
            }
        } catch (Exception e) {
            System.err.println("Application failed. " + e);
        } finally {
            if (client != null) {
                // TODO
//                client.close();
            }
        }
    }
}