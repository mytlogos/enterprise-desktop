package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.sqlite.internal.ConnectionImpl;
import com.mytlogos.enterprisedesktop.background.sqlite.internal.PreparedStatementImpl;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.tools.Log;
import com.mytlogos.enterprisedesktop.tools.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

/**
 *
 */
class QueryBuilder<R> {
    private static final int MAX_PARAM_COUNT = 100;
    private static final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    private final String name;
    private final String query;
    private final Set<Class<? extends AbstractTable>> tables = Collections.newSetFromMap(new WeakHashMap<>());
    private final boolean isRead;
    private final ConnectionManager manager;
    private final List<OrderBy> columnOrder = new ArrayList<>();
    private SqlConsumer<PreparedStatement> singleQuerySetter;
    private Collection<Object> values;
    private SqlBiConsumer<PreparedStatement, Object> multiQuerySetter;
    private SqlFunction<ResultSet, R> converter;
    private Type type;
    private List<?> queryInValues;
    private boolean doEmpty = false;

    QueryBuilder(String name, String query, ConnectionManager manager) {
        this.name = name;
        this.query = query;
        this.isRead = query.substring(0, query.indexOf(" ")).trim().equalsIgnoreCase("select");
        this.manager = manager;
    }

    private QueryBuilder(QueryBuilder<R> builder) {
        this.name = builder.name;
        this.query = builder.query;
        this.tables.addAll(builder.tables);
        this.isRead = builder.isRead;
        this.singleQuerySetter = builder.singleQuerySetter;
        this.values = builder.values == null ? null : new ArrayList<>(builder.values);
        this.multiQuerySetter = builder.multiQuerySetter;
        this.converter = builder.converter;
        this.type = builder.type;
        this.queryInValues = builder.queryInValues == null ? null : new ArrayList<>(builder.queryInValues);
        this.doEmpty = builder.doEmpty;
        this.manager = builder.manager;
        this.columnOrder.addAll(builder.columnOrder);
    }

    <T> QueryBuilder<R> setValue(Collection<T> value, SqlBiConsumer<PreparedStatement, T> multiQuerySetter) {
        //noinspection unchecked
        this.values = (Collection<Object>) value;
        //noinspection unchecked
        this.multiQuerySetter = (SqlBiConsumer<PreparedStatement, Object>) multiQuerySetter;
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> doEmpty() {
        this.doEmpty = true;
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> setValue(Collection<? extends R> value) {
        //noinspection unchecked
        this.values = (Collection<Object>) value;
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> setValues(SqlConsumer<PreparedStatement> singleQuerySetter) {
        this.singleQuerySetter = singleQuerySetter;
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> setValueSetter(SqlBiConsumer<PreparedStatement, R> querySetter) {
        //noinspection unchecked
        this.multiQuerySetter = (SqlBiConsumer<PreparedStatement, Object>) querySetter;
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> setConverter(SqlFunction<ResultSet, R> converter) {
        this.converter = converter;
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> setDependencies(Class<?>... tables) {
        //noinspection unchecked
        Collections.addAll(this.tables, ((Class<? extends AbstractTable>[]) tables));
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> setQueryIn(Collection<?> objects, Type type) {
        this.queryInValues = new ArrayList<>(objects);
        this.type = type;
        return new QueryBuilder<>(this);
    }

    QueryBuilder<R> orderBy(OrderBy... orderBy) {
        this.columnOrder.clear();
        this.columnOrder.addAll(Arrays.asList(orderBy));
        return new QueryBuilder<>(this);
    }

    List<R> queryListIgnoreError() {
        this.expectRead();
        try {
            return this.queryList();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void expectRead() {
        if (!this.isRead) {
            throw new IllegalStateException("Expected 'Read', got 'Write' instead");
        }
    }

    private void checkConverter() {
        if (this.converter == null) {
            throw new IllegalStateException("no converter available");
        }
    }

    private String getQuery() {
        String query = this.query;

        if (this.columnOrder.isEmpty()) {
            return query;
        }

        List<String> orders = new ArrayList<>();

        for (OrderBy orderBy : columnOrder) {
            orders.add(orderBy.column + " " + (orderBy.order == SqlOrder.ASC ? SqlOrder.ASC : SqlOrder.DESC));
        }
        return String.format("%s ORDER BY %s", query, String.join(", ", orders));
    }

    List<R> queryList() throws SQLException {
        this.expectRead();
        this.checkConverter();
        try (ConnectionImpl connection = this.manager.getConnection()) {
            try (PreparedStatementImpl preparedStatement = connection.prepareStatement(this.getQuery(), this.name)) {
                return selectResultList(preparedStatement);
            }
        }
    }

    private List<R> selectResultList(PreparedStatement preparedStatement) throws SQLException {
        this.prepareStatement(preparedStatement);

        List<R> values = new ArrayList<>();
        this.lock();
        var start = Instant.now();
        try (ResultSet set = preparedStatement.executeQuery()) {
            while (set.next()) {
                R value = this.converter.apply(set);
                values.add(value);
            }
        } finally {
            var end = Instant.now();
            Log.info(String.format("%s, Total Duration: %s", this.name, Duration.between(start, end)));
            this.unlock();
        }
        return values;
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

    private void lock() {
        if (this.isRead) {
            READ_WRITE_LOCK.readLock().lock();
        } else {
            READ_WRITE_LOCK.writeLock().lock();
        }
    }

    private void unlock() {
        if (this.isRead) {
            READ_WRITE_LOCK.readLock().unlock();
        } else {
            READ_WRITE_LOCK.writeLock().unlock();
        }
    }

    R query() {
        this.expectRead();
        try {
            return this.query(manager.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    R query(ConnectionImpl con) throws SQLException {
        this.expectRead();
        this.checkConverter();
        try (ConnectionImpl connection = con) {
            return selectResult(connection);
        }
    }

    private R selectResult(ConnectionImpl connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(this.getQuery(), this.name)) {
            this.prepareStatement(preparedStatement);

            this.lock();
            try (ResultSet set = preparedStatement.executeQuery()) {
                if (set.next()) {
                    return this.converter.apply(set);
                } else {
                    return null;
                }
            } finally {
                this.unlock();
            }
        }
    }

    LiveData<R> queryLiveDataPassError() {
        return this.queryLiveData();
    }

    LiveData<R> queryLiveData() {
        this.expectRead();
        this.checkConverter();
        return this.createLiveData(() -> {
            try (ConnectionImpl connection = manager.getConnection()) {
                return selectResult(connection);
            }
        });
    }

    private <T> LiveData<T> createLiveData(Callable<T> supplier) {
        this.expectRead();
        return LiveData.create(supplier, this.tables);
    }

    LiveData<List<R>> queryLiveDataList() {
        this.expectRead();
        this.checkConverter();
        return this.createLiveData(() -> {
            try (ConnectionImpl connection = manager.getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(this.getQuery(), this.name)) {
                    return selectResultList(preparedStatement);
                }
            }
        });
    }

    boolean execute() {
        try {
            return this.execute(this.manager.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean execute(ConnectionImpl con) {
        try {
            return this.executeThrow(con);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean executeThrow(ConnectionImpl con) throws SQLException {
        this.expectWrite();
        try (ConnectionImpl connection = con) {
            try (PreparedStatement statement = connection.prepareStatement(this.getQuery(), this.name)) {
                this.prepareStatement(statement);

                if (this.values != null) {
                    final boolean autoCommit = connection.getAutoCommit();
                    connection.setAutoCommit(false);

                    final int[] execute;
                    this.lock();
                    try {
                        execute = statement.executeBatch();
                        connection.commit();
                    } catch (SQLException e) {
                        connection.rollback();
                        throw e;
                    } finally {
                        // always unlock first before doing anything that can potentially fail
                        this.unlock();
                        // preserve previous auto commit value
                        connection.setAutoCommit(autoCommit);
                    }
                    for (int insertInfo : execute) {
                        if (insertInfo > 0) {
                            return true;
                        }
                    }
                } else {
                    try {
                        this.lock();
                        return !statement.execute() && statement.getUpdateCount() > 0;
                    } finally {
                        this.unlock();
                    }
                }
            }
        }
        return false;
    }

    private void expectWrite() {
        if (this.isRead) {
            throw new IllegalStateException("Expected 'Write', got 'Read' instead");
        }
    }

    LiveData<R> executeInLiveData(SqlFunction<PreparedStatement, R> biFunction, BiFunction<R, R, R> mergeFunction) {
        this.expectRead();
        return this.createLiveData(() -> this.executeIn(biFunction, mergeFunction));
    }

    <T> T executeIn(SqlFunction<PreparedStatement, T> biFunction, BiFunction<T, T, T> mergeFunction) throws SQLException {
        final String inPlaceholder = "$?";
        String query = this.getQuery();

        int index = query.indexOf(inPlaceholder);
        if (index < 0 && !this.queryInValues.isEmpty()) {
            throw new IllegalStateException("expected a '$?' placeholder in query");
        }
        List<?> inValues = this.queryInValues;

        final String before = query.substring(0, index);
        final String after = query.substring(index + inPlaceholder.length());

        int placeHolderCountBefore = Utils.countChar(before, '?');
        int placeHolderCountAfter = Utils.countChar(after, '?');

        int remainingCount = MAX_PARAM_COUNT - placeHolderCountBefore - placeHolderCountAfter;
        String queryPart = before + "IN (";

        AtomicReference<T> value = new AtomicReference<>();

        try (ConnectionImpl connection = manager.getConnection()) {
            try {
                Utils.doPartitioned(remainingCount, inValues, objects -> {
                    String[] placeholderArray = new String[objects.size()];
                    Arrays.fill(placeholderArray, "?");
                    final String concatQuery = queryPart + String.join(",", placeholderArray) + ")" + after;

                    try (PreparedStatementImpl statement = connection.prepareStatement(concatQuery, this.name)) {
                        final int from = placeHolderCountBefore + 1;
                        final int to = placeHolderCountBefore + objects.size();

                        for (int pIndex = from, i = 0; i < objects.size(); pIndex++, i++) {
                            this.type.setValue(statement, pIndex, objects.get(i));
                        }
                        // add used indices AFTER setting the values for said indices,
                        // else values cant be set in the right places
                        if (from <= to) {
                            statement.addUsedIndices(from, to);
                        }
                        this.prepareStatement(statement);
                        this.lock();
                        final T result;
                        try {
                            result = biFunction.apply(statement);
                        } finally {
                            this.unlock();
                        }
                        final T mergedResult = mergeFunction.apply(value.get(), result);
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

    boolean updateInIgnoreError() {
        try {
            return this.updateIn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean updateIn() throws SQLException {
        this.expectWrite();
        final Boolean result = this.executeIn(SqlUtils.update(this.singleQuerySetter), (o, o1) -> {
            if (o == null) {
                if (o1 == null) {
                    return false;
                } else {
                    return o1;
                }
            } else {
                return o || o1;
            }
        });
        return result == null ? false : result;
    }

    LiveData<List<R>> selectInLiveDataList() {
        this.expectRead();
        return this.createLiveData(this::selectInListIgnoreError);
    }

    List<R> selectInListIgnoreError() {
        this.expectRead();
        try {
            return this.selectInList(this.converter);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    List<R> selectInList(SqlFunction<ResultSet, R> biFunction) throws SQLException {
        this.expectRead();
        final String inPlaceholder = "$?";

        String query = this.getQuery();

        int index = query.indexOf(inPlaceholder);
        if (index < 0 && !this.queryInValues.isEmpty()) {
            throw new IllegalStateException("expected a '$?' placeholder in query");
        }
        List<?> inValues = this.queryInValues;

        final String before = query.substring(0, index);
        final String after = query.substring(index + inPlaceholder.length());

        int placeHolderCountBefore = Utils.countChar(before, '?');
        int placeHolderCountAfter = Utils.countChar(after, '?');

        int remainingCount = MAX_PARAM_COUNT - placeHolderCountBefore - placeHolderCountAfter;
        String queryPart = before + "IN (";

        List<R> resultValues = new ArrayList<>();

        try (ConnectionImpl connection = manager.getConnection()) {
            try {
                Utils.doPartitioned(remainingCount, this.doEmpty, inValues, objects -> {
                    String[] placeholderArray = new String[objects.size()];
                    Arrays.fill(placeholderArray, "?");
                    final String concatQuery = queryPart + String.join(",", placeholderArray) + ")" + after;

                    try (PreparedStatementImpl statement = connection.prepareStatement(concatQuery, this.name)) {
                        final int from = placeHolderCountBefore + 1;
                        final int to = placeHolderCountBefore + objects.size();

                        for (int pIndex = from, i = 0; i < objects.size(); pIndex++, i++) {
                            this.type.setValue(statement, pIndex, objects.get(i));
                        }
                        // add used indices AFTER setting the values for said indices,
                        // else values cant be set in the right places
                        if (from <= to) {
                            statement.addUsedIndices(from, to);
                        }
                        this.prepareStatement(statement);

                        this.lock();
                        try {
                            if (statement.execute()) {
                                try (ResultSet resultSet = statement.getResultSet()) {
                                    while (resultSet.next()) {
                                        final R result = biFunction.apply(resultSet);
                                        resultValues.add(result);
                                    }
                                }
                            }
                        } finally {
                            this.unlock();
                        }
                    }
                    return false;
                });
                return resultValues;
            } catch (SQLException e) {
                throw new SQLException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    List<R> selectInListSimple(SqlFunction<ResultSet, R> biFunction) throws SQLException {
        this.expectRead();
        return executeIn(statement -> {
            List<R> values = new LinkedList<>();
            this.lock();

            try {
                if (statement.execute()) {
                    try (ResultSet resultSet = statement.getResultSet()) {

                        while (resultSet.next()) {
                            R single = biFunction.apply(resultSet);
                            values.add(single);
                        }
                    }
                }
            } finally {
                this.unlock();
            }
            return values;
        }, SqlUtils::mergeLists);
    }

    List<R> selectInListIgnoreError(SqlFunction<ResultSet, R> biFunction) {
        this.expectRead();
        try {
            return this.selectInList(biFunction);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static class OrderBy {
        public final String column;
        public final SqlOrder order;

        /**
         * @param column
         * @param order
         */
        public OrderBy(String column, SqlOrder order) {
            this.column = column;
            this.order = order;
        }
    }

    public static enum SqlOrder {
        ASC,
        DESC;
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
