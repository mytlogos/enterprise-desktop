package com.mytlogos.enterprisedesktop.background.sqlite;


import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.MutableLiveData;
import com.mytlogos.enterprisedesktop.tools.Log;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class AbstractTable {
    private final MutableLiveData<Boolean> invalidated = new MutableLiveData<>();
    private final String table;

    AbstractTable(String table) {
        this.table = table;
        InvalidationManager.get().registerTable(this);
    }

    public void deleteAll() {
        this.executeDMLQueryIgnoreError("DELETE FROM `" + this.table + "`");
    }

    void executeDMLQueryIgnoreError(String query) {
        try {
            this.executeDMLQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.severe("%s: %s", e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    void executeDMLQuery(String query) throws SQLException {
        try (Connection connection = this.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                if (!statement.execute(query) && statement.getUpdateCount() > 0) {
                    this.setInvalidated();
                }
                Log.info(query);
            }
        }
    }

    ConnectionImpl getConnection() throws SQLException {
        final ConnectionManager manager = ConnectionManager.getManager();
        return manager.getConnection();
    }

    void setInvalidated() {
        this.invalidated.postValue(Boolean.TRUE);
    }

    void initialize() {
        try (Connection connection = this.getConnection()) {
            connection.createStatement().execute(this.createTableSql());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    abstract String createTableSql();

    <R> void executeDMLQuery(R value, QueryBuilder<R> queryBuilder) {
        this.executeDMLQuery(Collections.singleton(value), queryBuilder);
    }

    <R> void executeDMLQuery(Collection<? extends R> value, QueryBuilder<R> queryBuilder) {
        if (value.isEmpty()) {
            return;
        }
        try {
            if (queryBuilder.setValue(value).execute(this.getConnection())) {
                this.setInvalidated();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void clearInvalidated() {
        this.invalidated.postValue(Boolean.FALSE);
    }

    LiveData<Boolean> getInvalidated() {
        return this.invalidated;
    }

    Set<Integer> getLoadedInt() {
        try {
            return new HashSet<>(this.selectList(this.getLoadedQuery(), value -> value.getInt(1)));
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
                Log.info("%s: %d", sql, values.size());
                return values;
            }
        }
    }

    String getLoadedQuery() {
        throw new UnsupportedOperationException();
    }

    Set<String> getLoadedString() {
        try {
            return new HashSet<>(this.selectList(this.getLoadedQuery(), value -> value.getString(1)));
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    <T> void update(Collection<T> values, String table, Map<String, Function<T, ?>> attrExtractors, Map<String, Function<T, ?>> keyExtractors) {
        // TODO 02.3.2020: use something else maybe, as this may be used for SQLInjection?
        if (attrExtractors.isEmpty()) {
            System.out.println("trying to update without any attrExtractor");
            return;
        }
        final List<String> attrNames = new ArrayList<>(attrExtractors.keySet());
        final List<String> keyNames = new ArrayList<>(keyExtractors.keySet());
        String query = attrNames
                .stream()
                .map(attr -> "`" + attr + "` = ?")
                .collect(Collectors.joining(",", "UPDATE `" + table + "` SET ", ""));

        query = keyNames.stream()
                .map(attr -> "`" + attr + "` = ?")
                .collect(Collectors.joining(",", query + " WHERE ", ";"));

        this.executeDMLQuery(
                values,
                new QueryBuilder<T>("Update " + this.getClass().getSimpleName(), query)
                        .setValueSetter((preparedStatement, t) -> {
                            int placeholder = 1;
                            for (int i = 0, attrNamesSize = attrNames.size(); i < attrNamesSize; i++, placeholder++) {
                                String attrName = attrNames.get(i);
                                final Function<T, ?> function = attrExtractors.get(attrName);

                                setValue(preparedStatement, t, placeholder, function);
                            }
                            for (int i = 0; i < keyNames.size(); i++, placeholder++) {
                                String keyName = keyNames.get(i);
                                final Function<T, ?> function = keyExtractors.get(keyName);

                                setValue(preparedStatement, t, placeholder, function);
                            }
                        })
        );
    }

    private <T> void setValue(PreparedStatement preparedStatement, T t, int placeholder, Function<T, ?> function) throws SQLException {
        if (function instanceof ByteProducer) {
            //noinspection unchecked
            preparedStatement.setByte(placeholder, ((ByteProducer<T>) function).apply(t));
        } else if (function instanceof ShortProducer) {
            //noinspection unchecked
            preparedStatement.setShort(placeholder, ((ShortProducer<T>) function).apply(t));
        } else if (function instanceof IntProducer) {
            //noinspection unchecked
            preparedStatement.setInt(placeholder, ((IntProducer<T>) function).apply(t));
        } else if (function instanceof LongProducer) {
            //noinspection unchecked
            preparedStatement.setLong(placeholder, ((LongProducer<T>) function).apply(t));
        } else if (function instanceof FloatProducer) {
            //noinspection unchecked
            preparedStatement.setFloat(placeholder, ((FloatProducer<T>) function).apply(t));
        } else if (function instanceof DoubleProducer) {
            //noinspection unchecked
            preparedStatement.setDouble(placeholder, ((DoubleProducer<T>) function).apply(t));
        } else if (function instanceof BooleanProducer) {
            //noinspection unchecked
            preparedStatement.setBoolean(placeholder, ((BooleanProducer<T>) function).apply(t));
        } else if (function instanceof StringProducer) {
            //noinspection unchecked
            preparedStatement.setString(placeholder, ((StringProducer<T>) function).apply(t));
        }
    }

    interface IntProducer<R> extends Function<R, Integer> {
    }

    interface ShortProducer<R> extends Function<R, Short> {
    }

    interface ByteProducer<R> extends Function<R, Byte> {
    }

    interface StringProducer<R> extends Function<R, String> {
    }

    interface LongProducer<R> extends Function<R, Long> {
    }

    interface DoubleProducer<R> extends Function<R, Double> {
    }

    interface FloatProducer<R> extends Function<R, Float> {
    }

    interface BooleanProducer<R> extends Function<R, Boolean> {
    }
}
