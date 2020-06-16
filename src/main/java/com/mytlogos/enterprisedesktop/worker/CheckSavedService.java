package com.mytlogos.enterprisedesktop.worker;


import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 *
 */
public class CheckSavedService extends ScheduledService<Void> {

    public CheckSavedService() {
        this.setPeriod(Duration.minutes(15));
        this.setBackoffStrategy(param -> Duration.seconds(5));
    }

    @Override
    protected Task<Void> createTask() {
        return new CheckSavedWorker();
    }
}
