package com.mytlogos.enterprisedesktop.background.sqlite;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SqlUtils {

    private SqlUtils() {
        throw new IllegalAccessError("Do not instantiate SqlUtils!");
    }

    private static final SqlBiFunction getResultsFunction = (SqlBiFunction<PreparedStatement, SqlFunction<ResultSet, Object>, List<Object>>) (preparedStatement, resultSetSqlFunction) -> {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Object> list = new ArrayList<>();
            while (resultSet.next()) {
                Object result = resultSetSqlFunction.apply(resultSet);
                list.add(result);
            }
            return list;
        }
    };

    public static <R> SqlFunction<PreparedStatement, List<R>> getResults(SqlFunction<ResultSet, R> function) {
        return (preparedStatement) -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<R> list = new ArrayList<>();
                while (resultSet.next()) {
                    R result = function.apply(resultSet);
                    list.add(result);
                }
                return list;
            }
        };
    }

    public static <R> List<R> mergeLists(List<R> list1, List<R> list2) {
        if (list1 == null) {
            return list2;
        }
        list1.addAll(list2);
        return list1;
    }

    public static SqlFunction<PreparedStatement, Void> ignoreResult() {
        return (preparedStatement) -> {
            preparedStatement.executeUpdate();
            return null;
        };
    }

    public static SqlFunction<PreparedStatement, Boolean> update(SqlConsumer<PreparedStatement> statementSqlConsumer) {
        return (preparedStatement) -> {
            statementSqlConsumer.accept(preparedStatement);
            return preparedStatement.executeUpdate() > 0;
        };
    }
}
