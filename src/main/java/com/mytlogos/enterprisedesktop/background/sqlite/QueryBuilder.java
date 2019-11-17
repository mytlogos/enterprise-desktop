package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;
import com.mytlogos.enterprisedesktop.background.sqlite.internal.PreparedStatementImpl;
import com.mytlogos.enterprisedesktop.tools.Utils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

/**
 *
 */
class QueryBuilder<R> {
    private static final int MAX_PARAM_COUNT = 100;
    private String query;
    private SqlConsumer<PreparedStatement> singleQuerySetter;
    private Collection<Object> values;
    private SqlBiConsumer<PreparedStatement, Object> multiQuerySetter;
    private SqlFunction<ResultSet, R> converter;
    private Class<? extends AbstractTable>[] tables;
    private Type type;
    private List<?> queryInValues;

    QueryBuilder(String query) {
        this.query = query;
    }

    <T> QueryBuilder<R> setValue(Collection<T> value, SqlBiConsumer<PreparedStatement, T> multiQuerySetter) {
        //noinspection unchecked
        this.values = (Collection<Object>) value;
        //noinspection unchecked
        this.multiQuerySetter = (SqlBiConsumer<PreparedStatement, Object>) multiQuerySetter;
        return this;
    }

    QueryBuilder<R> setValues(SqlConsumer<PreparedStatement> singleQuerySetter) {
        this.singleQuerySetter = singleQuerySetter;
        return this;
    }

    QueryBuilder<R> setConverter(SqlFunction<ResultSet, R> converter) {
        this.converter = converter;
        return this;
    }

    QueryBuilder<R> setDependencies(Class<? extends AbstractTable>... tables) {
        this.tables = tables;
        return this;
    }

    QueryBuilder<R> setQueryIn(Collection<?> objects, Type type) {
        this.queryInValues = new ArrayList<>(objects);
        this.type = type;
        return this;
    }

    List<R> queryList() throws SQLException {
        if (this.converter == null) {
            throw new IllegalStateException("no converter available");
        }
        try (ConnectionImpl connection = ConnectionManager.getManager().getConnection()) {
            try (PreparedStatementImpl preparedStatement = connection.prepareStatement(this.query)) {
                this.prepareStatement(preparedStatement);

                try (ResultSet set = preparedStatement.executeQuery()) {
                    List<R> values = new ArrayList<>();

                    while (set.next()) {
                        R value = this.converter.apply(set);
                        values.add(value);
                    }
                    return values;
                }
            }
        }
    }

    private void prepareStatement(PreparedStatement preparedStatement) throws SQLException {
        if (this.values != null) {
            for (Object o : this.values) {
                this.multiQuerySetter.accept(preparedStatement, o);
                preparedStatement.addBatch();
            }
        } else if (this.singleQuerySetter != null) {
            this.singleQuerySetter.accept(preparedStatement);
        }
    }

    R query(Connection con) throws SQLException {
        if (this.converter == null) {
            throw new IllegalStateException("no converter available");
        }
        try (Connection connection = con) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(this.query)) {
                this.prepareStatement(preparedStatement);

                try (ResultSet set = preparedStatement.executeQuery()) {
                    if (set.next()) {
                        return this.converter.apply(set);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    Flowable<R> queryFlowable(Connection con) throws SQLException {
        return this.queryFlowable(con, BackpressureStrategy.LATEST);
    }

    Flowable<R> queryFlowable(Connection con, BackpressureStrategy strategy) throws SQLException {
        if (this.converter == null) {
            throw new IllegalStateException("no converter available");
        }
        try (Connection connection = con) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(this.query)) {
                this.prepareStatement(preparedStatement);

                try (ResultSet set = preparedStatement.executeQuery()) {
                    if (set.next()) {
                        return Flowable.create(emitter -> emitter.onNext(this.converter.apply(set)), strategy);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    Flowable<List<R>> queryFlowableList(Connection con) throws SQLException {
        return this.queryFlowableList(con, BackpressureStrategy.LATEST);
    }

    Flowable<List<R>> queryFlowableList(Connection con, BackpressureStrategy strategy) throws SQLException {
        if (this.converter == null) {
            throw new IllegalStateException("no converter available");
        }
        try (Connection connection = con) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(this.query)) {
                this.prepareStatement(preparedStatement);

                try (ResultSet set = preparedStatement.executeQuery()) {
                    List<R> values = new ArrayList<>();

                    while (set.next()) {
                        R value = this.converter.apply(set);
                        values.add(value);
                    }
                    return Flowable.create(emitter -> emitter.onNext(values), strategy);
                }
            }
        }
    }

    boolean execute(Connection con) {
        try (Connection connection = con) {
            try (PreparedStatement statement = connection.prepareStatement(this.query)) {
                this.prepareStatement(statement);

                if (this.values != null) {
                    connection.setAutoCommit(false);

                    final int[] execute;
                    try {
                        execute = statement.executeBatch();
                        connection.commit();
                    } catch (SQLException e) {
                        connection.rollback();
                        e.printStackTrace();
                        return false;
                    }
                    for (int insertInfo : execute) {
                        if (insertInfo > 0) {
                            return true;
                        }
                    }
                } else {
                    return !statement.execute() && statement.getUpdateCount() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    R executeIn(SqlFunction<PreparedStatement, R> biFunction, BiFunction<R, R, R> mergeFunction) throws SQLException {
        try (ConnectionImpl connection = ConnectionManager.getManager().getConnection()) {
            final String inPlaceholder = "$?";
            int index = this.query.indexOf(inPlaceholder);
            if (index < 0 && !this.queryInValues.isEmpty()) {
                throw new IllegalStateException("expected a '$?' placeholder in query");
            }
            List<?> inValues = this.queryInValues;

            final String before = this.query.substring(0, index);
            final String after = this.query.substring(index + inPlaceholder.length());

            int placeHolderCountBefore = Utils.countChar(before, '?');
            int placeHolderCountAfter = Utils.countChar(after, '?');

            int remainingCount = MAX_PARAM_COUNT - placeHolderCountBefore - placeHolderCountAfter;
            String queryPart = before + "IN (";

            AtomicReference<R> value = new AtomicReference<>();
            try {
                Utils.doPartitioned(remainingCount, inValues, objects -> {
                    String[] placeholderArray = new String[objects.size()];
                    Arrays.fill(placeholderArray, "?");
                    final String query = queryPart + String.join(",", placeholderArray) + ")" + after;

                    try (PreparedStatementImpl statement = connection.prepareStatement(query)) {
                        final int from = placeHolderCountBefore + 1;
                        final int to = placeHolderCountBefore + objects.size();

                        for (int pIndex = from, i = 0; i < objects.size(); pIndex++, i++) {
                            this.type.setValue(statement, pIndex, objects.get(i));
                        }
                        // add used indices AFTER setting the values for said indices,
                        // else values cant be set in the right places
                        statement.addUsedIndices(from, to);
                        final R result = biFunction.apply(statement);
                        final R mergedResult = mergeFunction.apply(value.get(), result);
                        value.set(mergedResult);
                    }
                    return false;
                });
                return value.get();
            } catch (SQLException e) {
                throw new SQLException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    enum Type {
        BOOLEAN {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setBoolean(index, ((Boolean) value));
            }
        },
        BYTE {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setByte(index, ((Byte) value));
            }
        },
        DOUBLE {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setDouble(index, ((Double) value));
            }
        },
        FLOAT {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setFloat(index, ((Float) value));
            }
        },
        INT {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setInt(index, ((Integer) value));
            }
        },
        LONG {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setLong(index, ((Long) value));
            }
        },
        SHORT {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setShort(index, ((Short) value));
            }
        },
        STRING {
            @Override
            void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
                statement.setString(index, ((String) value));
            }
        };

        abstract void setValue(PreparedStatement statement, int index, Object value) throws SQLException;
    }

    private static class CollectionType {
        private final Type type;
        private final Collection<?> collection;

        private CollectionType(Type type, Collection<?> collection) {
            this.type = type;
            this.collection = collection;
        }
    }
}
