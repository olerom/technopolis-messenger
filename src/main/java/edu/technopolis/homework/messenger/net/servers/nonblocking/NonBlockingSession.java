package edu.technopolis.homework.messenger.net.servers.nonblocking;

import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.store.datasets.User;
import edu.technopolis.homework.messenger.net.Protocol;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;
import edu.technopolis.homework.messenger.net.StringProtocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

/**
 * Date: 03.06.17
 *
 * @author olerom
 */
public class NonBlockingSession implements Session {
    private static Logger LOGGER = LogManager.getLogger(NonBlockingSession.class.getName());

    private User user;

    private Protocol protocol;

    private SocketChannel socketChannel;

    public NonBlockingSession(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.protocol = new StringProtocol();
    }

    public void send(Message msg) throws ProtocolException, IOException {
        System.out.println();
        LOGGER.log(Level.INFO, "Sending info: {chat=" + msg.getChatId() + ", senderId=" + msg.getSenderId() + "}");

        byte[] encoded = protocol.encode(msg);
        ByteBuffer buf = ByteBuffer.allocate(5096);
        buf.put(encoded);
        buf.flip();
        socketChannel.write(buf);
        buf.compact();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
