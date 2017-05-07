package edu.technopolis.homework.messenger.store;

import edu.technopolis.homework.messenger.store.executor.Executor;
import org.h2.jdbcx.JdbcDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
public class DatabaseImpl implements Database {

    private String user;
    private String password;
    private String fullPath;

    private Executor executor;

    public DatabaseImpl() {
        try (InputStream input = new FileInputStream("./src/main/resources/databaseConfiguration.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            this.user = properties.getProperty("dbuser");
            this.password = properties.getProperty("dbpassword");
            this.fullPath = properties.getProperty("databaseUrl") + properties.getProperty("database");

            this.executor = new Executor(getH2Connection());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection getH2Connection() {
        try {
            JdbcDataSource jdbcDataSource = new JdbcDataSource();
            jdbcDataSource.setURL(this.fullPath);
            jdbcDataSource.setUser(this.user);
            jdbcDataSource.setPassword(this.password);

            return DriverManager.getConnection(this.fullPath, this.user, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initMessages() throws SQLException {
        executor.execUpdate("CREATE TABLE if not exists MESSAGE (message_id bigint auto_increment, chat_id bigint, owner_id bigint, text varchar, primary key (message_id));");
    }

    @Override
    public void initUsers() throws SQLException {
        executor.execUpdate("CREATE TABLE if not exists USER (id bigint auto_increment, user_login varchar(30), user_password varchar(30), primary key (id), unique (user_login));");
    }

    @Override
    public void initUserChat() throws SQLException {
        executor.execUpdate("CREATE TABLE if not exists USER_CHAT (user_id bigint, chat_id bigint, primary key (user_id, chat_id));");
    }

    @Override
    public void initAdminChat() throws SQLException {
        executor.execUpdate("CREATE TABLE if not exists ADMIN_CHAT (user_id bigint, chat_id bigint, primary key (user_id, chat_id));");
    }

    @Override
    public void dropMessages() throws SQLException {
        executor.execUpdate("DROP TABLE if exists MESSAGE;");
    }

    @Override
    public void dropUsers() throws SQLException {
        executor.execUpdate("DROP TABLE if exists USER;");
    }

    @Override
    public void dropUserChat() throws SQLException {
        executor.execUpdate("DROP TABLE if exists USER_CHAT;");
    }

    @Override
    public void dropAdminChat() throws SQLException {
        executor.execUpdate("DROP TABLE if exists ADMIN_CHAT;");
    }

    @Override
    public Executor getExecutor() {
        return this.executor;
    }

}
