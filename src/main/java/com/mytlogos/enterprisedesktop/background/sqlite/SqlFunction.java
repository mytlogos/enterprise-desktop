package com.mytlogos.enterprisedesktop.background.sqlite;

import java.sql.SQLException;

/**
 *
 */
public interface SqlFunction<T, R> {
    R apply(T value) throws SQLException;
}
