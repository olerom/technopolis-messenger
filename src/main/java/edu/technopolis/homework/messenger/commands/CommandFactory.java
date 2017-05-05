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
    private Map<Type, Command> typeCommandMap = new HashMap<>();

    public CommandFactory() {
        typeCommandMap.put(Type.MSG_LOGIN, new LoginCommand());
    }

    public Command get(Type type) {
        return typeCommandMap.get(type);
    }
}