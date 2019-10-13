package com.mytlogos.enterprisedesktop.background;

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
                    repositoryImpl.initialize();
                    repository = repositoryImpl;
                }
            }
        }
        return repository;
    }
}
