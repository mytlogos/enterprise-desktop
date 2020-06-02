package com.mytlogos.enterprisedesktop.worker;


import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.preferences.DownloadPreferences;
import com.mytlogos.enterprisedesktop.tools.ContentTool;
import com.mytlogos.enterprisedesktop.tools.FileTools;
import com.mytlogos.enterprisedesktop.tools.NotEnoughSpaceException;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class DownloadWorker extends ScheduledService<Void> {
    private static final int maxPackageSize = 1;
    private final int mediumId;
    private final List<Integer> episodeIds;
    private final DownloadPreferences downloadPreference = ApplicationConfig.getMainPreferences().getDownloadPreferences();
    private Set<ContentTool> contentTools;

    public DownloadWorker() {
        this(-1, Collections.emptyList());
        this.setBackoffStrategy(LINEAR_BACKOFF_STRATEGY);
        this.setPeriod(Duration.minutes(15));
    }

    public DownloadWorker(int mediumId, List<Integer> episodeIds) {
        this.mediumId = mediumId;
        this.episodeIds = episodeIds;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                LiveData<Repository> repositoryLiveData = ApplicationConfig.getLiveDataRepository();
                try {
                    final Repository repository = repositoryLiveData.firstElement().get();
                    contentTools = FileTools.getSupportedContentTools();

                    if (!repository.isClientAuthenticated()) {
                        this.updateTitle("No Download: Not Authenticated");
                        cleanUp();
                        return null;
                    }
                    if (!repository.isClientOnline()) {
                        this.updateTitle("Download: Server not in reach");
                        cleanUp();
                        return null;
                    }
                    this.updateTitle("Getting Data for Download");
                    this.updateProgress(0, 0);

                    if (mediumId <= 0) {
                        download(repository);
                        Thread.currentThread().setName("Auto-DownloadWorker");
                    } else {
                        Thread.currentThread().setName(String.format("DownloadWorker-Medium-%d-Items-%d", mediumId, DownloadWorker.this.episodeIds.size()));
                        downloadData(repository);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                cleanUp();
                return null;
            }

            private void download(Repository repository) {
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

                Set<MediumDownload> mediumDownloads = new HashSet<>();
                int downloadCount = 0;

                for (Integer mediumId : toDownloadMedia) {
                    SimpleMedium medium = repository.getSimpleMedium(mediumId);

                    int count = downloadPreference.getDownloadLimitCount(medium.getMedium());
                    List<Integer> episodeIds = repository.getDownloadableEpisodes(mediumId, count);
                    Set<Integer> uniqueEpisodes = new LinkedHashSet<>(episodeIds);

                    if (uniqueEpisodes.isEmpty()) {
                        continue;
                    }
                    List<FailedEpisode> failedEpisodes = repository.getFailedEpisodes(uniqueEpisodes);

                    for (FailedEpisode failedEpisode : failedEpisodes) {
                        // if it failed 3 times or more, don't try anymore for now
                        if (failedEpisode.getFailCount() < 3) {
                            continue;
                        }
                        uniqueEpisodes.remove(failedEpisode.getEpisodeId());
                    }

                    MediumDownload download = new MediumDownload(
                            uniqueEpisodes,
                            mediumId,
                            medium.getMedium(),
                            medium.getTitle()
                    );

                    filterMediumConstraints(download);

                    if (download.toDownloadEpisodes.isEmpty()) {
                        continue;
                    }

                    mediumDownloads.add(download);
                    downloadCount += uniqueEpisodes.size();
                }

                if (!mediumDownloads.isEmpty()) {
                    downloadEpisodes(mediumDownloads, repository, downloadCount);
                } else {
                    updateTitle("Nothing to Download");
                    updateProgress(0, 0);
                }
            }

            private void updateProgress(int downloadCount, int successFull, int notSuccessFull) {
                int progress = successFull + notSuccessFull;
                updateTitle(String.format("Download in Progress [%s/%s]", progress, downloadCount));
                updateMessage(String.format("Failed: %s", notSuccessFull));
                updateProgress(progress, downloadCount);
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

            /**
             * Download episodes for each medium id,
             * up to an episode limit defined in
             */
            private void downloadEpisodes(Set<MediumDownload> episodeIds, Repository repository, int downloadCount) {
                Collection<DownloadPackage> episodePackages = getDownloadPackages(episodeIds, repository);

                int successFull = 0;
                int notSuccessFull = 0;

                for (DownloadPackage episodePackage : episodePackages) {
                    ContentTool contentTool = null;
                    for (ContentTool tool : contentTools) {
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

            private void downloadData(Repository repository) {
                if (mediumId == 0 || episodeIds == null || episodeIds.isEmpty()) {
                    return;
                }
                HashSet<Integer> episodes = new HashSet<>(episodeIds);
                SimpleMedium medium = repository.getSimpleMedium(mediumId);
                MediumDownload download = new MediumDownload(episodes, mediumId, medium.getMedium(), medium.getTitle());
                this.downloadEpisodes(Collections.singleton(download), repository, episodeIds.size());
            }
        };
    }

    private void cleanUp() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void filterMediumConstraints(MediumDownload mediumDownload) {
        int type = mediumDownload.mediumType;
        int sizeLimitMB = this.downloadPreference.getDownloadLimitSize(type);
        int id = mediumDownload.id;
        sizeLimitMB = Math.min(sizeLimitMB, downloadPreference.getMediumDownloadLimitSize(id));

        if (sizeLimitMB < 0) {
            return;
        }
        double maxSize = sizeLimitMB * 1024D * 1024D;

        for (ContentTool tool : this.contentTools) {
            if (MediumType.is(type, tool.getMedium())) {
                double averageEpisodeSize = tool.getAverageEpisodeSize(id);
                String path = tool.getItemPath(id);

                if (path == null || path.isEmpty()) {
                    break;
                }
                int size = tool.getEpisodePaths(path).keySet().size();

                double totalSize = averageEpisodeSize * size;

                for (Iterator<Integer> iterator = mediumDownload.toDownloadEpisodes.iterator(); iterator.hasNext(); ) {
                    totalSize += averageEpisodeSize;

                    if (totalSize > maxSize) {
                        iterator.remove();
                    }
                }
                break;
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

    @Override
    protected void succeeded() {
        // if it is only a single usage DownloadWorker
        if (this.mediumId > 0) {
            this.cancel();
        } else {
            super.succeeded();
        }
    }

    @Override
    protected void failed() {
        // if it is only a single usage DownloadWorker
        if (this.mediumId > 0) {
            this.cancel();
        } else {
            super.failed();
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
