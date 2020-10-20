package org.example.model.dao.executor;


import org.example.model.dao.connection.DBConnection;

import java.sql.*;

public class Executor {

    private final Connection connection;

    public Executor() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public void execUpdate(String query, String... args) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setArgs(statement, args);
            statement.executeUpdate();
        }
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        T value;
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            ResultSet result = statement.getResultSet();
            value = handler.handle(result);
            result.close();
        }
        return value;
    }

    public <T> T execQuery(ResultHandler<T> handler, String query, String... args) throws SQLException {
        T value;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setArgs(statement, args);
            statement.execute();
            ResultSet result = statement.getResultSet();
            value = handler.handle(result);
            result.close();
        }
        return value;
    }

    private void setArgs(PreparedStatement statement, String[] args) throws SQLException {
        for (int i = 0; i < args.length; i++)
            statement.setString(i + 1, args[i]);
    }

}
