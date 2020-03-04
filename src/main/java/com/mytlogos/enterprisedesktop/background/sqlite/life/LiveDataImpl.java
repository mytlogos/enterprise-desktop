package com.mytlogos.enterprisedesktop.background.sqlite.life;

import com.mytlogos.enterprisedesktop.tools.SafeRunnable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 *
 */
public class LiveDataImpl<T> extends LiveData<T> {
    final static ExecutorService executor = Executors.newCachedThreadPool();
    final boolean mInTransaction;
    final Callable<T> mComputeFunction;
    final InvalidationTracker.Observer mObserver;
    final AtomicBoolean mInvalid = new AtomicBoolean(true);
    final AtomicBoolean mComputing = new AtomicBoolean(false);
    final AtomicBoolean mRegisteredObserver = new AtomicBoolean(false);
    final Runnable mRefreshRunnable = new SafeRunnable(new Runnable() {
        @Override
        public void run() {
            if (mRegisteredObserver.compareAndSet(false, true)) {
//                mDatabase.getInvalidationTracker().addWeakObserver(mObserver);
            }
            boolean computed;
            do {
                computed = false;
                // compute can happen only in 1 thread but no reason to lock others.
                if (mComputing.compareAndSet(false, true)) {
                    // as long as it is invalid, keep computing.
                    try {
                        T value = null;
                        while (mInvalid.compareAndSet(true, false)) {
                            computed = true;
                            try {
                                value = mComputeFunction.call();
                            } catch (Exception e) {
                                throw new RuntimeException("Exception while computing database live data.", e);
                            }
                        }
                        if (computed) {
                            postValue(value);
                        }
                    } finally {
                        // release compute lock
                        mComputing.set(false);
                    }
                }
                // check invalid after releasing compute lock to avoid the following scenario.
                // Thread A runs compute()
                // Thread A checks invalid, it is false
                // Main thread sets invalid to true
                // Thread B runs, fails to acquire compute lock and skips
                // Thread A releases compute lock
                // We've left invalid in set state. The check below recovers.
            } while (computed && mInvalid.get());
        }
    });
    final Runnable mInvalidationRunnable = new SafeRunnable(() -> {
        boolean isActive = hasActiveObservers();
        if (mInvalid.compareAndSet(false, true)) {
            if (isActive) {
                executor.execute(mRefreshRunnable);
            }
        }
    });

    public LiveDataImpl(boolean mInTransaction, Callable<T> mComputeFunction) {
        this(null, mInTransaction, mComputeFunction, null);
    }

    public LiveDataImpl(Callable<T> mComputeFunction) {
        this(null, false, mComputeFunction, null);
    }

    public LiveDataImpl(T value, boolean mInTransaction, Callable<T> mComputeFunction, InvalidationTracker.Observer mObserver) {
        super(value);
        this.mInTransaction = mInTransaction;
        this.mComputeFunction = mComputeFunction;
        this.mObserver = mObserver;
    }

    public void refresh() {
        executor.execute(mInvalidationRunnable);
    }

    @Override
    protected void onActive() {
        super.onActive();
        executor.execute(mRefreshRunnable);
    }

}
