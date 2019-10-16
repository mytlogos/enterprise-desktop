package com.mytlogos.enterprisedesktop.background.sqlite;


import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
class ConnectionManager {
    private static final ConnectionManager manager = new ConnectionManager();
    private final HikariDataSource dataSource;

    private ConnectionManager() {
        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:sqlite:enterprise.db");
    }

    static ConnectionManager getManager() {
        return manager;
    }

    Connection getConnection() throws SQLException {
        return new ConnectionImpl(this.dataSource.getConnection());
    }
}
