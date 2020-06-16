package com.mytlogos.enterprisedesktop.background.sqlite;


import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;
import com.zaxxer.hikari.HikariDataSource;

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

    void enableForeignKeys() {
        try {
            this.getConnection().createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    ConnectionImpl getConnection() throws SQLException {
        return new ConnectionImpl(this.dataSource.getConnection());
    }
}
