package com.example.orderaccountingprogram.DB_API;

import java.sql.*;

public class Database {
    private String url;
    private String user;
    private String password;

    Connection dbConnection;

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        dbConnection = null;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        dbConnection = connection;
        return dbConnection;
    }
}
