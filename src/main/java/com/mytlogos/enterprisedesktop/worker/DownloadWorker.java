package com.mytlogos.enterprisedesktop.worker;


import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;
import com.mytlogos.enterprisedesktop.model.MediumType;
import com.mytlogos.enterprisedesktop.model.NotificationItem;
import com.mytlogos.enterprisedesktop.model.SimpleEpisode;
import com.mytlogos.enterprisedesktop.tools.ContentTool;
import com.mytlogos.enterprisedesktop.tools.NotEnoughSpaceException;

import java.io.IOException;
import java.util.*;

public class DownloadWorker {
    private static final int maxPackageSize = 1;
    private static final String mediumId = "mediumId";
    private static final String episodeIds = "episodeIds";
    private static Set<UUID> uuids = Collections.synchronizedSet(new HashSet<>());

    private final int downloadNotificationId = 0x100;
    private Set<ContentTool> contentTools;

    private void filterMediumConstraints(MediumDownload mediumDownload) {
        throw new UnsupportedOperationException();
//        DownloadPreference downloadPreference = UserPreferences.get(this.getApplicationContext()).getDownloadPreference();
//        int type = mediumDownload.mediumType;
//        int sizeLimitMB = downloadPreference.getDownloadLimitSize(type);
//        int id = mediumDownload.id;
//        sizeLimitMB = Math.min(sizeLimitMB, downloadPreference.getMediumDownloadLimitSize(id));
//
//        if (sizeLimitMB < 0) {
//            return;
//        }
//        double maxSize = sizeLimitMB * 1024D * 1024D;
//
//        for (ContentTool tool : this.contentTools) {
//            if (MediumType.is(type, tool.getMedium())) {
//                double averageEpisodeSize = tool.getAverageEpisodeSize(id);
//                String path = tool.getItemPath(id);
//
//                if (path == null || path.isEmpty()) {
//                    break;
//                }
//                int size = tool.getEpisodePaths(path).keySet().size();
//
//                double totalSize = averageEpisodeSize * size;
//
//                for (Iterator<Integer> iterator = mediumDownload.toDownloadEpisodes.iterator(); iterator.hasNext(); ) {
//                    totalSize += averageEpisodeSize;
//
//                    if (totalSize > maxSize) {
//                        iterator.remove();
//                    }
//                }
//                break;
//            }
//        }
    }

    private void download(Repository repository) {
        throw new UnsupportedOperationException();
//        List<ToDownload> toDownloadList = repository.getToDownload();
//
//        List<Integer> prohibitedMedia = new ArrayList<>();
//        Set<Integer> toDownloadMedia = new HashSet<>();
//
//        for (ToDownload toDownload : toDownloadList) {
//            if (toDownload.getMediumId() != null) {
//                if (toDownload.isProhibited()) {
//                    prohibitedMedia.add(toDownload.getMediumId());
//                } else {
//                    toDownloadMedia.add(toDownload.getMediumId());
//                }
//            }
//
//            if (toDownload.getExternalListId() != null) {
//                toDownloadMedia.addAll(repository.getExternalListItems(toDownload.getExternalListId()));
//            }
//
//            if (toDownload.getListId() != null) {
//                toDownloadMedia.addAll(repository.getListItems(toDownload.getListId()));
//            }
//        }
//
//        toDownloadMedia.removeAll(prohibitedMedia);
//
//        Set<MediumDownload> mediumDownloads = new HashSet<>();
//        int downloadCount = 0;
//        DownloadPreference downloadPreference = UserPreferences.get(this.getApplicationContext()).getDownloadPreference();
//
//        for (Integer mediumId : toDownloadMedia) {
//            SimpleMedium medium = repository.getSimpleMedium(mediumId);
//
//            int count = downloadPreference.getDownloadLimitCount(medium.getMedium());
//            List<Integer> episodeIds = repository.getDownloadableEpisodes(mediumId, count);
//            Set<Integer> uniqueEpisodes = new LinkedHashSet<>(episodeIds);
//
//            if (uniqueEpisodes.isEmpty()) {
//                continue;
//            }
//            List<FailedEpisode> failedEpisodes = repository.getFailedEpisodes(uniqueEpisodes);
//
//            for (FailedEpisode failedEpisode : failedEpisodes) {
//                // if it failed 3 times or more, don't try anymore for now
//                if (failedEpisode.getFailCount() < 3) {
//                    continue;
//                }
//                uniqueEpisodes.remove(failedEpisode.getEpisodeId());
//            }
//
//            MediumDownload download = new MediumDownload(
//                    uniqueEpisodes,
//                    mediumId,
//                    medium.getMedium(),
//                    medium.getTitle()
//            );
//
//            this.filterMediumConstraints(download);
//
//            if (download.toDownloadEpisodes.isEmpty()) {
//                continue;
//            }
//
//            mediumDownloads.add(download);
//            downloadCount += uniqueEpisodes.size();
//        }
//        if (!mediumDownloads.isEmpty()) {
//            this.downloadEpisodes(mediumDownloads, repository, downloadCount);
//        } else {
//            this.notificationManager.notify(
//                    this.downloadNotificationId,
//                    this.builder
//                            .setContentTitle("Nothing to Download")
//                            .setContentText(null)
//                            .setProgress(0, 0, false)
//                            .build()
//            );
//        }
    }

    private void downloadData(Repository repository) {
        throw new UnsupportedOperationException();
//        Data data = this.getInputData();
//        int mediumId = data.getInt(DownloadWorker.mediumId, 0);
//        int[] episodeIds = data.getIntArray(DownloadWorker.episodeIds);
//
//        if (mediumId == 0 || episodeIds == null || episodeIds.length == 0) {
//            return;
//        }
//        HashSet<Integer> episodes = new HashSet<>();
//        for (int episodeId : episodeIds) {
//            episodes.add(episodeId);
//        }
//        SimpleMedium medium = repository.getSimpleMedium(mediumId);
//        MediumDownload download = new MediumDownload(episodes, mediumId, medium.getMedium(), medium.getTitle());
//        this.downloadEpisodes(Collections.singleton(download), repository, episodeIds.length);
    }

    /**
     * Download episodes for each medium id,
     * up to an episode limit defined in
     */
    private void downloadEpisodes(Set<MediumDownload> episodeIds, Repository repository, int downloadCount) {
        Collection<DownloadPackage> episodePackages = getDownloadPackages(episodeIds, repository);

        int successFull = 0;
        int notSuccessFull = 0;

        for (DownloadPackage episodePackage : episodePackages) {
//            if (this.isStopped()) {
//                this.stopDownload();
//                return;
//            }

            ContentTool contentTool = null;
            for (ContentTool tool : this.contentTools) {
                if (MediumType.is(tool.getMedium(), episodePackage.mediumType) && tool.isSupported()) {
                    contentTool = tool;
                    break;
                }
            }
            if (contentTool == null) {
                notSuccessFull += episodePackage.episodeIds.size();

                updateProgress(downloadCount, successFull, notSuccessFull);
                continue;
            }
            try {
                List<ClientDownloadedEpisode> downloadedEpisodes = repository.downloadEpisodes(episodePackage.episodeIds);

                List<Integer> currentlySavedEpisodes = new ArrayList<>();

                if (downloadedEpisodes == null) {
                    notSuccessFull = onFailed(
                            downloadCount,
                            successFull,
                            notSuccessFull,
                            repository,
                            episodePackage,
                            false);
                    continue;
                }
                List<ClientDownloadedEpisode> contentEpisodes = new ArrayList<>();

                for (ClientDownloadedEpisode downloadedEpisode : downloadedEpisodes) {
                    int episodeId = downloadedEpisode.getEpisodeId();
                    SimpleEpisode episode = repository.getSimpleEpisode(episodeId);

                    if (downloadedEpisode.getContent().length > 0) {
                        successFull++;
                        currentlySavedEpisodes.add(episodeId);
                        contentEpisodes.add(downloadedEpisode);
                        repository.addNotification(NotificationItem.createNow(
                                String.format("Episode %s of %s saved", episode.getFormattedTitle(), episodePackage.mediumTitle),
                                ""
                        ));
                    } else {
                        notSuccessFull++;
                        repository.updateFailedDownloads(episodeId);
                        repository.addNotification(NotificationItem.createNow(
                                String.format("Could not save Episode %s of %s", episode.getFormattedTitle(), episodePackage.mediumTitle),
                                ""
                        ));
                    }
                }
                contentTool.saveContent(contentEpisodes, episodePackage.mediumId);
                repository.updateSaved(currentlySavedEpisodes, true);

                updateProgress(downloadCount, successFull, notSuccessFull);
            } catch (NotEnoughSpaceException e) {
                notSuccessFull = onFailed(
                        downloadCount,
                        successFull,
                        notSuccessFull,
                        repository,
                        episodePackage,
                        true
                );
            } catch (IOException e) {
                e.printStackTrace();
                notSuccessFull = onFailed(
                        downloadCount,
                        successFull,
                        notSuccessFull,
                        repository,
                        episodePackage,
                        false);
            }
        }
    }

    private Collection<DownloadPackage> getDownloadPackages(Set<MediumDownload> episodeIds, Repository repository) {
        List<Integer> savedEpisodes = repository.getSavedEpisodes();

        Set<Integer> savedIds = new HashSet<>(savedEpisodes);

        Collection<DownloadPackage> episodePackages = new ArrayList<>();

        for (MediumDownload mediumDownload : episodeIds) {
            DownloadPackage downloadPackage = new DownloadPackage(
                    mediumDownload.id,
                    mediumDownload.mediumType,
                    mediumDownload.title
            );

            for (Integer episodeId : mediumDownload.toDownloadEpisodes) {

                if (savedIds.contains(episodeId)) {
                    continue;
                }

                if (downloadPackage.episodeIds.size() == maxPackageSize) {
                    episodePackages.add(downloadPackage);
                    downloadPackage = new DownloadPackage(
                            mediumDownload.id,
                            mediumDownload.mediumType,
                            mediumDownload.title
                    );
                }

                downloadPackage.episodeIds.add(episodeId);
            }
            if (!downloadPackage.episodeIds.isEmpty()) {
                episodePackages.add(downloadPackage);
            }
        }
        return episodePackages;
    }

    private void updateProgress(int downloadCount, int successFull, int notSuccessFull) {
        int progress = successFull + notSuccessFull;
//        this.builder.setContentTitle(String.format("Download in Progress [%s/%s]", progress, downloadCount));
//        this.builder.setContentText(String.format("Failed: %s", notSuccessFull));
//        this.builder.setProgress(downloadCount, progress, false);
//        this.notificationManager.notify(this.downloadNotificationId, this.builder.build());
    }

    private int onFailed(int downloadCount, int successFull, int notSuccessFull, Repository repository, DownloadPackage downloadPackage, boolean notEnoughSpace) {
        notSuccessFull += downloadPackage.episodeIds.size();

        for (Integer episodeId : downloadPackage.episodeIds) {
            repository.updateFailedDownloads(episodeId);

            SimpleEpisode episode = repository.getSimpleEpisode(episodeId);
            String format = notEnoughSpace ? "Not enough Space for Episode %s of %s" : "Could not save Episode %s of %s";

            repository.addNotification(NotificationItem.createNow(
                    String.format(format, episode.getFormattedTitle(), downloadPackage.mediumTitle),
                    ""
            ));
        }

        updateProgress(downloadCount, successFull, notSuccessFull);
        return notSuccessFull;
    }

    private void stopDownload() {
    }

    private void cleanUp() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MediumDownload {
        private final Set<Integer> toDownloadEpisodes;
        private final int id;
        private final int mediumType;
        private final String title;

        private MediumDownload(Set<Integer> toDownloadEpisodes, int id, int mediumType, String title) {
            this.toDownloadEpisodes = toDownloadEpisodes;
            this.id = id;
            this.mediumType = mediumType;
            this.title = title;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MediumDownload that = (MediumDownload) o;

            return id == that.id;
        }
    }

    private static class DownloadPackage {
        private final Set<Integer> episodeIds = new HashSet<>();
        private final int mediumId;
        private final int mediumType;
        private final String mediumTitle;

        private DownloadPackage(int mediumId, int mediumType, String mediumTitle) {
            this.mediumId = mediumId;
            this.mediumType = mediumType;
            this.mediumTitle = mediumTitle;
        }
    }
}
