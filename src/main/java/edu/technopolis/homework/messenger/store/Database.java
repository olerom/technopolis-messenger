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

    void initUserChat() throws SQLException;

    void initAdminChat() throws SQLException;

    void dropMessages() throws SQLException;

    void dropUsers() throws SQLException;

    void dropUserChat() throws SQLException;

    void dropAdminChat() throws SQLException;

    Executor getExecutor();

    void initTables() throws SQLException;

    void dropTables() throws SQLException;
}
