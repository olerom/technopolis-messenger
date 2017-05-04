package edu.technopolis.homework.messenger.store.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 04.05.17
 *
 * @author olerom
 */
@FunctionalInterface
public interface ResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}