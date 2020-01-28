package com.mytlogos.enterprisedesktop.background.sqlite;

import java.sql.SQLException;

/**
 *
 */
public interface SqlSupplier<T> {
    T supply() throws SQLException;
}
