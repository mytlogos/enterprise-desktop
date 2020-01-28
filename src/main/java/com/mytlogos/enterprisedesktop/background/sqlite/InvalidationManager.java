package com.mytlogos.enterprisedesktop.background.sqlite;

import io.reactivex.schedulers.Schedulers;

import java.util.*;

/**
 *
 */
public class InvalidationManager {
    private static InvalidationManager INSTANCE = new InvalidationManager();
    private Map<AbstractTable, Set<SqlRunnable>> tableRunnable = Collections.synchronizedMap(new HashMap<>());
    private Map<AbstractTable, Boolean> tableInvalidationRunning = Collections.synchronizedMap(new HashMap<>());

    private InvalidationManager() {
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
    }

    public static InvalidationManager get() {
        return INSTANCE;
    }

    public void registerTable(AbstractTable table) {
        final Set<SqlRunnable> values = Collections.synchronizedSet(new HashSet<>());
        this.tableRunnable.put(table, values);
        //noinspection ResultOfMethodCallIgnored
        table.getInvalidated().subscribeOn(Schedulers.io()).subscribe(invalidated -> {
            final Boolean invalidationRunning = this.tableInvalidationRunning.get(table);

            if (!invalidated || (invalidationRunning != null && invalidationRunning)) {
                return;
            }
            this.tableInvalidationRunning.put(table, true);
            System.out.println("Invalidated: " + table.getClass().getName());

            for (SqlRunnable runnable : values) {
                runnable.run();
            }
            this.tableInvalidationRunning.put(table, false);
            table.clearInvalidated();
        });
    }

    public void removeObserver(SqlRunnable runnable) {
        for (Set<SqlRunnable> runnables : this.tableRunnable.values()) {
            runnables.remove(runnable);
        }
    }

    public void observeInvalidatedTables(SqlRunnable runnable, List<Class<? extends AbstractTable>> tablesToObserve) {
        final Set<AbstractTable> tables = this.tableRunnable.keySet();
        for (Class<? extends AbstractTable> aClass : tablesToObserve) {
            AbstractTable matchingTable = null;

            for (AbstractTable table : tables) {
                if (aClass.isAssignableFrom(table.getClass())) {
                    matchingTable = table;
                    break;
                }
            }

            if (matchingTable == null) {
                throw new IllegalStateException("Trying to observe unknown Table: " + aClass.getName());
            }
            this.tableRunnable.get(matchingTable).add(runnable);
        }
    }
}
