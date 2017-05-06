package edu.technopolis.homework.messenger.store;

import edu.technopolis.homework.messenger.messages.User;
import edu.technopolis.homework.messenger.store.executor.Executor;

import java.sql.SQLException;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public class UserStoreImpl implements UserStore {

    private final Executor executor;

    public UserStoreImpl(Executor executor) {
        this.executor = executor;
    }

    @Override
    public User addUser(User user) throws SQLException {
        executor.execUpdate("INSERT INTO USER (user_login, user_password) values ('"
                + user.getLogin() + "', '" + user.getPassword() + "')");
        return user;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUser(String login, String pass) throws SQLException {
        return executor.execQuery("SELECT * FROM USER WHERE user_login='" + login + "' AND user_password='"
                + pass + '\'', result -> {
            result.next();
            return new User(result.getLong(1),
                    result.getString(2), result.getString(3));
        });
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }
}
