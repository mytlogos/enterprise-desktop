package com.mytlogos.enterprisedesktop.background.sqlite;

import java.sql.SQLException;

/**
 *
 */
public interface SqlConsumer<T> {
    void apply(T value) throws SQLException;
}
