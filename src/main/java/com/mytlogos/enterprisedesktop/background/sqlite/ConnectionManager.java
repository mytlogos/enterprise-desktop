package com.mytlogos.enterprisedesktop.background.sqlite;


import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class ConnectionManager {
    private static final ConnectionManager manager = new ConnectionManager();
    private final HikariDataSource dataSource;

    public ConnectionManager() {
        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:sqlite:enterprise.db");
    }

    public static ConnectionManager getManager() {
        return manager;
    }

    Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
