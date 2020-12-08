package com.mytlogos.enterprisedesktop.background;

/**
 *
 */
public class RepositoryProvider {
    private static Repository repository;

    public synchronized Repository provide() {
        if (repository == null) {
            final RepositoryImpl repositoryImpl = new RepositoryImpl();
            TaskManager.runTask(repositoryImpl::initialize);
            repository = repositoryImpl;
        }
        return repository;
    }
}
