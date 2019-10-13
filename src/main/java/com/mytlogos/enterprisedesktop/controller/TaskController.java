package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.worker.SynchronizeService;

/**
 *
 */
public class TaskController {
    private final SynchronizeService synchronizeService;

    public TaskController() {
        this.synchronizeService = new SynchronizeService();
        this.synchronizeService.setOnFailed(event -> event.getSource().getException().printStackTrace());
        this.synchronizeService.start();
    }
}
