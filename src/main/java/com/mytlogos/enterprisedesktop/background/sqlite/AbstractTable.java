package com.mytlogos.enterprisedesktop.background.sqlite;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
abstract class AbstractTable {
    abstract void initialize();

    Connection getConnection() throws SQLException {
        final ConnectionManager manager = ConnectionManager.getManager();
        return manager.getConnection();
    }
}
