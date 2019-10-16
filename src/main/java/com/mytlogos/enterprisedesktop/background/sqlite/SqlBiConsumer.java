package com.mytlogos.enterprisedesktop.background.sqlite;

import java.sql.SQLException;

/**
 *
 */
public interface SqlBiConsumer<T, V> {
    void accept(T t, V v) throws SQLException;
}
