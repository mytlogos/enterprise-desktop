package com.mytlogos.enterprisedesktop.background.sqlite.life;

import io.reactivex.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link LiveData} subclass which may observe other {@code LiveData} objects and react on
 * {@code OnChanged} events from them.
 * <p>
 * This class correctly propagates its active/inactive states down to source {@code LiveData}
 * objects.
 * <p>
 * Consider the following scenario: we have 2 instances of {@code LiveData}, let's name them
 * {@code liveData1} and {@code liveData2}, and we want to merge their emissions in one object:
 * {@code liveDataMerger}. Then, {@code liveData1} and {@code liveData2} will become sources for
 * the {@code MediatorLiveData liveDataMerger} and every time {@code onChanged} callback
 * is called for either of them, we set a new value in {@code liveDataMerger}.
 *
 * <pre>
 * LiveData<Integer> liveData1 = ...;
 * LiveData<Integer> liveData2 = ...;
 *
 * MediatorLiveData<Integer> liveDataMerger = new MediatorLiveData<>();
 * liveDataMerger.addSource(liveData1, value -> liveDataMerger.setValue(value));
 * liveDataMerger.addSource(liveData2, value -> liveDataMerger.setValue(value));
 * </pre>
 * <p>
 * Let's consider that we only want 10 values emitted by {@code liveData1}, to be
 * merged in the {@code liveDataMerger}. Then, after 10 values, we can stop listening to {@code
 * liveData1} and remove it as a source.
 * <pre>
 * liveDataMerger.addSource(liveData1, new Observer<Integer>() {
 *      private int count = 1;
 *
 *      {@literal @}Override public void onChanged(@Nullable Integer s) {
 *          count++;
 *          liveDataMerger.setValue(s);
 *          if (count > 10) {
 *              liveDataMerger.removeSource(liveData1);
 *          }
 *      }
 * });
 * </pre>
 *
 * @param <T> The type of data hold by this instance
 */
public class MediatorLiveData<T> extends MutableLiveData<T> {
    private Map<LiveData<?>, Source<?>> mSources = new HashMap<>();

    /**
     * Starts to listen the given {@code source} LiveData, {@code onChanged} observer will be called
     * when {@code source} value was changed.
     * <p>
     * {@code onChanged} callback will be called only when this {@code MediatorLiveData} is active.
     * <p> If the given LiveData is already added as a source but with a different Observer,
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param source    the {@code LiveData} to listen to
     * @param onChanged The observer that will receive the events
     * @param <S>       The type of data hold by {@code source} LiveData
     */
    public <S> void addSource(@NonNull LiveData<S> source, @NonNull Observer<? super S> onChanged) {
        Source<S> e = new Source<>(source, onChanged);
        Source<?> existing = mSources.putIfAbsent(source, e);
        if (existing != null && existing.mObserver != onChanged) {
            throw new IllegalArgumentException(
                    "This source was already added with the different observer");
        }
        if (existing != null) {
            return;
        }
        if (hasActiveObservers()) {
            e.plug();
        }
    }

    /**
     * Stops to listen the given {@code LiveData}.
     *
     * @param toRemote {@code LiveData} to stop to listen
     * @param <S>      the type of data hold by {@code source} LiveData
     */
    
    public <S> void removeSource(@NonNull LiveData<S> toRemote) {
        Source<?> source = mSources.remove(toRemote);
        if (source != null) {
            source.unplug();
        }
    }

    
    @Override
    protected void onActive() {
        for (Map.Entry<LiveData<?>, Source<?>> source : mSources.entrySet()) {
            source.getValue().plug();
        }
    }

    
    @Override
    protected void onInactive() {
        for (Map.Entry<LiveData<?>, Source<?>> source : mSources.entrySet()) {
            source.getValue().unplug();
        }
    }

    private static class Source<V> implements Observer<V> {
        final LiveData<V> mLiveData;
        final Observer<? super V> mObserver;
        int mVersion = START_VERSION;

        Source(LiveData<V> liveData, final Observer<? super V> observer) {
            mLiveData = liveData;
            mObserver = observer;
        }

        @Override
        public void onChanged(V v) {
            if (mVersion != mLiveData.getVersion()) {
                mVersion = mLiveData.getVersion();
                mObserver.onChanged(v);
            }
        }

        void plug() {
            mLiveData.observe(this);
        }

        void unplug() {
            mLiveData.removeObserver(this);
        }
    }
}

