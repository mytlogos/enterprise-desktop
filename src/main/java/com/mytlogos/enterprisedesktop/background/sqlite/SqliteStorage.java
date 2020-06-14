package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.*;
import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorkGenerator;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.profile.DisplayEpisodeProfile;
import com.mytlogos.enterprisedesktop.tools.Sorting;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
public class SqliteStorage implements DatabaseStorage {
    final UserTable userTable;
    final EditEventTable editEventTable;
    final EpisodeTable episodeTable;
    final ExternalListMediumJoinTable externalListMediumJoinTable;
    final ExternalMediaListTable externalMediaListTable;
    final ExternalUserTable externalUserTable;
    final MediaListTable mediaListTable;
    final ListMediumJoinTable listMediumJoinTable;
    final FailedEpisodeTable failedEpisodeTable;
    final MediumInWaitTable mediumInWaitTable;
    final NewsTable newsTable;
    final MediumTable mediumTable;
    final TocTable tocTable;
    final NotificationTable notificationTable;
    final PartTable partTable;
    final ReleaseTable releaseTable;
    final ToDownloadTable toDownloadTable;

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
        this.tocTable = this.initTable(new TocTable());
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
    public LiveData<User> getUser() {
        return this.userTable.getUser();
    }

    @Override
    public User getUserNow() {
        return this.userTable.getUserNow();
    }

    @Override
    public LiveData<HomeStats> getHomeStats() {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAllUser() {
        throw new NotImplementedException();
    }

    @Override
    public ClientModelPersister getPersister(Repository repository, LoadData loadedData) {
        return new SqlitePersister(loadedData);
    }

    @Override
    public void deleteOldNews() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isLoading() {
        throw new NotImplementedException();
    }

    @Override
    public void setLoading(boolean loading) {
        throw new NotImplementedException();
    }

    @Override
    public LoadData getLoadData() {
        LoadData data = new LoadData();
        data.getEpisodes().addAll(this.episodeTable.getLoadedInt());
        data.getPart().addAll(this.partTable.getLoadedInt());
        data.getNews().addAll(this.newsTable.getLoadedInt());
        data.getMedia().addAll(this.mediumTable.getLoadedInt());
        data.getExternalMediaList().addAll(this.externalMediaListTable.getLoadedInt());
        data.getExternalUser().addAll(this.externalUserTable.getLoadedString());
        data.getMediaList().addAll(this.mediaListTable.getLoadedInt());
        return data;
    }

    @Override
    public LiveData<PagedList<News>> getNews() {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getSavedEpisodes() {
        return this.episodeTable.getSavedEpisodes();
    }

    @Override
    public List<Integer> getToDeleteEpisodes() {
        throw new NotImplementedException();
    }

    @Override
    public void updateSaved(int episodeId, boolean saved) {
        throw new NotImplementedException();
    }

    @Override
    public void updateSaved(Collection<Integer> episodeIds, boolean saved) {
        this.episodeTable.updateSaved(episodeIds, saved);
    }

    @Override
    public List<ToDownload> getAllToDownloads() {
        return this.toDownloadTable.getItems();
    }

    @Override
    public void removeToDownloads(Collection<ToDownload> toDownloads) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<Integer> getListItems(Integer listId) {
        return this.listMediumJoinTable.getMediumItemsIds(listId);
    }

    @Override
    public LiveData<List<Integer>> getLiveListItems(Integer listId) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<Integer> getExternalListItems(Integer externalListId) {
        return this.externalListMediumJoinTable.getMediumItemsIds(externalListId);
    }

    @Override
    public LiveData<List<Integer>> getLiveExternalListItems(Integer externalListId) {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getDownloadableEpisodes(Integer mediumId, int limit) {
        return this.episodeTable.getDownloadable(mediumId, limit);
    }

    @Override
    public List<Integer> getDownloadableEpisodes(Collection<Integer> mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<PagedList<DisplayRelease>> getDisplayEpisodes(DisplayEpisodeProfile filter) {
        return this.releaseTable.getReleases(filter);
    }

    @Override
    public LiveData<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<List<MediaList>> getLists() {
        final LiveData<List<ExternalMediaList>> externalMediaLists = this.externalMediaListTable.getLists();
        final LiveData<List<MediaList>> mediaLists = this.mediaListTable.getLists();

        return mediaLists.map(externalMediaLists, (mediaLists1, externalMediaLists1) -> {
            if (mediaLists1 == null || externalMediaLists1 == null) {
                return null;
            }
            List<MediaList> lists = new ArrayList<>(mediaLists1);
            lists.addAll(externalMediaLists1);
            return lists;
        });
    }

    @Override
    public void insertDanglingMedia(Collection<Integer> mediaIds) {
        throw new NotImplementedException();
    }

    @Override
    public void removeDanglingMedia(Collection<Integer> mediaIds) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<? extends MediaListSetting> getListSetting(int id, boolean isExternal) {
        throw new NotImplementedException();
    }

    @Override
    public MediaListSetting getListSettingNow(int id, boolean isExternal) {
        throw new NotImplementedException();
    }

    @Override
    public void updateToDownload(boolean add, ToDownload toDownload) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<PagedList<MediumItem>> getAllMedia(Sorting sortings, String title, int medium, String author, LocalDateTime lastUpdate, int minCountEpisodes, int minCountReadEpisodes) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<List<MediumItem>> getAllMedia(Sorting sortings) {
        return this.mediumTable.getAllMedia(sortings);
    }

    @Override
    public LiveData<MediumSetting> getMediumSettings(int mediumId) {
        return this.mediumTable.getSettings(mediumId);
    }

    @Override
    public MediumSetting getMediumSettingsNow(int mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved) {
        return this.episodeTable.getToc(mediumId, sortings, read, saved);
    }

    @Override
    public LiveData<List<MediumItem>> getMediumItems(int listId, boolean isExternal) {
        return isExternal ? this.externalMediaListTable.getMediumItems(listId) : this.mediaListTable.getMediumItems(listId);
    }

    @Override
    public LiveData<List<SimpleMedium>> getSimpleMediumItems(int listId, boolean external) {
        return external ? this.externalMediaListTable.getSimpleMediumItems(listId) : this.mediaListTable.getSimpleMediumItems(listId);
    }

    @Override
    public boolean listExists(String listName) {
        throw new NotImplementedException();
    }

    @Override
    public int countSavedEpisodes(Integer mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getSavedEpisodes(int mediumId) {
        return this.episodeTable.getSavedEpisodes();
    }

    @Override
    public Episode getEpisode(int episodeId) {
        throw new NotImplementedException();
    }

    @Override
    public List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> ids) {
        return this.episodeTable.getSimpleEpisodes(ids);
    }

    @Override
    public void updateProgress(Collection<Integer> episodeIds, float progress) {
        this.episodeTable.updateProgress(episodeIds, progress, LocalDateTime.now());
    }

    @Override
    public LiveData<PagedList<MediumInWait>> getMediaInWaitBy(String filter, int mediumFilter, String hostFilter, Sorting sortings) {
        return this.mediumInWaitTable.get(filter, mediumFilter, hostFilter, sortings);
    }

    @Override
    public LiveData<PagedList<ReadEpisode>> getReadTodayEpisodes() {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<List<MediaList>> getInternLists() {
        return this.mediaListTable.getLists();
    }

    @Override
    public void addItemsToList(int listId, Collection<Integer> ids) {
        List<ListMediumJoin> joins = new ArrayList<>(ids.size());
        for (Integer id : ids) {
            joins.add(new ListMediumJoin(listId, id, false));
        }
        this.listMediumJoinTable.insert(joins);
    }

    @Override
    public List<MediumInWait> getSimilarMediaInWait(MediumInWait mediumInWait) {
        return this.mediumInWaitTable.getSimilar(mediumInWait);
    }

    @Override
    public LiveData<List<SimpleMedium>> getMediaSuggestions(String title, int medium) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<List<MediaList>> getListSuggestion(String name) {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<Boolean> onDownloadAble() {
        throw new NotImplementedException();
    }

    @Override
    public void clearMediaInWait() {
        throw new NotImplementedException();
    }

    @Override
    public void deleteMediaInWait(Collection<MediumInWait> toDelete) {
        this.mediumInWaitTable.delete(toDelete);
    }

    @Override
    public LiveData<List<MediumItem>> getAllDanglingMedia() {
        throw new NotImplementedException();
    }

    @Override
    public void removeItemFromList(int listId, int mediumId) {
        this.listMediumJoinTable.removeJoin(Collections.singletonList(new ListMediumJoin(listId, mediumId, false)));
    }

    @Override
    public void removeItemFromList(int listId, Collection<Integer> mediumIds) {
        List<ListMediumJoin> removeJoins = new ArrayList<>(mediumIds.size());

        for (Integer id : mediumIds) {
            removeJoins.add(new ListMediumJoin(listId, id, false));
        }
        this.listMediumJoinTable.removeJoin(removeJoins);
    }

    @Override
    public void moveItemsToList(int oldListId, int listId, Collection<Integer> ids) {
        List<ListMediumJoin> removeJoins = new ArrayList<>(ids.size());
        List<ListMediumJoin> insertJoins = new ArrayList<>(ids.size());

        for (Integer id : ids) {
            removeJoins.add(new ListMediumJoin(oldListId, id, false));
            insertJoins.add(new ListMediumJoin(listId, id, false));
        }
        this.listMediumJoinTable.removeJoin(removeJoins);
        this.listMediumJoinTable.insert(insertJoins);
    }

    @Override
    public LiveData<PagedList<DisplayExternalUser>> getExternalUser() {
        throw new NotImplementedException();
    }

    @Override
    public SpaceMedium getSpaceMedium(int mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public int getMediumType(Integer mediumId) {
        return this.mediumTable.getSimpleMedium(mediumId).getMedium();
    }

    @Override
    public List<String> getReleaseLinks(int episodeId) {
        return this.releaseTable.getLinks(episodeId);
    }

    @Override
    public void clearLocalMediaData() {
        throw new NotImplementedException();
    }

    @Override
    public LiveData<PagedList<NotificationItem>> getNotifications() {
        throw new NotImplementedException();
    }

    @Override
    public void updateFailedDownload(int episodeId) {
        this.failedEpisodeTable.updateFailedDownload(episodeId);
    }

    @Override
    public List<FailedEpisode> getFailedEpisodes(Collection<Integer> episodeIds) {
        return this.failedEpisodeTable.getFailedEpisodes(episodeIds);
    }

    @Override
    public void addNotification(NotificationItem notification) {
        this.notificationTable.insert(notification);
    }

    @Override
    public SimpleEpisode getSimpleEpisode(int episodeId) {
        return this.episodeTable.getSimpleEpisode(episodeId);
    }

    @Override
    public SimpleMedium getSimpleMedium(int mediumId) {
        return this.mediumTable.getSimpleMedium(mediumId);
    }

    @Override
    public void clearNotifications() {
        throw new NotImplementedException();
    }

    @Override
    public void clearFailEpisodes() {
        throw new NotImplementedException();
    }

    @Override
    public Collection<Integer> getAllEpisodes(int mediumId) {
        return this.episodeTable.getMediumEpisodeIds(mediumId);
    }

    @Override
    public void syncProgress() {
        throw new NotImplementedException();
    }

    @Override
    public void updateDataStructure(List<Integer> mediaIds, List<Integer> partIds) {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getEpisodeIdsWithHigherIndex(double combiIndex, int mediumId, boolean read) {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getEpisodeIdsWithHigherIndex(double combiIndex, int mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getEpisodeIdsWithLowerIndex(double combiIndex, int mediumId, boolean read) {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getEpisodeIdsWithLowerIndex(double combiIndex, int mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<Integer> getSavedEpisodeIdsWithHigherIndex(double combiIndex, int mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<Integer> getSavedEpisodeIdsWithLowerIndex(double combiIndex, int mediumId) {
        throw new NotImplementedException();
    }

    @Override
    public void removeEpisodes(List<Integer> episodeIds) {
        throw new NotImplementedException();
    }

    @Override
    public void removeParts(Collection<Integer> partIds) {
        throw new NotImplementedException();
    }

    @Override
    public List<Integer> getReadEpisodes(Collection<Integer> episodeIds, boolean read) {
        throw new NotImplementedException();
    }

    @Override
    public void insertEditEvent(EditEvent event) {
        throw new NotImplementedException();
    }

    @Override
    public void insertEditEvent(Collection<EditEvent> events) {
        throw new NotImplementedException();
    }

    @Override
    public List<? extends EditEvent> getEditEvents() {
        throw new NotImplementedException();
    }

    @Override
    public void removeEditEvents(Collection<EditEvent> editEvents) {
        throw new NotImplementedException();
    }

    @Override
    public ReloadStat checkReload(ClientStat.ParsedStat parsedStat) {
        List<PartStat> roomStats = this.episodeTable.getStat();

        Map<Integer, ClientStat.Partstat> partStats = new HashMap<>();

        for (Map<Integer, ClientStat.Partstat> value : parsedStat.media.values()) {
            partStats.putAll(value);
        }

        List<Integer> loadEpisode = new LinkedList<>();
        List<Integer> loadRelease = new LinkedList<>();
        List<Integer> loadMediumTocs = new LinkedList<>();

        for (PartStat roomStat : roomStats) {
            ClientStat.Partstat partstat = partStats.get(roomStat.partId);

            if (partstat == null) {
                throw new IllegalStateException(String.format(
                        "Local Part %s does not exist on Server, missing local Part Deletion",
                        roomStat.partId
                ));
            }

            if (partstat.episodeCount != roomStat.episodeCount
                    || partstat.episodeSum != roomStat.episodeSum) {
                loadEpisode.add(roomStat.partId);
            } else if (partstat.releaseCount != roomStat.releaseCount) {
                loadRelease.add(roomStat.partId);
            }
        }
        final Map<Integer, Integer> tocStats = this.tocTable.getStat();

        for (Map.Entry<Integer, ClientMediaStat> entry : parsedStat.mediaStats.entrySet()) {
            final Integer mediumId = entry.getKey();
            final ClientMediaStat stats = entry.getValue();
            final Integer previousTocCount = tocStats.get(mediumId);

            if (previousTocCount == null || stats.tocs != previousTocCount) {
                loadMediumTocs.add(mediumId);
            }
        }
        return new ReloadStat(loadEpisode, loadRelease, loadMediumTocs);
    }

    @Override
    public void deleteMedia(Collection<Integer> toDelete) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteParts(Collection<Integer> toDelete) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteEpisodes(Collection<Integer> toDelete) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteReleases(Collection<Release> toDelete) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteList(Collection<Integer> toDelete) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteExternalList(Collection<Integer> toDelete) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteExternalUser(Collection<Integer> toDelete) {
        throw new NotImplementedException();
    }

    @Override
    public void clearAll() {
        this.listMediumJoinTable.deleteAll();
        this.notificationTable.deleteAll();
        this.editEventTable.deleteAll();
        this.externalListMediumJoinTable.deleteAll();
        this.toDownloadTable.deleteAll();
        this.releaseTable.deleteAll();
        this.failedEpisodeTable.deleteAll();
        this.episodeTable.deleteAll();
        this.partTable.deleteAll();
        this.mediumTable.deleteAll();
        this.mediaListTable.deleteAll();
        this.externalMediaListTable.deleteAll();
        this.externalUserTable.deleteAll();
        this.mediumInWaitTable.deleteAll();
        this.newsTable.deleteAll();
    }

    @Override
    public LiveData<List<SimpleMedium>> getSimpleMedium() {
        return this.mediumTable.getSimpleMedium();
    }

    @Override
    public LiveData<List<Integer>> getListItems(Collection<Integer> listIds) {
        return this.mediaListTable.getMediumItemsIds(listIds);
    }

    private class SqlitePersister implements ClientModelPersister {
        private final LoadData loadedData;
        private final LoadWorkGenerator generator;


        SqlitePersister(LoadData loadedData) {
            this.loadedData = loadedData;
            this.generator = new LoadWorkGenerator(loadedData);
        }

        @Override
        public ClientModelPersister persistEpisodes(Collection<ClientEpisode> episodes) {
            LoadWorkGenerator.FilteredEpisodes filteredEpisodes = this.generator.filterEpisodes(episodes);
            return persist(filteredEpisodes);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredEpisodes filteredEpisodes) {
            List<ClientEpisode> newEpisodes = filteredEpisodes.newEpisodes;
            List<ClientEpisode> update = filteredEpisodes.updateEpisodes;

            SqliteStorage.this.episodeTable.insert(newEpisodes);
            SqliteStorage.this.episodeTable.update(update);

            for (ClientEpisode episode : newEpisodes) {
                this.loadedData.getEpisodes().add(episode.getEpisodeId());
            }
            SqliteStorage.this.releaseTable.insert(filteredEpisodes.releases);
            return this;
        }

        @Override
        public ClientModelPersister persistMediaLists(Collection<ClientMediaList> mediaLists) {
            LoadWorkGenerator.FilteredMediaList filteredMediaList = this.generator.filterMediaLists(mediaLists);
            return this.persist(filteredMediaList);
        }

        private ClientModelPersister persistFiltered(LoadWorkGenerator.FilteredMediaList filteredMediaList) {
            List<ClientMediaList> list = filteredMediaList.newList;
            List<ClientMediaList> update = filteredMediaList.updateList;

            SqliteStorage.this.mediaListTable.insert(list);
            SqliteStorage.this.mediaListTable.update(update);

            for (ClientMediaList mediaList : list) {
                this.loadedData.getMediaList().add(mediaList.getId());
            }
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredMediaList filteredMediaList) {
            return this.persistFiltered(filteredMediaList);
        }

        @Override
        public ClientModelPersister persistExternalMediaLists(Collection<ClientExternalMediaList> externalMediaLists) {
            LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList = this.generator.filterExternalMediaLists(externalMediaLists);
            return this.persistFiltered(filteredExtMediaList);
        }

        private ClientModelPersister persistFiltered(LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList) {
            List<ClientExternalMediaList> list = filteredExtMediaList.newList;
            List<ClientExternalMediaList> update = filteredExtMediaList.updateList;

            SqliteStorage.this.externalMediaListTable.insert(list);
            SqliteStorage.this.externalMediaListTable.update(update);

            for (ClientExternalMediaList mediaList : list) {
                this.loadedData.getExternalMediaList().add(mediaList.getId());
            }
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList) {
            return this.persistFiltered(filteredExtMediaList);
        }

        @Override
        public ClientModelPersister persistExternalUsers(Collection<ClientExternalUser> externalUsers) {
            LoadWorkGenerator.FilteredExternalUser filteredExternalUser = this.generator.filterExternalUsers(externalUsers);
            return this.persist(filteredExternalUser);
        }

        private ClientModelPersister persistFiltered(LoadWorkGenerator.FilteredExternalUser filteredExternalUser) {
            List<ClientExternalUser> list = filteredExternalUser.newUser;
            List<ClientExternalUser> update = filteredExternalUser.updateUser;

            List<ClientExternalMediaList> externalMediaLists = filteredExternalUser.newList;
            List<ClientExternalMediaList> updateExternalMediaLists = filteredExternalUser.updateList;

            SqliteStorage.this.externalUserTable.insert(list);
            SqliteStorage.this.externalUserTable.update(update);
            SqliteStorage.this.externalMediaListTable.insert(externalMediaLists);
            SqliteStorage.this.externalMediaListTable.update(updateExternalMediaLists);

            for (ClientExternalUser user : list) {
                this.loadedData.getExternalUser().add(user.getUuid());
            }

            for (ClientExternalMediaList mediaList : externalMediaLists) {
                this.loadedData.getExternalMediaList().add(mediaList.getListId());
            }
            return this;
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredExternalUser filteredExternalUser) {
            return this.persistFiltered(filteredExternalUser);
        }

        @Override
        public ClientModelPersister persistMedia(Collection<ClientMedium> media) {
            LoadWorkGenerator.FilteredMedia filteredMedia = this.generator.filterMedia(media);
            return persist(filteredMedia);
        }

        @Override
        public ClientModelPersister persist(LoadWorkGenerator.FilteredMedia filteredMedia) {
            List<ClientMedium> list = filteredMedia.newMedia;
            List<ClientMedium> update = filteredMedia.updateMedia;

            SqliteStorage.this.mediumTable.insert(list);
            SqliteStorage.this.mediumTable.update(update);

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
            SqliteStorage.this.newsTable.update(update);

            for (ClientNews clientNews : list) {
                this.loadedData.getNews().add(clientNews.getId());
            }
            return this;
        }

        @Override
        public ClientModelPersister persistParts(Collection<ClientPart> parts) {
            LoadWorkGenerator.FilteredParts filteredParts = this.generator.filterParts(parts);
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
            List<ClientPart> list = filteredParts.newParts;
            List<ClientPart> update = filteredParts.updateParts;

            SqliteStorage.this.partTable.insert(list);
            SqliteStorage.this.partTable.update(update);

            for (ClientPart part : list) {
                this.loadedData.getPart().add(part.getId());
            }
            this.persistEpisodes(filteredParts.episodes);
            return this;
        }

        @Override
        public ClientModelPersister persistReadEpisodes(Collection<ClientReadEpisode> readEpisodes) {
            LoadWorkGenerator.FilteredReadEpisodes filteredReadEpisodes = this.generator.filterReadEpisodes(readEpisodes);
            return this.persist(filteredReadEpisodes);
        }

        @Override
        public void finish() {

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

        @Override
        public ClientModelPersister persist(ClientStat.ParsedStat stat) {
            /*
             * Remove any Join not defined in stat.lists
             * Remove any Join not defined in stat.exLists
             * Remove any ExList not defined for a user in stat.exUser
             * Remove any ExList which is not a key in stat.exLists
             * Remove any List which is not a key in stat.Lists
             * Remove any ExUser which is not a key in stat.exUser
             */
            List<ListUser> listUser = SqliteStorage.this.externalMediaListTable.getListUser();

            Set<Integer> deletedExLists = new HashSet<>();
            Set<String> deletedExUser = new HashSet<>();
            Set<Integer> deletedLists = new HashSet<>();
            List<ListMediumJoin> newInternalJoins = new LinkedList<>();
            List<ListMediumJoin> toDeleteInternalJoins = this.filterListMediumJoins(stat, deletedLists, newInternalJoins, false);
            List<ListMediumJoin> newExternalJoins = new LinkedList<>();
            List<ListMediumJoin> toDeleteExternalJoins = this.filterListMediumJoins(stat, deletedExLists, newExternalJoins, true);

            listUser.forEach(user -> {
                List<Integer> listIds = stat.extUser.get(user.uuid);
                if (listIds == null) {
                    deletedExUser.add(user.uuid);
                    deletedExLists.add(user.listId);
                    return;
                }
                if (!listIds.contains(user.listId)) {
                    deletedExLists.add(user.listId);
                }
            });

            SqliteStorage.this.externalListMediumJoinTable.removeJoin(toDeleteExternalJoins);
            SqliteStorage.this.externalListMediumJoinTable.insert(newExternalJoins);
            SqliteStorage.this.listMediumJoinTable.removeJoin(toDeleteInternalJoins);
            SqliteStorage.this.listMediumJoinTable.insert(newInternalJoins);

            SqliteStorage.this.externalMediaListTable.delete(deletedExLists);
            SqliteStorage.this.mediaListTable.delete(deletedLists);
            SqliteStorage.this.externalUserTable.delete(deletedExUser);
            return this;
        }

        private List<ListMediumJoin> filterListMediumJoins(final ClientStat.ParsedStat stat, final Set<Integer> deletedLists, final List<ListMediumJoin> newJoins, final boolean external) {
            final List<ListMediumJoin> previousListJoins;
            final Map<Integer, List<Integer>> currentJoins;
            if (external) {
                currentJoins = stat.extLists;
                previousListJoins = SqliteStorage.this.externalListMediumJoinTable.getListItems();
            } else {
                currentJoins = stat.lists;
                previousListJoins = SqliteStorage.this.listMediumJoinTable.getListItems();
            }
            final Map<Integer, Set<Integer>> previousListJoinMap = new HashMap<>();

            previousListJoins.removeIf(join -> {
                previousListJoinMap.computeIfAbsent(join.listId, integer -> new HashSet<>()).add(join.mediumId);
                final List<Integer> currentListItems = currentJoins.get(join.listId);
                if (currentListItems == null) {
                    deletedLists.add(join.listId);
                    // TODO 02.6.2020: should maybe return 'false', to not remove it,
                    //  so that this join may be deleted later, as the list is also deleted
                    return true;
                }
                return currentListItems.contains(join.mediumId);
            });

            // every join that is not in previousListJoin is added to newJoins
            for (Map.Entry<Integer, List<Integer>> entry : currentJoins.entrySet()) {
                final Set<Integer> previousItems = previousListJoinMap.getOrDefault(entry.getKey(), Collections.emptySet());

                for (Integer mediumId : entry.getValue()) {
                    if (!previousItems.contains(mediumId)) {
                        newJoins.add(new ListMediumJoin(entry.getKey(), mediumId, external));
                    }
                }
            }
            return previousListJoins;
        }

        @Override
        public void deleteLeftoverEpisodes(Map<Integer, List<Integer>> partEpisodes) {
            List<PartEpisode> episodes = SqliteStorage.this.episodeTable.getEpisodes(partEpisodes.keySet());

            List<Integer> deleteEpisodes = new LinkedList<>();

            episodes.forEach(roomPartEpisode -> {
                List<Integer> episodeIds = partEpisodes.get(roomPartEpisode.partId);

                if (episodeIds == null || !episodeIds.contains(roomPartEpisode.episodeId)) {
                    deleteEpisodes.add(roomPartEpisode.episodeId);
                }
            });

            SqliteStorage.this.episodeTable.deletePerId(deleteEpisodes);
        }

        @Override
        public Collection<Integer> deleteLeftoverReleases(Map<Integer, List<ClientSimpleRelease>> partReleases) {
            List<SmallRelease> roomReleases = SqliteStorage.this.episodeTable.getReleases(partReleases.keySet());

            List<SmallRelease> deleteRelease = new LinkedList<>();
            Collection<ClientSimpleRelease> unmatchedReleases = new HashSet<>();

            for (List<ClientSimpleRelease> list : partReleases.values()) {
                unmatchedReleases.addAll(list);
            }

            roomReleases.forEach(release -> {
                List<ClientSimpleRelease> releases = partReleases.get(release.partId);

                boolean found = false;

                if (releases != null) {
                    for (ClientSimpleRelease simpleRelease : releases) {
                        if (simpleRelease.id == release.episodeId && Objects.equals(simpleRelease.url, release.url)) {
                            found = true;
                            unmatchedReleases.remove(simpleRelease);
                            break;
                        }
                    }
                }

                if (!found) {
                    deleteRelease.add(new SmallRelease(0, release.episodeId, release.url));
                }
            });
            Collection<Integer> episodesToLoad = new HashSet<>();

            for (ClientSimpleRelease release : unmatchedReleases) {
                episodesToLoad.add(release.id);
            }

            SqliteStorage.this.releaseTable.delete(deleteRelease);
            return episodesToLoad;
        }

        @Override
        public ClientModelPersister persistReleases(Collection<ClientRelease> releases) {
            SqliteStorage.this.releaseTable.insert(releases);
            return this;
        }

        @Override
        public void deleteLeftoverTocs(Map<Integer, List<String>> mediaTocs) {
            List<Toc> previousTocs = SqliteStorage.this.tocTable.getTocs(mediaTocs.keySet());
            List<Toc> removeTocs = new ArrayList<>();

            for (Toc entry : previousTocs) {
                final List<String> currentTocLinks = mediaTocs.get(entry.getMediumId());

                if (currentTocLinks == null || currentTocLinks.contains(entry.getLink())) {
                    removeTocs.add(entry);
                }
            }
            SqliteStorage.this.tocTable.delete(removeTocs);
        }

        @Override
        public ClientModelPersister persistTocs(Collection<? extends Toc> tocs) {
            SqliteStorage.this.tocTable.insert(tocs);
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
