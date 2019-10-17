package com.mytlogos.enterprisedesktop.background.sqlite;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.sql.*;
import java.util.*;

/**
 *
 */
abstract class AbstractTable {
    private final Subject<Boolean> invalidated = PublishSubject.create();
    private final Observable<Boolean> invalidationObservable = this.invalidated.distinctUntilChanged();
    private final Subject<Class<? extends AbstractTable>> invalidatedTables = PublishSubject.create();
    private final Observable<Class<? extends AbstractTable>> invalidatedTableObservable = this.invalidatedTables.distinctUntilChanged();

    void initialize() {
        try (Connection connection = this.getConnection()) {
            connection.createStatement().execute(this.createTableSql());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Connection getConnection() throws SQLException {
        final ConnectionManager manager = ConnectionManager.getManager();
        return manager.getConnection();
    }

    abstract String createTableSql();

    void clearInvalidated() {
        this.invalidated.onNext(Boolean.FALSE);
    }

    Observable<Boolean> getInvalidated() {
        return this.invalidationObservable;
    }

    Observable<Class<? extends AbstractTable>> getInvalidatedTables() {
        return this.invalidatedTableObservable;
    }

    void invalidated(Class<? extends AbstractTable> invalidatedClass) {
        this.invalidatedTables.onNext(invalidatedClass);
    }

    Set<Integer> getLoadedInt() {
        try {
            return new HashSet<>(this.selectList(this.getLoadedQuery(), value -> value.getInt(1)));
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    Set<String> getLoadedString() {
        try {
            return new HashSet<>(this.selectList(this.getLoadedQuery(), value -> value.getString(1)));
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    <T> List<T> selectList(String sql, SqlFunction<ResultSet, T> function) throws SQLException {
        try (Connection connection = this.getConnection()) {
            try (ResultSet set = connection.createStatement().executeQuery(sql)) {
                List<T> values = new ArrayList<>();

                while (set.next()) {
                    T value = function.apply(set);
                    values.add(value);
                }
                return values;
            }
        }
    }

    String getLoadedQuery() {
        throw new UnsupportedOperationException();
    }

    <T> void execute(String sql, T value, SqlBiConsumer<PreparedStatement, T> consumer) {
        try (Connection connection = this.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                consumer.accept(statement, value);
                if (!statement.execute() && statement.getUpdateCount() > 0) {
                    this.setInvalidated();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setInvalidated() {
        this.invalidated.onNext(Boolean.TRUE);
    }

    <T> void execute(T value, PreparedQuery<T> preparedQuery) {
        try (Connection connection = this.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(preparedQuery.getQuery())) {
                preparedQuery.setValues(statement, value);
                if (!statement.execute() && statement.getUpdateCount() > 0) {
                    this.setInvalidated();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    <T> void execute(Collection<? extends T> value, PreparedQuery<T> preparedQuery) {
        try (Connection connection = this.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(preparedQuery.getQuery())) {
                for (T t : value) {
                    preparedQuery.setValues(statement, t);
                    statement.addBatch();
                }
                final int[] execute = statement.executeBatch();
                for (int insertInfo : execute) {
                    if (insertInfo > 0) {
                        this.setInvalidated();
                        break;
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    <T> void execute(String sql, Collection<T> collection, SqlBiConsumer<PreparedStatement, T> consumer) {
        try (Connection connection = this.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (T t : collection) {
                    consumer.accept(statement, t);
                    statement.addBatch();
                }
                final int[] execute = statement.executeBatch();
                for (int insertInfo : execute) {
                    if (insertInfo > 0) {
                        this.setInvalidated();
                        break;
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    <T> T selectSingle(String sql, SqlFunction<ResultSet, T> function) throws SQLException {
        try (Connection connection = this.getConnection()) {
            try (ResultSet set = connection.createStatement().executeQuery(sql)) {
                T value = null;

                if (set.next()) {
                    value = function.apply(set);
                }
                return value;
            }
        }
    }

    <T> List<T> selectList(String sql, SqlConsumer<PreparedStatement> querySetter, SqlFunction<ResultSet, T> function) throws SQLException {
        try (Connection connection = this.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                querySetter.apply(preparedStatement);

                try (ResultSet set = preparedStatement.executeQuery()) {
                    List<T> values = new ArrayList<>();

                    while (set.next()) {
                        T value = function.apply(set);
                        values.add(value);
                    }
                    return values;
                }
            }
        }
    }

    void executeQuery(String query) throws SQLException {
        try (Connection connection = this.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                if (!statement.execute(query) && statement.getUpdateCount() > 0) {
                    this.setInvalidated();
                }
            }
        }
    }

    protected interface PreparedQuery<T> {
        String getQuery();

        void setValues(PreparedStatement statement, T value) throws SQLException;
    }
}
