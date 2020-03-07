package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.TaskManager;
import com.mytlogos.enterprisedesktop.tools.Log;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class InvalidationManager {
    private static InvalidationManager INSTANCE = new InvalidationManager();
    private Map<AbstractTable, Set<Runnable>> tableRunnable = Collections.synchronizedMap(new HashMap<>());
    private Map<AbstractTable, AtomicBoolean> tableInvalidationRunning = Collections.synchronizedMap(new HashMap<>());
    private ExecutorService service = Executors.newFixedThreadPool(5);

    private InvalidationManager() {
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
    }

    public static InvalidationManager get() {
        return INSTANCE;
    }

    public void registerTable(AbstractTable table) {
        final Set<Runnable> values = Collections.newSetFromMap(new WeakHashMap<>());
        this.tableRunnable.put(table, values);
        this.tableInvalidationRunning.put(table, new AtomicBoolean(false));
        table.getInvalidated().observe(invalidated -> {
            final AtomicBoolean invalidationRunning = this.tableInvalidationRunning.get(table);

            if (!invalidated || !invalidationRunning.compareAndSet(false, true)) {
                return;
            }
            System.out.println("Invalidated: " + table.getClass().getName());
            TaskManager.runTask(() -> {
                for (Runnable runnable : values) {
                    runnable.run();
                }
                invalidationRunning.set(false);
                table.clearInvalidated();
            });
        });
    }

    public void removeObserver(Runnable runnable) {
        for (Set<Runnable> runnables : this.tableRunnable.values()) {
            runnables.remove(runnable);
        }
    }

    public void observeInvalidatedTables(Runnable runnable, List<Class<? extends AbstractTable>> tablesToObserve) {
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

    public void addWeakObserver(Runnable runnable, Set<Class<? extends AbstractTable>> dependents) {
        final Set<AbstractTable> tables = this.tableRunnable.keySet();
        for (Class<? extends AbstractTable> tableClass : dependents) {
            AbstractTable matchingTable = null;

            for (AbstractTable table : tables) {
                if (tableClass.isAssignableFrom(table.getClass())) {
                    matchingTable = table;
                    break;
                }
            }

            if (matchingTable == null) {
                Log.warning("Trying to observe unknown Table: " + tableClass.getSimpleName());
                continue;
            }
            final Set<Runnable> runnables = this.tableRunnable.get(matchingTable);
            if (runnable == null) {
                Log.warning("Observing on an unregistered Table: %s", matchingTable.getClass().getSimpleName());
                continue;
            }
            runnables.add(runnable);
        }
    }
}
