package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.*;
import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.background.resourceLoader.DependantValue;
import com.mytlogos.enterprisedesktop.background.resourceLoader.DependencyTask;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorkGenerator;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorker;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Sortings;
import io.reactivex.rxjava3.core.Flowable;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
public class SqliteStorage implements DatabaseStorage {
    private final UserTable userTable;

    public SqliteStorage() {
        this.userTable = new UserTable();
        this.userTable.initialize();
    }


    @Override
    public Flowable<User> getUser() {
        return null;
    }

    @Override
    public User getUserNow() {
        return null;
    }

    @Override
    public Flowable<HomeStats> getHomeStats() {
        return null;
    }

    @Override
    public void deleteAllUser() {

    }

    @Override
    public ClientModelPersister getPersister(Repository repository, LoadData loadedData) {
        return new SqlitePersister();
    }

    @Override
    public DependantGenerator getDependantGenerator(LoadData loadedData) {
        return new SqliteDependantGenerator(loadedData);
    }

    @Override
    public void deleteOldNews() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void setLoading(boolean loading) {

    }

    @Override
    public LoadData getLoadData() {
        return null;
    }

    @Override
    public Flowable<PagedList<News>> getNews() {
        return null;
    }

    @Override
    public List<Integer> getSavedEpisodes() {
        return null;
    }

    @Override
    public List<Integer> getToDeleteEpisodes() {
        return null;
    }

    @Override
    public void updateSaved(int episodeId, boolean saved) {

    }

    @Override
    public void updateSaved(Collection<Integer> episodeIds, boolean saved) {

    }

    @Override
    public List<ToDownload> getAllToDownloads() {
        return null;
    }

    @Override
    public void removeToDownloads(Collection<ToDownload> toDownloads) {

    }

    @Override
    public Collection<Integer> getListItems(Integer listId) {
        return null;
    }

    @Override
    public Flowable<List<Integer>> getLiveListItems(Integer listId) {
        return null;
    }

    @Override
    public Collection<Integer> getExternalListItems(Integer externalListId) {
        return null;
    }

    @Override
    public Flowable<List<Integer>> getLiveExternalListItems(Integer externalListId) {
        return null;
    }

    @Override
    public List<Integer> getDownloadableEpisodes(Integer mediumId, int limit) {
        return null;
    }

    @Override
    public List<Integer> getDownloadableEpisodes(Collection<Integer> mediumId) {
        return null;
    }

    @Override
    public Flowable<PagedList<DisplayRelease>> getDisplayEpisodes(int saved, int medium, int read, int minIndex, int maxIndex, boolean latestOnly) {
        return null;
    }

    @Override
    public Flowable<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium) {
        return null;
    }

    @Override
    public Flowable<List<MediaList>> getLists() {
        return null;
    }

    @Override
    public void insertDanglingMedia(Collection<Integer> mediaIds) {

    }

    @Override
    public void removeDanglingMedia(Collection<Integer> mediaIds) {

    }

    @Override
    public Flowable<? extends MediaListSetting> getListSetting(int id, boolean isExternal) {
        return null;
    }

    @Override
    public MediaListSetting getListSettingNow(int id, boolean isExternal) {
        return null;
    }

    @Override
    public void updateToDownload(boolean add, ToDownload toDownload) {

    }

    @Override
    public Flowable<PagedList<MediumItem>> getAllMedia(Sortings sortings, String title, int medium, String author, LocalDateTime lastUpdate, int minCountEpisodes, int minCountReadEpisodes) {
        return null;
    }

    @Override
    public Flowable<MediumSetting> getMediumSettings(int mediumId) {
        return null;
    }

    @Override
    public MediumSetting getMediumSettingsNow(int mediumId) {
        return null;
    }

    @Override
    public Flowable<PagedList<TocEpisode>> getToc(int mediumId, Sortings sortings, byte read, byte saved) {
        return null;
    }

    @Override
    public Flowable<List<MediumItem>> getMediumItems(int listId, boolean isExternal) {
        return null;
    }

    @Override
    public boolean listExists(String listName) {
        return false;
    }

    @Override
    public int countSavedEpisodes(Integer mediumId) {
        return 0;
    }

    @Override
    public List<Integer> getSavedEpisodes(int mediumId) {
        return null;
    }

    @Override
    public Episode getEpisode(int episodeId) {
        return null;
    }

    @Override
    public List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> ids) {
        return null;
    }

    @Override
    public void updateProgress(Collection<Integer> episodeIds, float progress) {

    }

    @Override
    public Flowable<PagedList<MediumInWait>> getMediaInWaitBy(String filter, int mediumFilter, String hostFilter, Sortings sortings) {
        return null;
    }

    @Override
    public Flowable<PagedList<ReadEpisode>> getReadTodayEpisodes() {
        return null;
    }

    @Override
    public Flowable<List<MediaList>> getInternLists() {
        return null;
    }

    @Override
    public void addItemsToList(int listId, Collection<Integer> ids) {

    }

    @Override
    public Flowable<List<MediumInWait>> getSimilarMediaInWait(MediumInWait mediumInWait) {
        return null;
    }

    @Override
    public Flowable<List<SimpleMedium>> getMediaSuggestions(String title, int medium) {
        return null;
    }

    @Override
    public Flowable<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium) {
        return null;
    }

    @Override
    public Flowable<List<MediaList>> getListSuggestion(String name) {
        return null;
    }

    @Override
    public Flowable<Boolean> onDownloadAble() {
        return null;
    }

    @Override
    public void clearMediaInWait() {

    }

    @Override
    public void deleteMediaInWait(Collection<MediumInWait> toDelete) {

    }

    @Override
    public Flowable<List<MediumItem>> getAllDanglingMedia() {
        return null;
    }

    @Override
    public void removeItemFromList(int listId, int mediumId) {

    }

    @Override
    public void removeItemFromList(int listId, Collection<Integer> mediumId) {

    }

    @Override
    public void moveItemsToList(int oldListId, int listId, Collection<Integer> ids) {

    }

    @Override
    public Flowable<PagedList<ExternalUser>> getExternalUser() {
        return null;
    }

    @Override
    public SpaceMedium getSpaceMedium(int mediumId) {
        return null;
    }

    @Override
    public int getMediumType(Integer mediumId) {
        return 0;
    }

    @Override
    public List<String> getReleaseLinks(int episodeId) {
        return null;
    }

    @Override
    public void clearLocalMediaData() {

    }

    @Override
    public Flowable<PagedList<NotificationItem>> getNotifications() {
        return null;
    }

    @Override
    public void updateFailedDownload(int episodeId) {

    }

    @Override
    public List<FailedEpisode> getFailedEpisodes(Collection<Integer> episodeIds) {
        return null;
    }

    @Override
    public void addNotification(NotificationItem notification) {

    }

    @Override
    public SimpleEpisode getSimpleEpisode(int episodeId) {
        return null;
    }

    @Override
    public SimpleMedium getSimpleMedium(int mediumId) {
        return null;
    }

    @Override
    public void clearNotifications() {

    }

    @Override
    public void clearFailEpisodes() {

    }

    @Override
    public Collection<Integer> getAllEpisodes(int mediumId) {
        return null;
    }

    @Override
    public void syncProgress() {

    }

    @Override
    public void updateDataStructure(List<Integer> mediaIds, List<Integer> partIds) {

    }

    @Override
    public List<Integer> getEpisodeIdsWithHigherIndex(double combiIndex, int mediumId, boolean read) {
        return null;
    }

    @Override
    public List<Integer> getEpisodeIdsWithHigherIndex(double combiIndex, int mediumId) {
        return null;
    }

    @Override
    public List<Integer> getEpisodeIdsWithLowerIndex(double combiIndex, int mediumId, boolean read) {
        return null;
    }

    @Override
    public List<Integer> getEpisodeIdsWithLowerIndex(double combiIndex, int mediumId) {
        return null;
    }

    @Override
    public Collection<Integer> getSavedEpisodeIdsWithHigherIndex(double combiIndex, int mediumId) {
        return null;
    }

    @Override
    public Collection<Integer> getSavedEpisodeIdsWithLowerIndex(double combiIndex, int mediumId) {
        return null;
    }

    @Override
    public void removeEpisodes(List<Integer> episodeIds) {

    }

    @Override
    public void removeParts(Collection<Integer> partIds) {

    }

    @Override
    public List<Integer> getReadEpisodes(Collection<Integer> episodeIds, boolean read) {
        return null;
    }

    @Override
    public void insertEditEvent(EditEvent event) {

    }

    @Override
    public void insertEditEvent(Collection<EditEvent> events) {

    }

    @Override
    public List<? extends EditEvent> getEditEvents() {
        return null;
    }

    @Override
    public void removeEditEvents(Collection<EditEvent> editEvents) {

    }

    private class SqliteDependantGenerator implements DependantGenerator {
        private final LoadData loadedData;

        private SqliteDependantGenerator(LoadData loadedData) {
            this.loadedData = loadedData;
        }

        @Override
        public Collection<DependencyTask<?>> generateReadEpisodesDependant(LoadWorkGenerator.FilteredReadEpisodes readEpisodes) {
            Set<DependencyTask<?>> tasks = new HashSet<>();
            LoadWorker worker = LoadWorker.getWorker();

            for (LoadWorkGenerator.IntDependency<ClientReadEpisode> dependency : readEpisodes.dependencies) {
                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(dependency.dependency),
                        worker.EPISODE_LOADER
                ));
            }

            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generatePartsDependant(LoadWorkGenerator.FilteredParts parts) {
            Set<DependencyTask<?>> tasks = new HashSet<>();

            LoadWorker worker = LoadWorker.getWorker();
            for (LoadWorkGenerator.IntDependency<ClientPart> dependency : parts.mediumDependencies) {
                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.PART_LOADER
                        ),
                        worker.MEDIUM_LOADER
                ));
            }
            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generateEpisodesDependant(LoadWorkGenerator.FilteredEpisodes episodes) {
            Set<DependencyTask<?>> tasks = new HashSet<>();
            LoadWorker worker = LoadWorker.getWorker();

            for (LoadWorkGenerator.IntDependency<ClientEpisode> dependency : episodes.partDependencies) {
                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.EPISODE_LOADER
                        ),
                        worker.PART_LOADER
                ));
            }
            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generateMediaDependant(LoadWorkGenerator.FilteredMedia media) {
            Set<DependencyTask<?>> tasks = new HashSet<>();

            LoadWorker worker = LoadWorker.getWorker();
            for (LoadWorkGenerator.IntDependency<ClientMedium> dependency : media.episodeDependencies) {
                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.MEDIUM_LOADER
                        ),
                        worker.EPISODE_LOADER
                ));
            }
            for (Integer unloadedPart : media.unloadedParts) {
                tasks.add(new DependencyTask<>(unloadedPart, null, worker.PART_LOADER));
            }
            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generateMediaListsDependant(LoadWorkGenerator.FilteredMediaList mediaLists) {
            Set<DependencyTask<?>> tasks = new HashSet<>();

            LoadWorker worker = LoadWorker.getWorker();
//                RoomConverter converter = new RoomConverter(this.loadedData);
//
//                for (LoadWorkGenerator.IntDependency<List<LoadWorkGenerator.ListJoin>> dependency : mediaLists.mediumDependencies) {
//                    int tmpListId = 0;
//                    if (!dependency.dependency.isEmpty()) {
//                        tmpListId = dependency.dependency.get(0).listId;
//                    }
//                    int listId = tmpListId;
//
//                    tasks.add(new DependencyTask<>(
//                            dependency.id,
//                            new DependantValue(
//                                    converter.convertListJoin(dependency.dependency),
//                                    () -> RoomStorage.this.mediaListDao.clearJoin(listId)
//                            ),
//                            worker.MEDIUM_LOADER
//                    ));
//                }
            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generateExternalMediaListsDependant(LoadWorkGenerator.FilteredExtMediaList externalMediaLists) {
            Set<DependencyTask<?>> tasks = new HashSet<>();

            LoadWorker worker = LoadWorker.getWorker();
//                RoomConverter converter = new RoomConverter(this.loadedData);
//
//                for (LoadWorkGenerator.IntDependency<List<LoadWorkGenerator.ListJoin>> dependency : externalMediaLists.mediumDependencies) {
//                    int tmpListId = 0;
//                    if (!dependency.dependency.isEmpty()) {
//                        tmpListId = dependency.dependency.get(0).listId;
//                    }
//                    int listId = tmpListId;
//
//                    tasks.add(new DependencyTask<>(
//                            dependency.id,
//                            new DependantValue(
//                                    converter.convertExListJoin(dependency.dependency),
//                                    () -> RoomStorage.this.externalMediaListDao.clearJoin(listId)
//                            ),
//                            worker.MEDIUM_LOADER
//                    ));
//                }
//                for (LoadWorkGenerator.Dependency<String, ClientExternalMediaList> dependency : externalMediaLists.userDependencies) {
//                    tasks.add(new DependencyTask<>(
//                            dependency.id,
//                            new DependantValue(
//                                    converter.convert(dependency.dependency),
//                                    dependency.dependency.getId(),
//                                    worker.EXTERNAL_MEDIALIST_LOADER
//                            ),
//                            worker.EXTERNAL_USER_LOADER
//                    ));
//                }
            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generateExternalUsersDependant(LoadWorkGenerator.FilteredExternalUser externalUsers) {
            Set<DependencyTask<?>> tasks = new HashSet<>();

            LoadWorker worker = LoadWorker.getWorker();
//                RoomConverter converter = new RoomConverter(this.loadedData);
//
//                for (LoadWorkGenerator.IntDependency<List<LoadWorkGenerator.ListJoin>> dependency : externalUsers.mediumDependencies) {
//                    int tmpListId = 0;
//                    if (!dependency.dependency.isEmpty()) {
//                        tmpListId = dependency.dependency.get(0).listId;
//                    }
//                    int listId = tmpListId;
//
//                    tasks.add(new DependencyTask<>(
//                            dependency.id,
//                            new DependantValue(
//                                    converter.convertExListJoin(dependency.dependency),
//                                    () -> RoomStorage.this.externalMediaListDao.clearJoin(listId)
//                            ),
//                            worker.MEDIUM_LOADER
//                    ));
//                }
            return tasks;
        }
    }


    private class SqlitePersister implements ClientModelPersister {
        @Override
        public Collection<ClientConsumer<?>> getConsumer() {
            return new ArrayList<>();
        }

        @Override
        public ClientModelPersister persistEpisodes(Collection<ClientEpisode> episode) {
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredEpisodes filteredEpisodes) {
            return this;
        }

        @Override
        public ClientModelPersister persistMediaLists(Collection<ClientMediaList> mediaLists) {
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredMediaList filteredMediaList) {
            return this;
        }

        @Override
        public ClientModelPersister persistExternalMediaLists(Collection<ClientExternalMediaList> externalMediaLists) {
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList) {
            return this;
        }

        @Override
        public ClientModelPersister persistExternalUsers(Collection<ClientExternalUser> externalUsers) {
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredExternalUser filteredExternalUser) {
            return this;
        }

        @Override
        public ClientModelPersister persistMedia(Collection<ClientMedium> media) {
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredMedia filteredMedia) {
            return this;
        }

        @Override
        public ClientModelPersister persistNews(Collection<ClientNews> news) {
            return this;
        }

        @Override
        public ClientModelPersister persistParts(Collection<ClientPart> parts) {
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredReadEpisodes filteredReadEpisodes) {
            return this;
        }

        @Override
        public ClientModelPersister persist(ClientListQuery query) {
            return this;
        }

        @Override
        public ClientModelPersister persist(ClientMultiListQuery query) {
            return this;
        }

        @Override
        public ClientModelPersister persist(ClientUser user) {
            return this;
        }

        @Override
        public ClientModelPersister persist(ClientUpdateUser user) {
            return this;
        }

        @Override
        public ClientModelPersister persistToDownloads(Collection<ToDownload> toDownloads) {
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredParts filteredParts) {
            return this;
        }

        @Override
        public ClientModelPersister persistReadEpisodes(Collection<ClientReadEpisode> readMedia) {
            return this;
        }

        @Override
        public void finish() {

        }

        @Override
        public ClientModelPersister persist(ToDownload toDownload) {
            return this;
        }

        @Override
        public void persistMediaInWait(List<ClientMediumInWait> medium) {

        }

        @Override
        public ClientModelPersister persist(ClientSimpleUser user) {
            SqliteStorage.this.userTable.insertUser(new User(user.getUuid(), user.getSession(), user.getName()));
            return this;
        }
    }

}
