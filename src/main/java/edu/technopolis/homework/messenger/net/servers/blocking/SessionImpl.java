package edu.technopolis.homework.messenger.net.servers.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import edu.technopolis.homework.messenger.messages.User;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.net.Protocol;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.net.StringProtocol;

/**
 * Сессия связывает бизнес-логику и сетевую часть.
 * Бизнес логика представлена объектом юзера - владельца сессии.
 * Сетевая часть привязывает нас к определнному соединению по сети (от клиента)
 */
public class SessionImpl implements Session {

    /**
     * Пользователь сессии, пока не прошел логин, user == null
     * После логина устанавливается реальный пользователь
     */
    private User user;

    // сокет на клиента
    private Socket socket;

    private Protocol protocol;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    public SessionImpl(User user, Socket socket) {
        this.user = user;
        this.socket = socket;

        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Could't get stream");
            e.printStackTrace();
            System.exit(1);
        }
        this.protocol = new StringProtocol();
    }

    public SessionImpl(Socket socket) {
        this.socket = socket;
        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Could't get stream");
            e.printStackTrace();
            System.exit(1);
        }
        this.protocol = new StringProtocol();
    }

    public void send(Message msg) throws ProtocolException, IOException {
        System.out.println("Sending info: {chat=" + msg.getChatId()
                + ", senderId=" + msg.getSenderId() + "}");
        out.write(protocol.encode(msg));
        out.flush();
    }

    public void onMessage(Message msg) {
        // TODO: Пришло некое сообщение от клиента, его нужно обработать
    }

    public void close() {
        // TODO: закрыть in/out каналы и сокет. Освободить другие ресурсы, если необходимо
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}