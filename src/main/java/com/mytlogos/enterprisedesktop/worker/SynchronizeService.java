package com.mytlogos.enterprisedesktop.worker;

import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.RepositoryProvider;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 *
 */
public class SynchronizeService extends ScheduledService<Void> {
    public SynchronizeService() {
        this.setPeriod(Duration.minutes(15));
        this.setBackoffStrategy(param -> Duration.seconds(5));
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                final Repository repository = new RepositoryProvider().provide();

                if (!repository.isClientAuthenticated()) {
                    throw new IllegalStateException("Not Authenticated");
                }

                repository.syncUser();
                return null;
            }
        };
    }
}
