package edu.technopolis.homework.messenger.commands;

import edu.technopolis.homework.messenger.messages.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 05.05.17
 *
 * @author olerom
 */
public class CommandFactory {
    private Map<Type, Command> typeCommandMap = new HashMap<Type, Command>() {{
        put(Type.MSG_TEXT, new TextCommand());
        put(Type.MSG_LOGIN, new LoginCommand());
        put(Type.MSG_REGISTER, new RegisterCommand());
        put(Type.MSG_CHAT_CREATE, new ChatCreateCommand());
        put(Type.MSG_INFO, new UserInfoCommand());
    }};

    public Command get(Type type) {
        return typeCommandMap.get(type);
    }
}