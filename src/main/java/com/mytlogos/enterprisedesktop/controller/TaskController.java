package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.background.TaskManager;
import com.mytlogos.enterprisedesktop.worker.AutoDownloadService;
import com.mytlogos.enterprisedesktop.worker.CheckSavedService;
import com.mytlogos.enterprisedesktop.worker.SingleDownloadService;
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
    private final AutoDownloadService autoDownloadService;
    private final CheckSavedService checkSavedService;
    private TasksHelper helper;

    public TaskController() {
        this.synchronizeService = new SynchronizeService();
        this.synchronizeService.setOnFailed(event -> event.getSource().getException().printStackTrace());

        this.checkSavedService = new CheckSavedService();
        this.checkSavedService.setOnFailed(event -> event.getSource().getException().printStackTrace());

        this.autoDownloadService = new AutoDownloadService();
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
        final SingleDownloadService worker = new SingleDownloadService(medium, episodeIds);
        TaskManager.runFxTask(() -> {
            this.helper.addService(worker);
            this.listenOnce(worker);
        });
    }

    private void listenOnce(Service<?> service) {
        service.setOnFailed(event -> event.getSource().getException().printStackTrace());
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
        DELAY_TIMER.schedule(new RemoveServiceTask(service), 10_000);
    }

    void setTasksHelper(TasksHelper helper) {
        this.helper = helper;
        this.helper.addService(this.synchronizeService);
        this.helper.addService(this.autoDownloadService);
        this.helper.addService(this.checkSavedService);
    }

    private class RemoveServiceTask extends TimerTask {
        private final Service<?> service;

        public RemoveServiceTask(Service<?> service) {
            this.service = service;
        }

        @Override
        public void run() {
            Runnable r = () -> helper.removeService(this.service);

            // We must make sure that it is called from the FX thread.
            if (Platform.isFxApplicationThread()) {
                r.run();
            } else {
                Platform.runLater(r);
            }
        }
    }
}
