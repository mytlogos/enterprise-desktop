package com.mytlogos.enterprisedesktop;

import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.controller.TaskController;
import com.mytlogos.enterprisedesktop.preferences.MainPreferences;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 */
public abstract class ApplicationConfig {
    private static Repository repository;
    private static Future<?> initializeFuture;
    private static Subject<Repository> repositorySubject = PublishSubject.create();
    private static final Flowable<Repository> repositoryFlowable = repositorySubject.toFlowable(BackpressureStrategy.BUFFER);
    private static MainPreferences mainPreferences = new MainPreferences();
    private static TaskController taskController = new TaskController();

    private ApplicationConfig() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static MainPreferences getMainPreferences() {
        return mainPreferences;
    }

    public static TaskController getTaskController() {
        return taskController;
    }

    public static Repository getRepository() {
        waitForInitialize();
        return repository;
    }

    private static void waitForInitialize() {
        if (initializeFuture == null) {
            return;
        }
        try {
            initializeFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static Flowable<Repository> getFlowableRepository() {
        return Flowable.create(emitter -> {
            if (repository != null) {
                emitter.onNext(repository);
            } else {
                final Disposable subscribe = repositorySubject.subscribe(emitter::onNext);
                emitter.setDisposable(subscribe);
            }
        }, BackpressureStrategy.BUFFER);
    }

    public static void setInitializeFuture(Future<?> initializeFuture) {
        ApplicationConfig.initializeFuture = initializeFuture;
    }

    public static void initialize(Repository repository) {
        ApplicationConfig.repository = repository;
        ApplicationConfig.repositorySubject.onNext(repository);
    }
}
