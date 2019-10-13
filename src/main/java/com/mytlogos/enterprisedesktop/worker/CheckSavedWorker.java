package com.mytlogos.enterprisedesktop.worker;















import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.RepositoryProvider;
import com.mytlogos.enterprisedesktop.model.ToDownload;
import com.mytlogos.enterprisedesktop.tools.ContentTool;
import com.mytlogos.enterprisedesktop.tools.FileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckSavedWorker {
    private int correctedSaveState = 0;
    private int clearedLooseEpisodes = 0;
    private int checkedCount;
    private int mediaToCheck;

    public void doWork() {
        Set<ContentTool> tools = FileTools.getSupportedContentTools(null);
        Repository repository = new RepositoryProvider().provide();

        Map<Integer, Set<Integer>> mediumSavedEpisodes = new HashMap<>();

        for (ContentTool tool : tools) {
            this.putItemContainer(tool, mediumSavedEpisodes, true, repository);
            this.putItemContainer(tool, mediumSavedEpisodes, false, repository);
        }

        Map<Integer, Map<Integer, Set<Integer>>> typeMediumSavedEpisodes = new HashMap<>();

        for (Map.Entry<Integer, Set<Integer>> entry : mediumSavedEpisodes.entrySet()) {
            int mediumType = repository.getMediumType(entry.getKey());
            typeMediumSavedEpisodes
                    .computeIfAbsent(mediumType, integer -> new HashMap<>())
                    .put(entry.getKey(), entry.getValue());
        }
        this.mediaToCheck = 0;

        for (Map<Integer, Set<Integer>> map : typeMediumSavedEpisodes.values()) {
            this.mediaToCheck += map.size();
        }

        this.checkedCount = 0;
        this.updateNotificationContentText();

        for (Map.Entry<Integer, Map<Integer, Set<Integer>>> entry : typeMediumSavedEpisodes.entrySet()) {
            Integer mediumType = entry.getKey();
            ContentTool tool = FileTools.getContentTool(mediumType);
            checkLocalContentFiles(tool, repository, entry.getValue());
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private void putItemContainer(ContentTool bookTool, Map<Integer, Set<Integer>> mediumSavedEpisodes, boolean externalSpace, Repository repository) {
        for (Map.Entry<Integer, File> entry : bookTool.getItemContainers(externalSpace).entrySet()) {
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
