package com.mytlogos.enterprisedesktop.worker;


import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class AutoDownloadService extends ScheduledService<Void> {
    public AutoDownloadService() {
        this.setBackoffStrategy(LINEAR_BACKOFF_STRATEGY);
        this.setPeriod(Duration.minutes(15));
    }

    @Override
    protected Task<Void> createTask() {
        return new DownloadTask();
    }
}
