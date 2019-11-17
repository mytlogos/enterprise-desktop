package com.mytlogos.enterprisedesktop.background;

import javafx.concurrent.Task;

/**
 *
 */
public class RepositoryProvider {
    private static Repository repository;

    public Repository provide() {
        if (repository == null) {
            synchronized (RepositoryProvider.class) {
                if (repository == null) {
                    final RepositoryImpl repositoryImpl = new RepositoryImpl();
                    TaskManager.runTask(repositoryImpl::initialize);
                    repository = repositoryImpl;
                }
            }
        }
        return repository;
    }
}
