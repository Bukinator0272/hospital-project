package org.example.model.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DEBUG_PARAM = ";hsqldb.lock_file = false";
    private static final String URL = "jdbc:hsqldb:file:db/testDB";
    private static final String USER = "SA";
    private static final String PASSWORD = "";
    private Connection connection;
    private static DBConnection instance;

    public DBConnection() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (instance == null)
            instance = new DBConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
