package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.worker.DownloadWorker;
import com.mytlogos.enterprisedesktop.worker.SynchronizeService;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class TaskController {
    private static final Timer DELAY_TIMER = new Timer("Tasks Remove Delay Timer", true);
    private final SynchronizeService synchronizeService;
    private final DownloadWorker autoDownloadService;
    private TasksHelper helper;

    public TaskController() {
        this.synchronizeService = new SynchronizeService();
        this.synchronizeService.setOnFailed(event -> event.getSource().getException().printStackTrace());

        this.autoDownloadService = new DownloadWorker();
        this.autoDownloadService.setOnFailed(event -> event.getSource().getException().printStackTrace());
    }

    public void startSynchronizeTask() {
        if (this.synchronizeService.getState() == Worker.State.SCHEDULED) {
            this.synchronizeService.restart();
        } else if (this.synchronizeService.getState() == Worker.State.READY) {
            this.synchronizeService.start();
        }
    }

    public void startDownloadTask(int medium, List<Integer> episodeIds) {
        final DownloadWorker worker = new DownloadWorker(medium, episodeIds);
        this.helper.addService(worker);
        this.listenOnce(worker);
    }

    private void listenOnce(Service<?> service) {
        service.stateProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                final Worker.State state = service.getState();

                if (state != Worker.State.RUNNING && state != Worker.State.SCHEDULED && state != Worker.State.READY) {
                    service.stateProperty().removeListener(this);
                    removeService(service);
                }
            }
        });
    }

    private void removeService(Service<?> service) {
        DELAY_TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                Runnable r = () -> helper.removeService(service);

                // We must make sure that it is called from the FX thread.
                if (Platform.isFxApplicationThread()) {
                    r.run();
                } else {
                    Platform.runLater(r);
                }
            }
        }, 10_000);
    }

    void setTasksHelper(TasksHelper helper) {
        this.helper = helper;
        this.helper.addService(this.synchronizeService);
        this.helper.addService(this.autoDownloadService);
    }

    private static class Message {
        final String title;
        final String message;
        final double total;
        final double progress;

        private Message(String title, String message, double total, double progress) {
            this.title = title;
            this.message = message;
            this.total = total;
            this.progress = progress;
        }
    }
}
