package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 *
 */
class ConnectionManagerTest {

    @Test
    void enableForeignKeys() {
        final ConnectionManager manager = new TestConnectionManager();
        try {
            final ConnectionImpl connection = manager.getConnection();
//            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            try (ResultSet result = connection.createStatement().executeQuery("pragma foreign_keys")) {
                Assertions.assertTrue(result.next());
                Assertions.assertTrue(result.getBoolean(1));
            }
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getConnection() {
        final ConnectionManager manager = new TestConnectionManager();
        try {
            final ConnectionImpl connection = manager.getConnection();
            Assertions.assertNotNull(connection);
            Assertions.assertTrue(connection.isValid(1));
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }
}