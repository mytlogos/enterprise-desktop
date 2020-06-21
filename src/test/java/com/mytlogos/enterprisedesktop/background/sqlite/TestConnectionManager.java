package com.mytlogos.enterprisedesktop.background.sqlite;

import com.zaxxer.hikari.HikariConfig;

/**
 *
 */
public class TestConnectionManager extends ConnectionManager {

    @Override
    HikariConfig getHikariConfig() {
        final HikariConfig config = super.getHikariConfig();
        config.setJdbcUrl("jdbc:sqlite:test.db");
        return config;
    }
}
