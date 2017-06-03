package edu.technopolis.homework.messenger.net;


import edu.technopolis.homework.messenger.commands.Command;
import edu.technopolis.homework.messenger.commands.CommandFactory;
import edu.technopolis.homework.messenger.messages.Message;
import edu.technopolis.homework.messenger.store.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Date: 02.06.17
 *
 * @author olerom
 */
public class NonBlockingServer {
    public static void main(String[] args) {
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

        UserStore userStore = new UserStoreImpl(database.getExecutor());
        MessageStore messageStore = new MessageStoreImpl(database.getExecutor());
        BlockingQueue<Session> sessions = new LinkedBlockingQueue<>();

        HashMap<SocketChannel, ByteBuffer> map = new HashMap<>();

        try (ServerSocketChannel open = openAndBind()) {
            open.configureBlocking(false);
            while (true) {
                SocketChannel accept = open.accept(); //не блокируется
                if (accept != null) {
                    accept.configureBlocking(false);
                    map.put(accept, ByteBuffer.allocateDirect(4096));
                }
                map.keySet().removeIf(sc -> !sc.isOpen());

                map.forEach((sc, byteBuffer) -> {
                    try {
                        int read = sc.read(byteBuffer);
                        if (read == -1) {
                            close(sc);
                        } else if (read > 0) {
                            byteBuffer.flip();

                            Message msg = new StringProtocol().decode(read(byteBuffer));

                            byteBuffer.clear();

                            Session session = fixSessionChoice(sessions, msg.getSenderId());

                            if (session == null) {
                                session = new NonBlockingSession(sc);
                                sessions.add(session);
                            }

                            Command command = new CommandFactory(userStore, messageStore).get(msg.getType());
                            command.execute(session, msg, sessions);
                        }
                    } catch (IOException e) {
                        close(sc);
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static ServerSocketChannel openAndBind() throws IOException {
        ServerSocketChannel open = ServerSocketChannel.open();
        open.bind(new InetSocketAddress(19000));
        return open;
    }

    private static void close(SocketChannel sc) {
        try {
            sc.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static byte[] read(ByteBuffer data) {
        byte[] decoded = new byte[data.limit()];
        for (int i = 0; i < data.limit(); i++) {
            decoded[i] = data.get(i);
        }
        return decoded;
    }

    private static Session fixSessionChoice(BlockingQueue<Session> sessions, Long senderId) {
        if (senderId == null) {
            return null;
        }
        for (Session session : sessions) {
            if (session != null && session.getUser().getId() == senderId) {
                return session;
            }
        }
        return null;
    }
}