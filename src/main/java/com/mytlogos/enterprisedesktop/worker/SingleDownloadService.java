package com.mytlogos.enterprisedesktop.worker;


import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 *
 */
public class SingleDownloadService extends Service<Void> {
    private final int mediumId;
    private final List<Integer> episodeIds;
    private boolean finished = false;

    public SingleDownloadService(int mediumId, List<Integer> episodeIds) {
        this.mediumId = mediumId;
        this.episodeIds = episodeIds;
    }

    @Override
    protected Task<Void> createTask() {
        if (this.finished) {
            throw new IllegalStateException("Service already finished once");
        }
        return new DownloadTask(this.mediumId, this.episodeIds);
    }

    @Override
    protected void succeeded() {
        this.finished = true;
    }
}
