package edu.technopolis.homework.messenger.teacher.client;


import edu.technopolis.homework.messenger.messages.*;
import edu.technopolis.homework.messenger.net.Protocol;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.StringProtocol;
import edu.technopolis.homework.messenger.store.datasets.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    private void setHost(String host) {
        this.host = host;
    }

    private void initSocket() throws IOException {
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
    private void onMessage(Message msg) {

        switch (msg.getType()) {
            case MSG_LOGIN:
                LoginMessage loginMessage = (LoginMessage) msg;
                this.user = new User(loginMessage.getSenderId(), loginMessage.getLogin(), loginMessage.getPassword());

                System.out.println("You are logged in, " + user.getLogin()
                        + ". Your id is " + user.getId());
                break;
            case MSG_TEXT:
                TextMessage textMessage = (TextMessage) msg;
                System.out.println("You received a new message from chat "
                        + textMessage.getSenderId()
                        + ": " + textMessage.getText());
                break;

            case MSG_INFO_RESULT:
            case MSG_STATUS:
                StatusMessage statusMessage = (StatusMessage) msg;
                System.out.println(statusMessage.getInfo());
                break;

            case MSG_CHAT_LIST_RESULT:
                ChatListResultMessage message = (ChatListResultMessage) msg;
                StringBuilder chats = new StringBuilder();
                chats.append("Your chats: ");

                for (Long chatId : message.getChats()) {
                    chats.append(chatId).append(" ");
                }

                System.out.println(chats.toString());

                break;

            case MSG_CHAT_HIST_RESULT:
                ChatHistoryResultMessage chatHistoryResultMessage = (ChatHistoryResultMessage) msg;
                StringBuilder history = new StringBuilder();
                history.append("Your history: \n");

                for (String historyMessage : chatHistoryResultMessage.getMessages()) {
                    history.append(historyMessage).append("\n");
                }

                System.out.println(history.toString());

        }
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    private void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ");
        System.err.println("Tokens: " + Arrays.toString(tokens));
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
                String printHelp =
                        "/help - показать список команд и общий хэлп по месседжеру\n" +
                                "/login <логин_пользователя> <пароль> - залогиниться\n" +
                                "/register <логин_пользователя> <пароль> - зарегистрироваться\n" +
                                "/info [id] - получить всю информацию о пользователе, без аргументов - о себе\n" +
                                "/chat_list - получить список чатов пользователя\n" +
                                "/chat_create <user_id list> - создать новый чат, список пользователей приглашенных в чат\n" +
                                "/chat_history <chat_id> - список сообщений из указанного чата\n" +
                                "/text <id> <message> - отправить сообщение в заданный чат\n" +
                                "/q - выйти\n";
                System.out.println(printHelp);
                break;
            case "/text":

                if (tokens.length < 3) {
                    System.out.println("Not enough arguments. Make sure that you mention chat and text to this chat");
                } else {
                    try {
                        long chatId = Long.valueOf(tokens[1]);

                        StringBuilder text = new StringBuilder();
                        for (int i = 2; i < tokens.length; i++) {
                            text.append(tokens[i]);
                            text.append(" ");
                        }

                        if (text.toString().length() > 200) {
                            System.out.println("Your text is too big. Notice that 200 symbols is max number of them.");
                        }

                        TextMessage textMessage = new TextMessage(user, chatId, text.toString());
                        send(textMessage);
                    } catch (NumberFormatException e) {
                        System.out.println();
                    }
                }
                break;

            case "/chat_create":
                if (tokens.length < 2) {
                    System.out.println("Not enough arguments");
                } else {
                    try {
                        List<Long> participants = new ArrayList<>();

                        for (String participant : tokens[1].split(",")) {
                            participants.add(Long.valueOf(participant));
                        }

                        ChatCreateMessage chatCreateMessage = new ChatCreateMessage(user, participants);
                        send(chatCreateMessage);

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                        System.out.println("Can't create message. Whoops! :(");
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Can't parse participants ids");
                    }
                }

                break;

            case "/register":
                if (tokens.length != 3) {
                    System.out.println("Not enough arguments. Make sure that you mention login and password");
                } else {
                    String login = tokens[1];
                    String password = tokens[2];
                    Message loginMessage = new LoginMessage(this.user, login, password);
                    loginMessage.setType(Type.MSG_REGISTER);
                    send(loginMessage);
                }
                break;

            case "/info":

                if (tokens.length > 2) {
                    System.out.println("Too much arguments");
                } else {
                    try {
                        Long userId = tokens.length == 2 ? Long.valueOf(tokens[1]) : null;

                        UserInfoMessage userInfoMessage = new UserInfoMessage(user, userId);
                        send(userInfoMessage);

                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Can't parse friend id");
                    }
                }

                break;

            case "/chat_list":

                if (tokens.length > 2) {
                    System.out.println("Too much arguments. They are redundant.");
                } else {
                    try {

                        ChatListMessage chatListMessage = new ChatListMessage(this.user);
                        send(chatListMessage);

                    } catch (InstantiationException e) {
                        System.out.println("Something went wrong.");
                        e.printStackTrace();
                    }
                }

                break;

            case "/chat_history":

                if (tokens.length != 2) {
                    System.out.println("This command requires 2 arguments");
                } else {
                    try {

                        Long chatId = Long.valueOf(tokens[1]);

                        ChatHistoryMessage chatHistoryMessage = new ChatHistoryMessage(this.user, chatId);
                        send(chatHistoryMessage);

                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Can't parse chat id");
                    } catch (InstantiationException e) {
                        System.out.println("Something went wrong.");
                        e.printStackTrace();
                    }
                }

                break;

            default:
                System.err.println("Invalid input: " + line);
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    private void send(Message msg) throws IOException, ProtocolException {
        System.err.println(msg);
        out.write(protocol.encode(msg));
        out.flush(); // принудительно проталкиваем буфер с данными
    }

    public static void main(String[] args) throws Exception {
        new MessengerClient().run(args);
    }

    private void run(String[] args) throws Exception {

        setHost(HOST);
        setPort(PORT);
        setProtocol(new StringProtocol());

        try {
            initSocket();
            FastScanner scanner = new FastScanner();

            // Цикл чтения с консоли
            System.out.println("$");
            while (true) {
                String input = scanner.readLine();
                if ("/q".equals(input)) {
                    scanner.close();
                    System.exit(0);
                }
                try {
                    processInput(input);
                } catch (ProtocolException | IOException e) {
                    System.err.println("Failed to process user input " + e);
                }
            }
        } catch (Exception e) {
            System.err.println("Application failed. " + e);
        } finally {
            if (this != null) {
                System.out.println("Something went wrong.");
                System.exit(1);
            }
        }
    }
}