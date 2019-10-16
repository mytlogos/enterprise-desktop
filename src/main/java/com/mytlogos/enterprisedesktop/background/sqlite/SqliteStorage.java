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
    private final EditEventTable editEventTable;
    private final EpisodeTable episodeTable;
    private final ExternalListMediumJoinTable externalListMediumJoinTable;
    private final ExternalMediaListTable externalMediaListTable;
    private final ExternalUserTable externalUserTable;
    private final MediaListTable mediaListTable;
    private final ListMediumJoinTable listMediumJoinTable;
    private final FailedEpisodeTable failedEpisodeTable;
    private final MediumInWaitTable mediumInWaitTable;
    private final NewsTable newsTable;
    private final MediumTable mediumTable;
    private final NotificationTable notificationTable;
    private final PartTable partTable;
    private final ReleaseTable releaseTable;
    private final ToDownloadTable toDownloadTable;

    public SqliteStorage() {
        this.userTable = this.initTable(new UserTable());
        this.editEventTable = this.initTable(new EditEventTable());
        this.episodeTable = this.initTable(new EpisodeTable());
        this.externalListMediumJoinTable = this.initTable(new ExternalListMediumJoinTable());
        this.externalMediaListTable = this.initTable(new ExternalMediaListTable());
        this.externalUserTable = this.initTable(new ExternalUserTable());
        this.mediaListTable = this.initTable(new MediaListTable());
        this.listMediumJoinTable = this.initTable(new ListMediumJoinTable());
        this.failedEpisodeTable = this.initTable(new FailedEpisodeTable());
        this.mediumInWaitTable = this.initTable(new MediumInWaitTable());
        this.newsTable = this.initTable(new NewsTable());
        this.mediumTable = this.initTable(new MediumTable());
        this.notificationTable = this.initTable(new NotificationTable());
        this.partTable = this.initTable(new PartTable());
        this.releaseTable = this.initTable(new ReleaseTable());
        this.toDownloadTable = this.initTable(new ToDownloadTable());
    }

    private <T extends AbstractTable> T initTable(T table) {
        table.initialize();
        return table;
    }


    @Override
    public Flowable<User> getUser() {
        return Flowable.empty();
    }

    @Override
    public User getUserNow() {
        return null;
    }

    @Override
    public Flowable<HomeStats> getHomeStats() {
        return Flowable.empty();
    }

    @Override
    public void deleteAllUser() {

    }

    @Override
    public ClientModelPersister getPersister(Repository repository, LoadData loadedData) {
        return new SqlitePersister(loadedData, repository);
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
        return new LoadData();
    }

    @Override
    public Flowable<PagedList<News>> getNews() {
        return Flowable.empty();
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
        return Flowable.empty();
    }

    @Override
    public Collection<Integer> getExternalListItems(Integer externalListId) {
        return null;
    }

    @Override
    public Flowable<List<Integer>> getLiveExternalListItems(Integer externalListId) {
        return Flowable.empty();
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
        return Flowable.empty();
    }

    @Override
    public Flowable<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium) {
        return Flowable.empty();
    }

    @Override
    public Flowable<List<MediaList>> getLists() {
        return Flowable.empty();
    }

    @Override
    public void insertDanglingMedia(Collection<Integer> mediaIds) {

    }

    @Override
    public void removeDanglingMedia(Collection<Integer> mediaIds) {

    }

    @Override
    public Flowable<? extends MediaListSetting> getListSetting(int id, boolean isExternal) {
        return Flowable.empty();
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
        return Flowable.empty();
    }

    @Override
    public Flowable<MediumSetting> getMediumSettings(int mediumId) {
        return Flowable.empty();
    }

    @Override
    public MediumSetting getMediumSettingsNow(int mediumId) {
        return null;
    }

    @Override
    public Flowable<PagedList<TocEpisode>> getToc(int mediumId, Sortings sortings, byte read, byte saved) {
        return Flowable.empty();
    }

    @Override
    public Flowable<List<MediumItem>> getMediumItems(int listId, boolean isExternal) {
        return Flowable.empty();
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
        return Flowable.empty();
    }

    @Override
    public Flowable<PagedList<ReadEpisode>> getReadTodayEpisodes() {
        return Flowable.empty();
    }

    @Override
    public Flowable<List<MediaList>> getInternLists() {
        return Flowable.empty();
    }

    @Override
    public void addItemsToList(int listId, Collection<Integer> ids) {

    }

    @Override
    public Flowable<List<MediumInWait>> getSimilarMediaInWait(MediumInWait mediumInWait) {
        return Flowable.empty();
    }

    @Override
    public Flowable<List<SimpleMedium>> getMediaSuggestions(String title, int medium) {
        return Flowable.empty();
    }

    @Override
    public Flowable<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium) {
        return Flowable.empty();
    }

    @Override
    public Flowable<List<MediaList>> getListSuggestion(String name) {
        return Flowable.empty();
    }

    @Override
    public Flowable<Boolean> onDownloadAble() {
        return Flowable.empty();
    }

    @Override
    public void clearMediaInWait() {

    }

    @Override
    public void deleteMediaInWait(Collection<MediumInWait> toDelete) {

    }

    @Override
    public Flowable<List<MediumItem>> getAllDanglingMedia() {
        return Flowable.empty();
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
    public Flowable<PagedList<DisplayExternalUser>> getExternalUser() {
        return Flowable.empty();
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
        return Flowable.empty();
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
            for (LoadWorkGenerator.IntDependency<List<ListMediumJoin>> dependency : mediaLists.mediumDependencies) {
                int tmpListId = 0;
                if (!dependency.dependency.isEmpty()) {
                    tmpListId = dependency.dependency.get(0).listId;
                }
                int listId = tmpListId;

                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                () -> SqliteStorage.this.listMediumJoinTable.delete(listId)
                        ),
                        worker.MEDIUM_LOADER
                ));
            }
            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generateExternalMediaListsDependant(LoadWorkGenerator.FilteredExtMediaList externalMediaLists) {
            Set<DependencyTask<?>> tasks = new HashSet<>();

            LoadWorker worker = LoadWorker.getWorker();

            for (LoadWorkGenerator.IntDependency<List<ListMediumJoin>> dependency : externalMediaLists.mediumDependencies) {
                int tmpListId = 0;
                if (!dependency.dependency.isEmpty()) {
                    tmpListId = dependency.dependency.get(0).listId;
                }
                int listId = tmpListId;

                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                () -> SqliteStorage.this.externalListMediumJoinTable.delete(listId)
                        ),
                        worker.MEDIUM_LOADER
                ));
            }
            for (LoadWorkGenerator.Dependency<String, ClientExternalMediaList> dependency : externalMediaLists.userDependencies) {
                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.EXTERNAL_MEDIALIST_LOADER
                        ),
                        worker.EXTERNAL_USER_LOADER
                ));
            }
            return tasks;
        }

        @Override
        public Collection<DependencyTask<?>> generateExternalUsersDependant(LoadWorkGenerator.FilteredExternalUser externalUsers) {
            Set<DependencyTask<?>> tasks = new HashSet<>();

            LoadWorker worker = LoadWorker.getWorker();

            for (LoadWorkGenerator.IntDependency<List<ListMediumJoin>> dependency : externalUsers.mediumDependencies) {
                int tmpListId = 0;
                if (!dependency.dependency.isEmpty()) {
                    tmpListId = dependency.dependency.get(0).listId;
                }
                int listId = tmpListId;

                tasks.add(new DependencyTask<>(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                () -> SqliteStorage.this.externalListMediumJoinTable.delete(listId)
                        ),
                        worker.MEDIUM_LOADER
                ));
            }
            return tasks;
        }
    }


    private class SqlitePersister implements ClientModelPersister {
        private final Collection<ClientConsumer<?>> consumer = new ArrayList<>();
        private final LoadData loadedData;
        private final Repository repository;
        private final LoadWorkGenerator generator;


        SqlitePersister(LoadData loadedData, Repository repository) {
            this.loadedData = loadedData;
            this.repository = repository;
            this.generator = new LoadWorkGenerator(loadedData);
            this.initConsumer();
        }

        private void initConsumer() {
            consumer.add(new ClientConsumer<ClientReadEpisode>() {
                @Override
                public Class<ClientReadEpisode> getType() {
                    return ClientReadEpisode.class;
                }

                @Override
                public void consume(Collection<ClientReadEpisode> clientEpisodes) {
                    SqlitePersister.this.persistReadEpisodes(clientEpisodes);
                }
            });
            consumer.add(new ClientConsumer<ClientEpisode>() {
                @Override
                public Class<ClientEpisode> getType() {
                    return ClientEpisode.class;
                }

                @Override
                public void consume(Collection<ClientEpisode> clientEpisodes) {
                    SqlitePersister.this.persistEpisodes(clientEpisodes);
                }
            });
            consumer.add(new ClientConsumer<ClientPart>() {
                @Override
                public Class<ClientPart> getType() {
                    return ClientPart.class;
                }

                @Override
                public void consume(Collection<ClientPart> parts) {
                    SqlitePersister.this.persistParts(parts);
                }
            });
            consumer.add(new ClientConsumer<ClientMedium>() {
                @Override
                public Class<ClientMedium> getType() {
                    return ClientMedium.class;
                }

                @Override
                public void consume(Collection<ClientMedium> media) {
                    SqlitePersister.this.persistMedia(media);
                }
            });
            consumer.add(new ClientConsumer<ListMediumJoin>() {
                @Override
                public Class<ListMediumJoin> getType() {
                    return ListMediumJoin.class;
                }

                @Override
                public void consume(Collection<ListMediumJoin> joins) {
                    Set<ListMediumJoin> internalJoins = new HashSet<>();
                    Set<ListMediumJoin> externalJoins = new HashSet<>();

                    for (ListMediumJoin join : joins) {
                        if (join.isExternal()) {
                            externalJoins.add(join);
                        } else {
                            internalJoins.add(join);
                        }
                    }
                    SqliteStorage.this.listMediumJoinTable.insert(internalJoins);
                    SqliteStorage.this.externalListMediumJoinTable.insert(externalJoins);
                }
            });
            consumer.add(new ClientConsumer<ClientExternalMediaList>() {
                @Override
                public Class<ClientExternalMediaList> getType() {
                    return ClientExternalMediaList.class;
                }

                @Override
                public void consume(Collection<ClientExternalMediaList> extLists) {
                    SqlitePersister.this.persistExternalMediaLists(extLists);
                }
            });
            consumer.add(new ClientConsumer<ClientMediaList>() {
                @Override
                public Class<ClientMediaList> getType() {
                    return ClientMediaList.class;
                }

                @Override
                public void consume(Collection<ClientMediaList> lists) {
                    SqlitePersister.this.persistMediaLists(lists);
                }
            });
            consumer.add(new ClientConsumer<ClientExternalUser>() {
                @Override
                public Class<ClientExternalUser> getType() {
                    return ClientExternalUser.class;
                }

                @Override
                public void consume(Collection<ClientExternalUser> extUsers) {
                    SqlitePersister.this.persistExternalUsers(extUsers);
                }
            });
        }

        private ClientModelPersister persistFiltered(LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList) {
            List<ClientExternalMediaList> list = filteredExtMediaList.newList;
            List<ClientExternalMediaList> update = filteredExtMediaList.updateList;

            List<ListMediumJoin> joins = filteredExtMediaList.joins;
            List<Integer> clearListMediumJoin = filteredExtMediaList.clearJoins;

            SqliteStorage.this.externalMediaListTable.insert(list);
            // TODO 16.10.2019: 
//            SqliteStorage.this.externalMediaListTable.updateBulk(update);
            // first clear all possible out-of-date joins
            SqliteStorage.this.externalListMediumJoinTable.delete(clearListMediumJoin);
            // then add all up-to-date joins
            SqliteStorage.this.externalListMediumJoinTable.insert(joins);

            for (ClientExternalMediaList mediaList : list) {
                this.loadedData.getExternalMediaList().add(mediaList.getId());
            }
            return this;
        }

        private ClientModelPersister persistFiltered(LoadWorkGenerator.FilteredMediaList filteredMediaList) {
            List<ClientMediaList> list = filteredMediaList.newList;
            List<ClientMediaList> update = filteredMediaList.updateList;
            List<ListMediumJoin> joins = filteredMediaList.joins;
            List<Integer> clearListMediumJoin = filteredMediaList.clearJoins;

            SqliteStorage.this.mediaListTable.insert(list);
            // TODO 16.10.2019:
//            SqliteStorage.this.mediaListTable.updateBulk(update);
            // first clear all possible out-of-date joins
            SqliteStorage.this.listMediumJoinTable.delete(clearListMediumJoin);
            // then add all up-to-date joins
            SqliteStorage.this.listMediumJoinTable.insert(joins);

            for (ClientMediaList mediaList : list) {
                this.loadedData.getMediaList().add(mediaList.getId());
            }
            return this;
        }

        private ClientModelPersister persistFiltered(LoadWorkGenerator.FilteredExternalUser filteredExternalUser) {
            List<ClientExternalUser> list = filteredExternalUser.newUser;
            List<ClientExternalUser> update = filteredExternalUser.updateUser;

            List<ClientExternalMediaList> externalMediaLists = filteredExternalUser.newList;
            List<ClientExternalMediaList> updateExternalMediaLists = filteredExternalUser.updateList;

            List<ListMediumJoin> extListMediaJoin = filteredExternalUser.joins;
            List<Integer> clearListMediumJoin = filteredExternalUser.clearJoins;

            SqliteStorage.this.externalUserTable.insert(list);
            // TODO 16.10.2019:
//            SqliteStorage.this.externalUserTable.updateBulk(update);
            SqliteStorage.this.externalMediaListTable.insert(externalMediaLists);
            // TODO 16.10.2019:
//            SqliteStorage.this.externalMediaListTable.updateBulk(updateExternalMediaLists);
            // first clear all possible out-of-date joins
            SqliteStorage.this.externalListMediumJoinTable.delete(clearListMediumJoin);
            // then add all up-to-date joins
            SqliteStorage.this.externalListMediumJoinTable.insert(extListMediaJoin);

            for (ClientExternalUser user : list) {
                this.loadedData.getExternalUser().add(user.getUuid());
            }

            for (ClientExternalMediaList mediaList : externalMediaLists) {
                this.loadedData.getExternalMediaList().add(mediaList.getListId());
            }
            return this;
        }

        @Override
        public Collection<ClientConsumer<?>> getConsumer() {
            return consumer;
        }

        @Override
        public ClientModelPersister persistEpisodes(Collection<ClientEpisode> episodes) {
            LoadWorkGenerator.FilteredEpisodes filteredEpisodes = this.generator.filterEpisodes(episodes);

            LoadWorker worker = this.repository.getLoadWorker();

            for (LoadWorkGenerator.IntDependency<ClientEpisode> dependency : filteredEpisodes.partDependencies) {
                worker.addIntegerIdTask(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.EPISODE_LOADER
                        ),
                        worker.PART_LOADER
                );
            }

            return persist(filteredEpisodes);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredEpisodes filteredEpisodes) {
            List<ClientEpisode> newEpisodes = filteredEpisodes.newEpisodes;
            List<ClientEpisode> update = filteredEpisodes.updateEpisodes;

            List<ClientRelease> releases = filteredEpisodes.releases;

            SqliteStorage.this.episodeTable.insert(newEpisodes);
            // TODO 16.10.2019:
//            SqliteStorage.this.episodeTable.update(update);
            SqliteStorage.this.releaseTable.insert(releases);

            for (ClientEpisode episode : newEpisodes) {
                this.loadedData.getEpisodes().add(episode.getEpisodeId());
            }
            return this;
        }

        @Override
        public ClientModelPersister persistMediaLists(Collection<ClientMediaList> mediaLists) {
            LoadWorkGenerator.FilteredMediaList filteredMediaList = this.generator.filterMediaLists(mediaLists);

            LoadWorker worker = this.repository.getLoadWorker();

            for (LoadWorkGenerator.IntDependency<List<ListMediumJoin>> dependency : filteredMediaList.mediumDependencies) {
                int tmpListId = 0;
                if (!dependency.dependency.isEmpty()) {
                    tmpListId = dependency.dependency.get(0).listId;
                }
                int listId = tmpListId;
                worker.addIntegerIdTask(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                () -> SqliteStorage.this.listMediumJoinTable.delete(listId)
                        ),
                        worker.MEDIUM_LOADER
                );
            }

            return this.persist(filteredMediaList);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredMediaList filteredMediaList) {
            return this.persistFiltered(filteredMediaList);
        }

        @Override
        public ClientModelPersister persistExternalMediaLists(Collection<ClientExternalMediaList> externalMediaLists) {
            LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList = this.generator.filterExternalMediaLists(externalMediaLists);

            LoadWorker worker = this.repository.getLoadWorker();

            for (LoadWorkGenerator.Dependency<String, ClientExternalMediaList> dependency : filteredExtMediaList.userDependencies) {
                worker.addStringIdTask(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.EXTERNAL_MEDIALIST_LOADER
                        ),
                        worker.EXTERNAL_USER_LOADER
                );
            }
            for (LoadWorkGenerator.IntDependency<List<ListMediumJoin>> dependency : filteredExtMediaList.mediumDependencies) {
                int tmpListId = 0;
                if (!dependency.dependency.isEmpty()) {
                    tmpListId = dependency.dependency.get(0).listId;
                }
                int listId = tmpListId;
                worker.addIntegerIdTask(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                () -> SqliteStorage.this.externalListMediumJoinTable.delete(listId)
                        ),
                        worker.MEDIUM_LOADER
                );
            }

            return this.persistFiltered(filteredExtMediaList);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList) {
            return this.persistFiltered(filteredExtMediaList);
        }

        @Override
        public ClientModelPersister persistExternalUsers(Collection<ClientExternalUser> externalUsers) {
            LoadWorkGenerator.FilteredExternalUser filteredExternalUser = this.generator.filterExternalUsers(externalUsers);

            LoadWorker worker = this.repository.getLoadWorker();

            for (LoadWorkGenerator.IntDependency<List<ListMediumJoin>> dependency : filteredExternalUser.mediumDependencies) {
                int tmpListId = 0;
                if (!dependency.dependency.isEmpty()) {
                    tmpListId = dependency.dependency.get(0).listId;
                }
                int listId = tmpListId;
                worker.addIntegerIdTask(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                () -> SqliteStorage.this.externalListMediumJoinTable.delete(listId)
                        ),
                        worker.MEDIUM_LOADER
                );
            }

            return this.persist(filteredExternalUser);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredExternalUser filteredExternalUser) {
            return this.persistFiltered(filteredExternalUser);
        }

        @Override
        public ClientModelPersister persistMedia(Collection<ClientMedium> media) {
            LoadWorkGenerator.FilteredMedia filteredMedia = this.generator.filterMedia(media);
            LoadWorker worker = this.repository.getLoadWorker();

            for (LoadWorkGenerator.IntDependency<ClientMedium> dependency : filteredMedia.episodeDependencies) {
                worker.addIntegerIdTask(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.MEDIUM_LOADER
                        ),
                        worker.EPISODE_LOADER
                );
            }
            for (Integer part : filteredMedia.unloadedParts) {
                worker.addIntegerIdTask(part, null, worker.PART_LOADER);
            }
            return persist(filteredMedia);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredMedia filteredMedia) {
            List<ClientMedium> list = filteredMedia.newMedia;
            List<ClientMedium> update = filteredMedia.updateMedia;

            SqliteStorage.this.mediumTable.insert(list);
            // TODO 16.10.2019:
//            SqliteStorage.this.mediumTable.updateBulk(update);

            for (ClientMedium medium : list) {
                this.loadedData.getMedia().add(medium.getMediumId());
            }
            return this;
        }

        @Override
        public ClientModelPersister persistNews(Collection<ClientNews> news) {
            List<ClientNews> list = new ArrayList<>();
            List<ClientNews> update = new ArrayList<>();

            for (ClientNews clientNews : news) {
                if (this.generator.isNewsLoaded(clientNews.getId())) {
                    update.add(clientNews);
                } else {
                    list.add(clientNews);
                }
            }
            SqliteStorage.this.newsTable.insert(list);
            // TODO 16.10.2019:
//            SqliteStorage.this.newsTable.updateNews(update);

            for (ClientNews clientNews : list) {
                this.loadedData.getNews().add(clientNews.getId());
            }
            System.out.println("from " + news.size() + " persisted: " + list);
            return this;
        }

        @Override
        public ClientModelPersister persistParts(Collection<ClientPart> parts) {
            LoadWorkGenerator.FilteredParts filteredParts = this.generator.filterParts(parts);
            LoadWorker worker = this.repository.getLoadWorker();

            for (LoadWorkGenerator.IntDependency<ClientPart> dependency : filteredParts.mediumDependencies) {
                worker.addIntegerIdTask(
                        dependency.id,
                        new DependantValue(
                                dependency.dependency,
                                dependency.dependency.getId(),
                                worker.PART_LOADER
                        ),
                        worker.MEDIUM_LOADER
                );
            }
            return persist(filteredParts);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredReadEpisodes filteredReadEpisodes) {
            for (ClientReadEpisode readEpisode : filteredReadEpisodes.episodeList) {
                SqliteStorage.this.episodeTable.updateProgress(
                        readEpisode.getEpisodeId(),
                        readEpisode.getProgress(),
                        readEpisode.getReadDate()
                );
            }
            return this;
        }

        @Override
        public ClientModelPersister persist(ClientListQuery query) {
            this.persist(query.getMedia());
            this.persist(query.getList());
            return this;
        }

        @Override
        public ClientModelPersister persist(ClientMultiListQuery query) {
            this.persist(query.getMedia());
            this.persist(query.getList());
            return this;
        }

        @Override
        public ClientModelPersister persist(ClientUser clientUser) {
            this.persistUser(clientUser);

            if (clientUser == null) {
                return this;
            }

            LoadWorker worker = this.repository.getLoadWorker();

            for (int clientReadChapter : clientUser.getUnreadChapter()) {
                if (!this.generator.isEpisodeLoaded(clientReadChapter)) {
                    worker.addIntegerIdTask(clientReadChapter, null, worker.EPISODE_LOADER);
                }
            }

            for (ClientNews clientNews : clientUser.getUnreadNews()) {
                int id = clientNews.getId();

                if (!this.generator.isNewsLoaded(id)) {
                    worker.addIntegerIdTask(id, null, worker.NEWS_LOADER);
                }
            }

            for (ClientReadEpisode clientReadEpisode : clientUser.getReadToday()) {
                int episodeId = clientReadEpisode.getEpisodeId();

                if (!this.generator.isEpisodeLoaded(episodeId)) {
                    worker.addIntegerIdTask(episodeId, null, worker.EPISODE_LOADER);
                }
            }

            // persistFiltered lists
            this.persist(clientUser.getLists());
            // persistFiltered externalUser
            this.persist(clientUser.getExternalUser());
            // persistFiltered loaded unread NewsImpl
            this.persist(clientUser.getUnreadNews());
            // persistFiltered/update media with data
            this.persist(clientUser.getReadToday());

            return this;
        }

        @Override
        public ClientModelPersister persist(ClientUpdateUser user) {
            User value = SqliteStorage.this.userTable.getUserNow();
            if (value == null) {
                throw new IllegalArgumentException("cannot update user if none is stored in the database");
            }
            if (!user.getUuid().equals(value.getUuid())) {
                throw new IllegalArgumentException("cannot update user which do not share the same uuid");
            }
            // at the moment the only thing that can change for the user on client side is the name
            if (user.getName().equals(value.getName())) {
                return this;
            }
            SqliteStorage.this.userTable.update(new UserImpl(user.getName(), value.getUuid(), value.getSession()));
            return this;
        }

        @Override
        public ClientModelPersister persistToDownloads(Collection<ToDownload> toDownloads) {
            SqliteStorage.this.toDownloadTable.insert(toDownloads);
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredParts filteredParts) {
            List<ClientEpisode> episodes = filteredParts.episodes;

            List<ClientPart> list = filteredParts.newParts;
            List<ClientPart> update = filteredParts.updateParts;

            SqliteStorage.this.partTable.insert(list);
            // TODO 16.10.2019:
//            SqliteStorage.this.partTable.updateBulk(update);

            for (ClientPart part : list) {
                this.loadedData.getPart().add(part.getId());
            }
            this.persistEpisodes(episodes);
            return this;
        }

        @Override
        public ClientModelPersister persistReadEpisodes(Collection<ClientReadEpisode> readEpisodes) {
            LoadWorkGenerator.FilteredReadEpisodes filteredReadEpisodes = this.generator.filterReadEpisodes(readEpisodes);
            LoadWorker worker = this.repository.getLoadWorker();

            for (LoadWorkGenerator.IntDependency dependency : filteredReadEpisodes.dependencies) {
                worker.addIntegerIdTask(
                        dependency.id,
                        new DependantValue(dependency.dependency),
                        worker.EPISODE_LOADER
                );
            }
            return this.persist(filteredReadEpisodes);
        }

        @Override
        public void finish() {
            this.repository.getLoadWorker().work();
        }

        @Override
        public ClientModelPersister persist(ToDownload toDownload) {
            SqliteStorage.this.toDownloadTable.insert(toDownload);
            return this;
        }

        @Override
        public void persistMediaInWait(List<ClientMediumInWait> medium) {
            SqliteStorage.this.mediumInWaitTable.insert(medium);
        }

        @Override
        public ClientModelPersister persist(ClientSimpleUser user) {
            this.persistUser(user);
            return this;
        }

        private void persistUser(User user) {
            // short cut version
            if (user == null) {
                SqliteStorage.this.deleteAllUser();
                return;
            }
            User currentUser = SqliteStorage.this.userTable.getUserNow();

            if (currentUser != null && user.getUuid().equals(currentUser.getUuid())) {
                // update user, so previous one wont be deleted
                SqliteStorage.this.userTable.update(user);
            } else {
                SqliteStorage.this.userTable.deleteAllUser();
                // persistFiltered user
                SqliteStorage.this.userTable.insert(user);
            }

        }
    }

}
