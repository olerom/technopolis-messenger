package edu.technopolis.homework.messenger.store;

import edu.technopolis.homework.messenger.store.executor.Executor;

import java.sql.SQLException;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public interface Database {
    void initMessages() throws SQLException;

    void initUsers() throws SQLException;

    void dropMessages() throws SQLException;

    void dropUsers() throws SQLException;

    Executor getExecutor();
}
