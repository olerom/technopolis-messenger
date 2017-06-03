package edu.technopolis.homework.messenger.net.servers.nonblocking;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.messages.User;
import edu.technopolis.homework.messenger.net.Protocol;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.net.StringProtocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Date: 03.06.17
 *
 * @author olerom
 */
public class NonBlockingSession implements Session {
    private User user;

    private Protocol protocol;

    private SocketChannel socketChannel;

    public NonBlockingSession(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.protocol = new StringProtocol();
    }

    public void send(Message msg) throws ProtocolException, IOException {
        System.out.println("Sending info: {chat=" + msg.getChatId()
                + ", senderId=" + msg.getSenderId() + "}");

        byte[] encoded = protocol.encode(msg);
        ByteBuffer buf = ByteBuffer.allocate(5096);
        buf.put(encoded);
        buf.flip();
        socketChannel.write(buf);
        buf.compact();
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
