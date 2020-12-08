package com.mytlogos.enterprisedesktop;

import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.MutableLiveData;
import com.mytlogos.enterprisedesktop.controller.TaskController;
import com.mytlogos.enterprisedesktop.preferences.MainPreferences;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 */
public abstract class ApplicationConfig {
    private static Repository repository;
    private static Future<?> initializeFuture;
    private static final MutableLiveData<Repository> repositoryLiveData = new MutableLiveData<>();
    private static final MainPreferences mainPreferences = new MainPreferences();
    private static final TaskController taskController = new TaskController();

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
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static LiveData<Repository> getLiveDataRepository() {
        return ApplicationConfig.repositoryLiveData;
    }

    public static void setInitializeFuture(Future<?> initializeFuture) {
        ApplicationConfig.initializeFuture = initializeFuture;
    }

    public static void initialize(Repository repository) {
        ApplicationConfig.repository = repository;
        ApplicationConfig.repositoryLiveData.postValue(repository);
    }
}
