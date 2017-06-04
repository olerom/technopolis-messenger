package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.TextMessage;
import edu.technopolis.homework.messenger.net.ProtocolException;
import edu.technopolis.homework.messenger.net.Session;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.*;

/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class ChatManager implements Runnable {

    private List<Session> sessions;
    private List<Long> receiverIds;
    private TextMessage textMessage;

    public ChatManager(BlockingQueue<Session> sessions,
                       List<Long> receiverIds,
                       TextMessage textMessage) {

        this.sessions = new ArrayList<>(sessions.size());
        this.sessions.addAll(sessions);

        this.receiverIds = receiverIds;
        this.textMessage = textMessage;
    }

    public void run() {

        for (Session participantSession : sessions) {
            for (long userId : receiverIds) {
                if (participantSession.getUser() != null && participantSession.getUser().getId() == userId) {
                    try {
                        participantSession.send(textMessage);
//                        sessions.remove(participantSession);
                    } catch (ProtocolException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addSession(Session session) {
        sessions.add(session);
    }
}
