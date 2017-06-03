package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.Type;
import edu.technopolis.homework.messenger.store.MessageStore;
import edu.technopolis.homework.messenger.store.UserStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class CommandFactory {

    private Map<Type, Command> typeCommandMap;

    public CommandFactory(UserStore userStore, MessageStore messageStore) {
        this.typeCommandMap = new HashMap<Type, Command>() {{
            put(Type.MSG_TEXT, new TextCommand(messageStore));
            put(Type.MSG_LOGIN, new LoginCommand(userStore));
            put(Type.MSG_REGISTER, new RegisterCommand(userStore));
            put(Type.MSG_CHAT_CREATE, new ChatCreateCommand(messageStore));
            put(Type.MSG_INFO, new UserInfoCommand(userStore));
            put(Type.MSG_CHAT_LIST, new ChatListCommand(messageStore));
            put(Type.MSG_CHAT_HIST, new ChatHistoryCommand(messageStore));
        }};
    }

    public Command get(Type type) {
        return typeCommandMap.get(type);
    }
}