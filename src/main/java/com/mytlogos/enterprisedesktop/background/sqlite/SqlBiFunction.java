package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.sqlite.internal.builder.SqlExpression;

import java.sql.SQLException;

/**
 *
 */
public interface SqlBiFunction<T, T1, V> {
    V apply(T t, T1 t1) throws SQLException;
}
