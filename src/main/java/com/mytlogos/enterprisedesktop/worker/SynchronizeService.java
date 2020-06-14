package com.mytlogos.enterprisedesktop.worker;

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
        return new SynchronizeTask();
    }
}
