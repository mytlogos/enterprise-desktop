package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.worker.DownloadWorker;
import com.mytlogos.enterprisedesktop.worker.SynchronizeService;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TaskController {
    private final SynchronizeService synchronizeService;
    private final DownloadWorker autoDownloadService;
    private final List<DownloadWorker> manualDownloadService = new ArrayList<>();
    private final List<Service<?>> activeServices = new ArrayList<>();
    private StringProperty infoTextProperty;

    public TaskController() {
        this.synchronizeService = new SynchronizeService();
        this.synchronizeService.setOnFailed(event -> event.getSource().getException().printStackTrace());
        this.listen(synchronizeService, false);
        this.synchronizeService.start();

        this.autoDownloadService = new DownloadWorker();
        this.autoDownloadService.setOnFailed(event -> event.getSource().getException().printStackTrace());
        this.listen(autoDownloadService, false);
        this.autoDownloadService.start();
    }

    private void listen(Service<?> service, boolean once) {
        final ChangeListener<Message> messageChangeListener = (observable, oldValue, newValue) -> {
            if (this.infoTextProperty == null) {
                return;
            }
            if (this.activeServices.size() == 1) {
                this.infoTextProperty.set(String.format("%s: %f%% - %s", newValue.title, newValue.progress * 100, newValue.message));
            } else {
                this.infoTextProperty.set(String.format("%d Tasks active", this.activeServices.size()));
            }
        };
        final ObjectBinding<Message> objectBinding = Bindings.createObjectBinding(
                () -> new Message(
                        service.titleProperty().get(),
                        service.messageProperty().get(),
                        service.totalWorkProperty().get(),
                        service.progressProperty().get()
                ),
                service.totalWorkProperty(),
                service.titleProperty(),
                service.messageProperty(),
                service.progressProperty()
        );
        objectBinding.addListener(messageChangeListener);
        service.stateProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                final Worker.State state = service.getState();
                if (state != Worker.State.RUNNING) {
                    if (once && state != Worker.State.SCHEDULED && state != Worker.State.READY) {
                        service.stateProperty().removeListener(this);
                        objectBinding.removeListener(messageChangeListener);
                        service.cancel();
                    }
                    activeServices.remove(service);
                } else {
                    activeServices.add(service);
                }
            }
        });
    }

    public void setInfoTextProperty(StringProperty infoTextProperty) {
        this.infoTextProperty = infoTextProperty;

        if (this.activeServices.size() == 1) {
            final Service<?> service = this.activeServices.get(0);
            this.infoTextProperty.set(String.format("%s: %f%% - %s", service.getTitle(), service.getProgress() * 100, service.getMessage()));
        } else {
            this.infoTextProperty.set(String.format("%d Tasks active", this.activeServices.size()));
        }
    }

    public void startDownloadTask(int medium, List<Integer> episodeIds) {
        final DownloadWorker worker = new DownloadWorker(medium, episodeIds);
        this.manualDownloadService.add(worker);
        this.listen(worker, true);
        worker.start();
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
