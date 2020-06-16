package com.mytlogos.enterprisedesktop.worker;















import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.model.ToDownload;
import com.mytlogos.enterprisedesktop.tools.ContentTool;
import com.mytlogos.enterprisedesktop.tools.FileTools;
import javafx.concurrent.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CheckSavedWorker extends Task<Void> {
    private int correctedSaveState = 0;
    private int clearedLooseEpisodes = 0;
    private int checkedCount;
    private int mediaToCheck;

    @Override
    protected Void call() {
        this.updateTitle("Check Saved");
        Set<ContentTool> tools = FileTools.getSupportedContentTools();
        Repository repository = ApplicationConfig.getRepository();

        this.updateMessage("Checking for saved Episodes");
        Set<Integer> savedEpisodes = new HashSet<>();

        for (ContentTool tool : tools) {
            savedEpisodes.addAll(this.putItemContainer(tool));
        }
        final List<Integer> previouslySavedEpisodes = repository.getSavedEpisodes();
        Set<Integer> unsavedEpisodes = new HashSet<>(previouslySavedEpisodes);
        // get all the unsaved episodes
        unsavedEpisodes.removeAll(savedEpisodes);
        // get the newly saved episodes, not visible from database
        savedEpisodes.removeAll(previouslySavedEpisodes);

        this.updateMessage("Persisting");
        repository.updateSaved(unsavedEpisodes, false);
        repository.updateSaved(savedEpisodes, true);
        this.updateMessage("Finished");
        return null;
    }

    private void checkLocalContentFiles(ContentTool tool, Repository repository, Map<Integer, Set<Integer>> mediumSavedEpisodes) {
        for (Map.Entry<Integer, Set<Integer>> entry : mediumSavedEpisodes.entrySet()) {
            List<Integer> savedEpisodes = repository.getSavedEpisodes(entry.getKey());

            Set<Integer> unSavedIds = new HashSet<>(savedEpisodes);
            unSavedIds.removeAll(entry.getValue());

            Set<Integer> looseIds = entry.getValue();
            looseIds.removeAll(savedEpisodes);

            if (!unSavedIds.isEmpty()) {
                repository.updateSaved(unSavedIds, false);
                this.correctedSaveState += unSavedIds.size();
            }

            if (!looseIds.isEmpty()) {
                try {
                    tool.removeMediaEpisodes(entry.getKey(), looseIds);
                    this.clearedLooseEpisodes += looseIds.size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.checkedCount++;
            updateNotificationContentText();
        }
    }

    private void updateNotificationContentText() {
    }

    private Set<Integer> putItemContainer(ContentTool bookTool) {
        final List<String> mediaPaths = bookTool.getMediaPaths();
        Set<Integer> savedEpisodes = new HashSet<>();

        for (String mediaPath : mediaPaths) {
            final Map<Integer, String> episodePaths = bookTool.getEpisodePaths(mediaPath);
            savedEpisodes.addAll(episodePaths.keySet());
        }
        return savedEpisodes;
    }

    private void putItemContainerOld(ContentTool bookTool, Map<Integer, Set<Integer>> mediumSavedEpisodes, Repository repository) {
        for (Map.Entry<Integer, File> entry : bookTool.getItemContainers().entrySet()) {
            Map<Integer, String> episodePaths = bookTool.getEpisodePaths(entry.getValue().getAbsolutePath());
            Set<Integer> episodeIds = new HashSet<>(episodePaths.keySet());

            mediumSavedEpisodes.merge(entry.getKey(), episodeIds, (integers, integers2) -> {
                integers.addAll(integers2);
                return integers;
            });
        }
        List<ToDownload> toDownloadList = repository.getToDownload();

        List<Integer> prohibitedMedia = new ArrayList<>();
        Set<Integer> toDownloadMedia = new HashSet<>();

        for (ToDownload toDownload : toDownloadList) {
            if (toDownload.getMediumId() != null) {
                if (toDownload.isProhibited()) {
                    prohibitedMedia.add(toDownload.getMediumId());
                } else {
                    toDownloadMedia.add(toDownload.getMediumId());
                }
            }

            if (toDownload.getExternalListId() != null) {
                toDownloadMedia.addAll(repository.getExternalListItems(toDownload.getExternalListId()));
            }

            if (toDownload.getListId() != null) {
                toDownloadMedia.addAll(repository.getListItems(toDownload.getListId()));
            }
        }

        toDownloadMedia.removeAll(prohibitedMedia);

        for (Integer mediumId : toDownloadMedia) {
            mediumSavedEpisodes.putIfAbsent(mediumId, new HashSet<>());
        }
    }
}
