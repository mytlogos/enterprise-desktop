package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sqlite.SQLiteConfig;

import java.sql.SQLException;
import java.util.Properties;

/**
 *
 */
public class TestConnectionManager extends ConnectionManager {
    private final HikariDataSource dataSource;

    TestConnectionManager() {
        final HikariConfig configuration = new HikariConfig();
        configuration.setConnectionInitSql("PRAGMA foreign_keys = ON");
        configuration.setJdbcUrl("jdbc:sqlite:test.db");
        this.dataSource = new HikariDataSource(configuration);
    }

    ConnectionImpl getConnection() throws SQLException {
        return new ConnectionImpl(this.dataSource.getConnection());
    }
}
