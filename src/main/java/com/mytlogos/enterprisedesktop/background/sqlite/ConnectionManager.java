package com.mytlogos.enterprisedesktop.background.sqlite;


import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

/**
 *
 */
class ConnectionManager {
    private final HikariDataSource dataSource;

    ConnectionManager() {
        final HikariConfig configuration = getHikariConfig();
        this.dataSource = new HikariDataSource(configuration);
    }

    HikariConfig getHikariConfig() {
        final HikariConfig configuration = new HikariConfig();
        configuration.setConnectionInitSql("PRAGMA foreign_keys = ON");
        configuration.setJdbcUrl("jdbc:sqlite:enterprise.db");
        return configuration;
    }

    ConnectionImpl getConnection() throws SQLException {
        return new ConnectionImpl(this.dataSource.getConnection());
    }
}
