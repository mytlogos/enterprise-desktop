package com.mytlogos.enterprisedesktop.background;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.api.Client;
import com.mytlogos.enterprisedesktop.background.api.DesktopNetworkIdentificator;
import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.background.resourceLoader.BlockingLoadWorker;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorkGenerator;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorker;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.SqliteStorage;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.controller.ReleaseFilter;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.preferences.MainPreferences;
import com.mytlogos.enterprisedesktop.tools.*;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 *
 */
class RepositoryImpl implements Repository {
    private final Client client;
    private final DatabaseStorage storage;
    private final ClientModelPersister persister;
    private final LiveData<User> storageUserLiveData;
    private final LoadData loadedData;
    private final LoadWorker loadWorker;
    private final EditService editService;

    RepositoryImpl() {
        this.loadedData = new LoadData();
        this.client = new Client(new DesktopNetworkIdentificator());
        this.storage = new SqliteStorage();
        this.persister = this.storage.getPersister(this, this.loadedData);
        this.storageUserLiveData = this.storage.getUser().map(value -> {
            if (value == null) {
                this.client.clearAuthentication();
            } else {
                this.client.setAuthentication(value.getUuid(), value.getSession());
            }
            return value;
        });

        DependantGenerator dependantGenerator = this.storage.getDependantGenerator(this.loadedData);
        this.loadWorker = new BlockingLoadWorker(
                this.loadedData,
                this,
                this.persister,
                dependantGenerator
        );
        this.editService = new EditService(this.client, this.storage, this.persister);

    }

    @Override
    public boolean isClientOnline() {
        return this.client.isOnline();
    }

    @Override
    public boolean isClientAuthenticated() {
        return this.client.isAuthenticated();
    }

    @Override
    public LoadWorker getLoadWorker() {
        return loadWorker;
    }

    @Override
    public LiveData<HomeStats> getHomeStats() {
        return this.storage.getHomeStats();
    }

    @Override
    public LiveData<User> getUser() {
        return this.storageUserLiveData;
    }

    @Override
    public void updateUser(UpdateUser updateUser) {
        this.editService.updateUser(updateUser);
    }

    @Override
    public void deleteAllUser() {
        TaskManager.runTask(storage::deleteAllUser);
    }

    /**
     * Synchronous Login.
     *
     * @param email    email or name of the user
     * @param password password of the user
     * @throws IOException if an connection problem arose
     */
    @Override
    public void login(String email, String password) throws IOException {
        Response<ClientUser> response = this.client.login(email, password);
        ClientUser user = response.body();

        if (user != null) {
            // set authentication in client before persisting user,
            // as it may load data which requires authentication
            this.client.setAuthentication(user.getUuid(), user.getSession());
        } else if (!response.isSuccessful()) {
            checkAndGetBody(response);
        }
        persister.persist(user);
    }

    /**
     * Synchronous Registration.
     *
     * @param email    email or name of the user
     * @param password password of the user
     */
    @Override
    public void register(String email, String password) throws IOException {
        Response<ClientUser> response = this.client.register(email, password);
        ClientUser user = response.body();

        if (user != null) {
            // set authentication in client before persisting user,
            // as it may load data which requires authentication
            this.client.setAuthentication(user.getUuid(), user.getSession());
        } else if (!response.isSuccessful()) {
            checkAndGetBody(response);
        }
        persister.persist(user).finish();
    }

    @Override
    public void logout() {
        TaskManager.runTask(() -> {
            try {
                Response<Boolean> response = this.client.logout();
                if (!response.isSuccessful()) {
                    System.out.println("Log out was not successful: " + response.message());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            storage.deleteAllUser();
        });
    }

    @Override
    public void loadAllMedia() {
        try {
            Response<List<Integer>> response = this.client.getAllMedia();
            List<Integer> mediaIds = response.body();

            if (mediaIds == null) {
                return;
            }
            for (Integer mediumId : mediaIds) {
                if (this.loadedData.getMedia().contains(mediumId) || this.loadWorker.isMediumLoading(mediumId)) {
                    continue;
                }
                this.loadWorker.addIntegerIdTask(mediumId, null, this.loadWorker.MEDIUM_LOADER);
            }
            this.loadWorker.work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<List<ClientEpisode>> loadEpisodeAsync(Collection<Integer> episodeIds) {
        return CompletableFuture.supplyAsync(() -> loadEpisodeSync(episodeIds));
    }

    @Override
    public List<ClientEpisode> loadEpisodeSync(Collection<Integer> episodeIds) {
        try {
            System.out.println("loading episodes: " + episodeIds + " on " + Thread.currentThread());
            return this.client.getEpisodes(episodeIds).body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompletableFuture<List<ClientMedium>> loadMediaAsync(Collection<Integer> mediaIds) {
        return CompletableFuture.supplyAsync(() -> this.loadMediaSync(mediaIds));
    }

    @Override
    public List<ClientMedium> loadMediaSync(Collection<Integer> mediaIds) {
        try {
            System.out.println("loading media: " + mediaIds + " on " + Thread.currentThread());
            return this.client.getMedia(mediaIds).body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompletableFuture<List<ClientPart>> loadPartAsync(Collection<Integer> partIds) {
        return CompletableFuture.supplyAsync(() -> this.loadPartSync(partIds));
    }

    @Override
    public List<ClientPart> loadPartSync(Collection<Integer> partIds) {
        try {
            System.out.println("loading parts: " + partIds + " on " + Thread.currentThread());
            return this.client.getParts(partIds).body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompletableFuture<ClientMultiListQuery> loadMediaListAsync(Collection<Integer> listIds) {
        return CompletableFuture.supplyAsync(() -> this.loadMediaListSync(listIds));
    }

    @Override
    public ClientMultiListQuery loadMediaListSync(Collection<Integer> listIds) {
        try {
            System.out.println("loading lists: " + listIds + " on " + Thread.currentThread());
            return this.client.getLists(listIds).body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompletableFuture<List<ClientExternalMediaList>> loadExternalMediaListAsync(Collection<Integer> externalListIds) {
        return CompletableFuture.supplyAsync(() -> this.loadExternalMediaListSync(externalListIds));
    }

    @Override
    public List<ClientExternalMediaList> loadExternalMediaListSync(Collection<Integer> externalListIds) {
        System.out.println("loading ExtLists: " + externalListIds + " on " + Thread.currentThread());
//        try {
//                List<ClientEpisode> body = this.client.getExternalUser(episodeIds).execute().body();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        // todo implement loading of externalMediaLists
        return null;
    }

    @Override
    public CompletableFuture<List<ClientExternalUser>> loadExternalUserAsync(Collection<String> externalUuids) {
        return CompletableFuture.supplyAsync(() -> this.loadExternalUserSync(externalUuids));
    }

    @Override
    public List<ClientExternalUser> loadExternalUserSync(Collection<String> externalUuids) {
        try {
            System.out.println("loading DisplayExternalUser: " + externalUuids + " on " + Thread.currentThread());
            return this.client.getExternalUser(externalUuids).body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompletableFuture<List<ClientNews>> loadNewsAsync(Collection<Integer> newsIds) {
        return CompletableFuture.supplyAsync(() -> this.loadNewsSync(newsIds));
    }

    @Override
    public List<ClientNews> loadNewsSync(Collection<Integer> newsIds) {
        try {
            System.out.println("loading News: " + newsIds + " on " + Thread.currentThread());
            return this.client.getNews(newsIds).body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LiveData<PagedList<News>> getNews() {
        return this.storage.getNews();
    }

    @Override
    public void removeOldNews() {
        TaskManager.runTask(storage::deleteOldNews);
    }

    @Override
    public boolean isLoading() {
        return storage.isLoading();
    }

    @Override
    public void refreshNews(LocalDateTime latest) throws IOException {
        List<ClientNews> news = this.client.getNews(latest, null).body();

        if (news != null) {
            this.persister.persistNews(news);
        }
    }

    @Override
    public void loadInvalidated() throws IOException {
        List<InvalidatedData> invalidatedData = this.client.getInvalidated().body();

        if (invalidatedData == null || invalidatedData.isEmpty()) {
            return;
        }

        boolean userUpdated = false;
        LoadWorker loadWorker = this.getLoadWorker();

        for (InvalidatedData datum : invalidatedData) {
            if (datum.isUserUuid()) {
                userUpdated = true;

            } else if (datum.getEpisodeId() > 0) {
                loadWorker.addIntegerIdTask(datum.getEpisodeId(), null, loadWorker.EPISODE_LOADER);

            } else if (datum.getPartId() > 0) {
                loadWorker.addIntegerIdTask(datum.getPartId(), null, loadWorker.PART_LOADER);

            } else if (datum.getMediumId() > 0) {
                loadWorker.addIntegerIdTask(datum.getMediumId(), null, loadWorker.MEDIUM_LOADER);

            } else if (datum.getListId() > 0) {
                loadWorker.addIntegerIdTask(datum.getListId(), null, loadWorker.MEDIALIST_LOADER);

            } else if (datum.getExternalListId() > 0) {
                loadWorker.addIntegerIdTask(datum.getExternalListId(), null, loadWorker.EXTERNAL_MEDIALIST_LOADER);

            } else if (datum.getExternalUuid() != null && !datum.getExternalUuid().isEmpty()) {
                loadWorker.addStringIdTask(datum.getExternalUuid(), null, loadWorker.EXTERNAL_USER_LOADER);

            } else if (datum.getNewsId() > 0) {
                loadWorker.addIntegerIdTask(datum.getNewsId(), null, loadWorker.NEWS_LOADER);
            } else {
                System.out.println("unknown invalid data: " + datum);
            }
        }

        if (userUpdated) {
            ClientSimpleUser user = this.client.checkLogin().body();
            this.persister.persist(user);
        }
        loadWorker.work();
    }

    @Override
    public List<Integer> getSavedEpisodes() {
        return this.storage.getSavedEpisodes();
    }

    @Override
    public void updateSaved(int episodeId, boolean saved) {
        this.storage.updateSaved(episodeId, saved);
    }

    @Override
    public void updateSaved(Collection<Integer> episodeIds, boolean saved) {
        try {
            Utils.doPartitioned(episodeIds, ids -> {
                this.storage.updateSaved(ids, saved);
                return false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getToDeleteEpisodes() {
        return this.storage.getToDeleteEpisodes();
    }

    @Override
    public List<ClientDownloadedEpisode> downloadEpisodes(Collection<Integer> episodeIds) throws IOException {
        return this.client.downloadEpisodes(episodeIds).body();
    }

    @Override
    public List<ToDownload> getToDownload() {
        return this.storage.getAllToDownloads();
    }

    @Override
    public void addToDownload(ToDownload toDownload) {
        this.persister.persist(toDownload).finish();
    }

    @Override
    public void removeToDownloads(Collection<ToDownload> toDownloads) {
        this.storage.removeToDownloads(toDownloads);
    }

    @Override
    public Collection<Integer> getExternalListItems(Integer externalListId) {
        return this.storage.getExternalListItems(externalListId);
    }

    @Override
    public Collection<Integer> getListItems(Integer listId) {
        return this.storage.getListItems(listId);
    }

    @Override
    public List<Integer> getDownloadableEpisodes(Collection<Integer> mediaIds) {
        return this.storage.getDownloadableEpisodes(mediaIds);
    }

    @Override
    public List<Integer> getDownloadableEpisodes(Integer mediumId, int limit) {
        return this.storage.getDownloadableEpisodes(mediumId, limit);
    }

    @Override
    public LiveData<PagedList<DisplayRelease>> getDisplayEpisodes(ReleaseFilter filter) {
        return this.storage.getDisplayEpisodes(filter);
    }

    @Override
    public LiveData<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium) {
        return this.storage.getDisplayEpisodesGrouped(saved, medium);
    }

    @Override
    public LiveData<List<MediaList>> getLists() {
        return this.storage.getLists();
    }

    @Override
    public LiveData<? extends MediaListSetting> getListSettings(int id, boolean isExternal) {
        return this.storage.getListSetting(id, isExternal);
    }

    @Override
    public CompletableFuture<String> updateListName(MediaListSetting listSetting, String newName) {
        return this.editService.updateListName(listSetting, newName);
    }

    @Override
    public CompletableFuture<String> updateListMedium(MediaListSetting listSetting, int newMediumType) {
        return this.editService.updateListMedium(listSetting, newMediumType);
    }

    @Override
    public void updateToDownload(boolean add, ToDownload toDownload) {
        this.storage.updateToDownload(add, toDownload);
    }

    @Override
    public LiveData<PagedList<MediumItem>> getAllMedia(Sorting sortings, String title, int medium, String author, LocalDateTime lastUpdate, int minCountEpisodes, int minCountReadEpisodes) {
        return this.storage.getAllMedia(sortings, title, medium, author, lastUpdate, minCountEpisodes, minCountReadEpisodes);
    }

    @Override
    public LiveData<MediumSetting> getMediumSettings(int mediumId) {
        return this.storage.getMediumSettings(mediumId);
    }

    @Override
    public CompletableFuture<String> updateMedium(MediumSetting mediumSettings) {
        return this.editService.updateMedium(mediumSettings);
    }

    @Override
    public LiveData<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved) {
        return this.storage.getToc(mediumId, sortings, read, saved);
    }

    @Override
    public LiveData<List<MediumItem>> getMediumItems(int listId, boolean isExternal) {
        return this.storage.getMediumItems(listId, isExternal);
    }

    @Override
    public void loadMediaInWaitSync() throws IOException {
        List<ClientMediumInWait> medium = this.client.getMediumInWait().body();

        if (medium != null && !medium.isEmpty()) {
            this.storage.clearMediaInWait();
            this.persister.persistMediaInWait(medium);
        }
    }

    @Override
    public void addList(MediaList list, boolean autoDownload) throws IOException {
        User value = this.storageUserLiveData.getValue();

        if (value == null || value.getUuid().isEmpty()) {
            throw new IllegalStateException("user is not authenticated");
        }
        ClientMediaList mediaList = new ClientMediaList(
                value.getUuid(),
                0,
                list.getName(),
                list.getMedium(),
                new int[0]
        );
        ClientMediaList clientMediaList = this.client.addList(mediaList).body();

        if (clientMediaList == null) {
            throw new IllegalArgumentException("adding list failed");
        }

        this.persister.persist(clientMediaList);
        ToDownload toDownload = new ToDownload(
                false,
                null,
                clientMediaList.getId(),
                null
        );
        this.storage.updateToDownload(true, toDownload);
    }

    @Override
    public boolean listExists(String listName) {
        return this.storage.listExists(listName);
    }

    @Override
    public int countSavedUnreadEpisodes(Integer mediumId) {
        return this.storage.countSavedEpisodes(mediumId);
    }

    @Override
    public List<Integer> getSavedEpisodes(int mediumId) {
        return this.storage.getSavedEpisodes(mediumId);
    }

    @Override
    public Episode getEpisode(int episodeId) {
        return this.storage.getEpisode(episodeId);
    }

    @Override
    public List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> ids) {
        return this.storage.getSimpleEpisodes(ids);
    }

    @Override
    public LiveData<PagedList<ReadEpisode>> getReadTodayEpisodes() {
        return this.storage.getReadTodayEpisodes();
    }

    @Override
    public LiveData<PagedList<MediumInWait>> getMediaInWaitBy(String filter, int mediumFilter, String hostFilter, Sorting sortings) {
        return this.storage.getMediaInWaitBy(filter, mediumFilter, hostFilter, sortings);
    }

    @Override
    public LiveData<List<MediaList>> getInternLists() {
        return this.storage.getInternLists();
    }

    @Override
    public CompletableFuture<Boolean> moveMediaToList(int oldListId, int listId, Collection<Integer> ids) {
        return this.editService.moveMediaToList(oldListId, listId, ids);
    }

    @Override
    public List<MediumInWait> getSimilarMediaInWait(MediumInWait mediumInWait) {
        return this.storage.getSimilarMediaInWait(mediumInWait);
    }

    @Override
    public LiveData<List<SimpleMedium>> getMediaSuggestions(String title, int medium) {
        return this.storage.getMediaSuggestions(title, medium);
    }

    @Override
    public LiveData<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium) {
        return this.storage.getMediaInWaitSuggestions(title, medium);
    }

    @Override
    public CompletableFuture<Boolean> consumeMediumInWait(SimpleMedium selectedMedium, List<MediumInWait> mediumInWaits) {
        return TaskManager.runCompletableTask(() -> {
            Collection<ClientMediumInWait> others = new HashSet<>();

            if (mediumInWaits != null) {
                for (MediumInWait inWait : mediumInWaits) {
                    others.add(new ClientMediumInWait(
                            inWait.getTitle(),
                            inWait.getMedium(),
                            inWait.getLink()
                    ));
                }
            }
            try {
                Boolean success = this.client.consumeMediumInWait(selectedMedium.getMediumId(), others).body();

                if (success != null && success) {
                    this.storage.deleteMediaInWait(mediumInWaits);
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> createMedium(MediumInWait mediumInWait, List<MediumInWait> mediumInWaits, MediaList list) {
        return TaskManager.runCompletableTask(() -> {
            ClientMediumInWait medium = new ClientMediumInWait(
                    mediumInWait.getTitle(),
                    mediumInWait.getMedium(),
                    mediumInWait.getLink()
            );
            Collection<ClientMediumInWait> others = new HashSet<>();

            if (mediumInWaits != null) {
                for (MediumInWait inWait : mediumInWaits) {
                    others.add(new ClientMediumInWait(
                            inWait.getTitle(),
                            inWait.getMedium(),
                            inWait.getLink()
                    ));
                }
            }
            Integer listId = list == null ? null : list.getListId();
            try {
                ClientMedium clientMedium = this.client.createFromMediumInWait(medium, others, listId).body();

                if (clientMedium == null) {
                    return false;
                }
                this.persister.persist(clientMedium);

                Collection<MediumInWait> toDelete = new HashSet<>();
                toDelete.add(mediumInWait);

                if (mediumInWaits != null) {
                    toDelete.addAll(mediumInWaits);
                }
                this.storage.deleteMediaInWait(toDelete);

                if (listId != null && listId > 0) {
                    this.storage.addItemsToList(listId, Collections.singleton(clientMedium.getId()));
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> removeItemFromList(int listId, int mediumId) {
        return this.editService.removeItemFromList(listId, Collections.singleton(mediumId));
    }

    @Override
    public CompletableFuture<Boolean> removeItemFromList(int listId, Collection<Integer> mediumId) {
        return this.editService.removeItemFromList(listId, mediumId);
    }

    @Override
    public CompletableFuture<Boolean> moveItemFromList(int oldListId, int newListId, int mediumId) {
        return this.editService.moveItemFromList(oldListId, newListId, mediumId);
    }

    @Override
    public LiveData<List<MediaList>> getListSuggestion(String name) {
        return this.storage.getListSuggestion(name);
    }

    @Override
    public LiveData<Boolean> onDownloadable() {
        return this.storage.onDownloadAble();
    }

    @Override
    public void removeDanglingMedia(Collection<Integer> mediaIds) {
        this.storage.removeDanglingMedia(mediaIds);
    }

    @Override
    public LiveData<List<MediumItem>> getAllDanglingMedia() {
        return this.storage.getAllDanglingMedia();
    }

    @Override
    public CompletableFuture<Boolean> addMediumToList(int listId, Collection<Integer> ids) {
        return this.editService.addMediumToList(listId, ids);
    }

    @Override
    public LiveData<PagedList<DisplayExternalUser>> getExternalUser() {
        return this.storage.getExternalUser();
    }

    @Override
    public SpaceMedium getSpaceMedium(int mediumId) {
        return this.storage.getSpaceMedium(mediumId);
    }

    @Override
    public int getMediumType(Integer mediumId) {
        return this.storage.getMediumType(mediumId);
    }

    @Override
    public List<String> getReleaseLinks(int episodeId) {
        return this.storage.getReleaseLinks(episodeId);
    }

    @Override
    public void syncWithTime() throws IOException {
        LocalDateTime lastSync = MainPreferences.getLastSync();
        syncChanged(lastSync);
        MainPreferences.setLastSync(LocalDateTime.now());
        syncDeleted();
    }

    @Override
    public void syncUser() throws IOException {
        Response<ClientUser> user = this.client.getUser();
        ClientUser body = user.body();

        if (!user.isSuccessful()) {
            try (ResponseBody responseBody = user.errorBody()) {
                if (responseBody != null) {
                    System.out.println(responseBody.string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.persister.persist(body).finish();
    }

    @Override
    public void clearLocalMediaData() {
        TaskManager.runTask(() -> {
            this.loadedData.getPart().clear();
            this.loadedData.getEpisodes().clear();
            this.storage.clearLocalMediaData();
        });
    }

    @Override
    public LiveData<PagedList<NotificationItem>> getNotifications() {
        return this.storage.getNotifications();
    }

    @Override
    public void updateFailedDownloads(int episodeId) {
        this.storage.updateFailedDownload(episodeId);
    }

    @Override
    public List<FailedEpisode> getFailedEpisodes(Collection<Integer> episodeIds) {
        return this.storage.getFailedEpisodes(episodeIds);
    }

    @Override
    public void addNotification(NotificationItem notification) {
        this.storage.addNotification(notification);
    }

    @Override
    public SimpleEpisode getSimpleEpisode(int episodeId) {
        return this.storage.getSimpleEpisode(episodeId);
    }

    @Override
    public SimpleMedium getSimpleMedium(Integer mediumId) {
        return this.storage.getSimpleMedium(mediumId);
    }

    @Override
    public LiveData<List<SimpleMedium>> getSimpleMedium() {
        return this.storage.getSimpleMedium();
    }

    @Override
    public void clearNotifications() {
        this.storage.clearNotifications();
    }

    @Override
    public void clearFailEpisodes() {
        TaskManager.runAsyncTask(this.storage::clearFailEpisodes);
    }

    @Override
    public void updateRead(int episodeId, boolean read) throws Exception {
        this.editService.updateRead(Collections.singletonList(episodeId), read);
    }

    @Override
    public void updateRead(Collection<Integer> episodeIds, boolean read) throws Exception {
        this.editService.updateRead(episodeIds, read);
    }

    @Override
    public void updateAllRead(int mediumId, boolean read) throws Exception {
        Collection<Integer> episodeIds = this.storage.getAllEpisodes(mediumId);
        this.updateRead(episodeIds, read);
    }

    @Override
    public void updateReadWithHigherIndex(double combiIndex, boolean read, int mediumId) throws Exception {
        List<Integer> episodeIds = this.storage.getEpisodeIdsWithHigherIndex(combiIndex, mediumId, read);
        this.updateRead(episodeIds, read);
    }

    @Override
    public void updateReadWithLowerIndex(double combiIndex, boolean read, int mediumId) throws Exception {
        List<Integer> episodeIds = this.storage.getEpisodeIdsWithLowerIndex(combiIndex, mediumId, read);
        this.updateRead(episodeIds, read);
    }

    @Override
    public void deleteAllLocalEpisodes(int mediumId) throws IOException {
        Collection<Integer> episodes = this.storage.getSavedEpisodes(mediumId);
        this.deleteLocalEpisodes(new HashSet<>(episodes), mediumId);
    }

    @Override
    public void deleteLocalEpisodesWithLowerIndex(double combiIndex, int mediumId) throws IOException {
        Collection<Integer> episodeIds = this.storage.getSavedEpisodeIdsWithLowerIndex(combiIndex, mediumId);
        this.deleteLocalEpisodes(new HashSet<>(episodeIds), mediumId);
    }

    @Override
    public void deleteLocalEpisodesWithHigherIndex(double combiIndex, int mediumId) throws IOException {
        Collection<Integer> episodeIds = this.storage.getSavedEpisodeIdsWithHigherIndex(combiIndex, mediumId);
        this.deleteLocalEpisodes(new HashSet<>(episodeIds), mediumId);
    }

    @Override
    public void deleteLocalEpisodes(Set<Integer> episodeIds, int mediumId) throws IOException {
        int medium = this.getMediumType(mediumId);

        ContentTool contentTool = FileTools.getContentTool(medium);

        if (!contentTool.isSupported()) {
            throw new IOException("medium type: " + medium + " is not supported");
        }

        contentTool.removeMediaEpisodes(mediumId, episodeIds);
        this.updateSaved(episodeIds, false);
    }

    @Override
    public void addProgressListener(Consumer<Integer> consumer) {
        this.loadWorker.addProgressListener(consumer);
    }

    @Override
    public void removeProgressListener(Consumer<Integer> consumer) {
        this.loadWorker.removeProgressListener(consumer);
    }

    @Override
    public void addTotalWorkListener(Consumer<Integer> consumer) {
        this.loadWorker.addTotalWorkListener(consumer);
    }

    @Override
    public void removeTotalWorkListener(Consumer<Integer> consumer) {
        this.loadWorker.removeTotalWorkListener(consumer);
    }

    @Override
    public int getLoadWorkerProgress() {
        return this.loadWorker.getProgress();
    }

    @Override
    public int getLoadWorkerTotalWork() {
        return this.loadWorker.getTotalWork();
    }

    @Override
    public void syncProgress() {
        this.storage.syncProgress();
    }

    @Override
    public void updateDataStructure(List<Integer> mediaIds, List<Integer> partIds) {
        this.storage.updateDataStructure(mediaIds, partIds);
    }

    @Override
    public void reloadLowerIndex(double combiIndex, int mediumId) throws Exception {
        List<Integer> episodeIds = this.storage.getEpisodeIdsWithLowerIndex(combiIndex, mediumId);
        this.reloadEpisodes(episodeIds);
    }

    @Override
    public void reloadHigherIndex(double combiIndex, int mediumId) throws Exception {
        List<Integer> episodeIds = this.storage.getEpisodeIdsWithHigherIndex(combiIndex, mediumId);
        this.reloadEpisodes(episodeIds);
    }

    @Override
    public void reload(Set<Integer> episodeIds) throws Exception {
        this.reloadEpisodes(episodeIds);
    }

    @Override
    public void reloadAll(int mediumId) throws IOException {
        ClientMedium medium = this.client.getMedium(mediumId).body();

        if (medium == null) {
            System.err.println("missing medium: " + mediumId);
            return;
        }
        int[] parts = medium.getParts();
        Collection<Integer> partIds = new ArrayList<>(parts.length);

        for (int part : parts) {
            partIds.add(part);
        }
        List<ClientPart> partBody = this.client.getParts(partIds).body();

        if (partBody == null) {
            return;
        }
        List<Integer> loadedPartIds = new ArrayList<>();

        for (ClientPart part : partBody) {
            loadedPartIds.add(part.getId());
        }
        LoadWorkGenerator generator = new LoadWorkGenerator(this.loadedData);
        LoadWorkGenerator.FilteredParts filteredParts = generator.filterParts(partBody);
        this.persister.persist(filteredParts);

        partIds.removeAll(loadedPartIds);
        this.storage.removeParts(partIds);
    }

    @Override
    public void downloadLowerIndex(double combiIndex, int mediumId) {
        List<Integer> episodeIds = this.storage.getEpisodeIdsWithLowerIndex(combiIndex, mediumId);
        ApplicationConfig.getTaskController().startDownloadTask(mediumId, episodeIds);
    }

    @Override
    public void downloadHigherIndex(double combiIndex, int mediumId) {
        List<Integer> episodeIds = this.storage.getEpisodeIdsWithHigherIndex(combiIndex, mediumId);
        ApplicationConfig.getTaskController().startDownloadTask(mediumId, episodeIds);
    }

    @Override
    public void download(Set<Integer> episodeIds, int mediumId) {
        ApplicationConfig.getTaskController().startDownloadTask(mediumId, new ArrayList<>(episodeIds));
    }

    @Override
    public void downloadAll(int mediumId) {
        Collection<Integer> episodeIds = this.storage.getAllEpisodes(mediumId);
        ApplicationConfig.getTaskController().startDownloadTask(mediumId, new ArrayList<>(episodeIds));
    }

    @Override
    public void updateProgress(int episodeId, float progress) {
        TaskManager.runTask(() -> this.storage.updateProgress(Collections.singleton(episodeId), progress));
    }

    @Override
    public List<SearchResponse> requestSearch(SearchRequest searchRequest) {
        try {
            final Response<List<SearchResponse>> response = this.client.searchRequest(searchRequest);
            final List<SearchResponse> body = response.body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public LiveData<List<Integer>> getListItems(Collection<Integer> listIds) {
        return this.storage.getListItems(listIds);
    }

    private void reloadEpisodes(Collection<Integer> episodeIds) throws Exception {
        Utils.doPartitioned(episodeIds, integers -> {
            List<ClientEpisode> episodes = this.client.getEpisodes(integers).body();

            if (episodes == null) {
                return true;
            }
            LoadWorkGenerator generator = new LoadWorkGenerator(this.loadedData);
            LoadWorkGenerator.FilteredEpisodes filteredEpisodes = generator.filterEpisodes(episodes);
            this.persister.persist(filteredEpisodes);

            List<Integer> loadedIds = new ArrayList<>();

            for (ClientEpisode episode : episodes) {
                loadedIds.add(episode.getId());
            }

            integers.removeAll(loadedIds);
            this.storage.removeEpisodes(integers);
            return false;
        });
    }

    private <T> T checkAndGetBody(Response<T> response) throws IOException {
        T body = response.body();

        if (body == null) {
            String errorMsg = response.errorBody() != null ? response.errorBody().string() : null;
            throw new IOException(String.format("No Body, Http-Code %d, ErrorBody: %s", response.code(), errorMsg));
        }
        return body;
    }

    void initialize() {
        TaskManager.runTask(() -> {
            try {
                // ask the database what data it has, to check if it needs to be loaded from the server
                this.loadLoadedData();

                Response<ClientSimpleUser> call = this.client.checkLogin();

                ClientSimpleUser clientUser = call.body();

                if (clientUser != null) {
                    this.client.setAuthentication(clientUser.getUuid(), clientUser.getSession());
                }
                this.persister.persist(clientUser).finish();
                System.out.println("successful query");
            } catch (IOException e) {
                System.out.println("failed query");
            }
        });
    }

    private void loadLoadedData() {
        LoadData loadData = this.storage.getLoadData();

        this.loadedData.getMedia().addAll(loadData.getMedia());
        this.loadedData.getPart().addAll(loadData.getPart());
        this.loadedData.getEpisodes().addAll(loadData.getEpisodes());
        this.loadedData.getNews().addAll(loadData.getNews());
        this.loadedData.getMediaList().addAll(loadData.getMediaList());
        this.loadedData.getExternalUser().addAll(loadData.getExternalUser());
        this.loadedData.getExternalMediaList().addAll(loadData.getExternalMediaList());
    }

    private <T> Map<Integer, T> mapStringToInt(Map<String, T> map) {
        Map<Integer, T> result = new HashMap<>();

        for (Map.Entry<String, T> entry : map.entrySet()) {
            result.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return result;
    }

    private void firstSync() throws IOException {
        this.storage.clearAll();
        final List<ClientMedium> clientMedia = checkAndGetBody(this.client.getAllMediaFull());
        this.persister.persistMedia(clientMedia);
        clientMedia.clear();
        Log.info("First sync, loaded all Media");

        final List<ClientPart> clientParts = checkAndGetBody(this.client.getAllParts());
        this.persister.persistParts(clientParts);
        clientParts.clear();
        Log.info("First sync, loaded all Parts");

        final List<ClientEpisode> clientEpisodes = checkAndGetBody(this.client.getAllEpisodes());
        this.persister.persistEpisodes(clientEpisodes);
        clientEpisodes.clear();
        Log.info("First sync, loaded all Episodes");

        final List<ClientRelease> clientReleases = checkAndGetBody(this.client.getAllReleases());
        this.persister.persistReleases(clientReleases);
        clientReleases.clear();
        Log.info("First sync, loaded all Releases");

        final List<ClientMediaList> clientMediaLists = checkAndGetBody(this.client.getAllLists());
        this.persister.persistMediaLists(clientMediaLists);
        clientMediaLists.clear();
        Log.info("First sync, loaded all MediaLists");

        final List<ClientExternalUser> clientExternalUsers = checkAndGetBody(this.client.getAllExternalUsers());
        this.persister.persistExternalUsers(clientExternalUsers);
        clientExternalUsers.clear();
        Log.info("First sync, loaded all ExternalUsers");

        final List<ClientMediumInWait> clientMediumInWaits = checkAndGetBody(this.client.getMediumInWait());
        this.persister.persistMediaInWait(clientMediumInWaits);
        clientMediumInWaits.clear();
        Log.info("First sync, loaded all MediumInWaits");
    }

    private void syncChanged(LocalDateTime lastSync) throws IOException {
        if (lastSync == null) {
            Log.info("First sync, start loading all");
            this.firstSync();
            return;
        }
        Log.info("request changedEntities on ThreadId-%d: %s", Thread.currentThread().getId(), Thread.currentThread().getName());
        Response<ClientChangedEntities> changedEntitiesResponse = this.client.getNew(lastSync);
        ClientChangedEntities changedEntities = checkAndGetBody(changedEntitiesResponse);
        Log.info(
                "received changedEntities: %d media, %d parts, %d episodes, %d releases, %d lists, %d extUser, %d extLists, %d mediaInWait, %d news on ThreadId-%d: %s",
                () -> new Object[]{
                        changedEntities.media.size(),
                        changedEntities.parts.size(),
                        changedEntities.episodes.size(),
                        changedEntities.releases.size(),
                        changedEntities.lists.size(),
                        changedEntities.extUser.size(),
                        changedEntities.extLists.size(),
                        changedEntities.mediaInWait.size(),
                        changedEntities.news.size(),
                        Thread.currentThread().getId(),
                        Thread.currentThread().getName()
                }
        );

        // persist all new or updated entities, media to releases needs to be in this order
        this.persister.persistMedia(changedEntities.media);
        this.persistParts(changedEntities.parts);
        this.persistEpisodes(changedEntities.episodes);
        this.persistReleases(changedEntities.releases);
        this.persister.persistMediaLists(changedEntities.lists);
        this.persister.persistExternalUsers(changedEntities.extUser);
        this.persistExternalLists(changedEntities.extLists);
        this.persister.persistMediaInWait(changedEntities.mediaInWait);
        this.persister.persistNews(changedEntities.news);
    }

    private void persistParts(Collection<ClientPart> parts) throws IOException {
        Collection<Integer> missingIds = new HashSet<>();
        Collection<ClientPart> loadingParts = new HashSet<>();

        parts.removeIf(part -> {
            if (!this.loadedData.getMedia().contains(part.getMediumId())) {
                missingIds.add(part.getMediumId());
                loadingParts.add(part);
                return true;
            }
            return false;
        });

        this.persister.persistParts(parts);
        if (missingIds.isEmpty()) {
            return;
        }
        try {
            Utils.doPartitionedAsync(missingIds, parentIds -> {
                Log.info("loading %d media", parentIds.size());
                final Response<List<ClientMedium>> media = this.client.getMedia(parentIds);
                List<ClientMedium> parents = checkAndGetBody(media);
                this.persister.persistMedia(parents);
                return false;
            });
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw new IOException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
        this.persister.persistParts(loadingParts);
    }

    private void persistEpisodes(Collection<ClientEpisode> episodes) throws IOException {
        Collection<Integer> missingIds = new HashSet<>();
        Collection<ClientEpisode> loading = new HashSet<>();

        episodes.removeIf(value -> {
            if (!this.loadedData.getMedia().contains(value.getPartId())) {
                missingIds.add(value.getPartId());
                loading.add(value);
                return true;
            }
            return false;
        });

        this.persister.persistEpisodes(episodes);

        if (missingIds.isEmpty()) {
            return;
        }
        try {
            Utils.doPartitionedAsync(missingIds, partIds -> {
                Log.info("loading %d parts", partIds.size());
                final Response<List<ClientPart>> parts = this.client.getParts(partIds);
                List<ClientPart> parents = checkAndGetBody(parts);
                this.persistParts(parents);
                return false;
            });
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw new IOException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
        this.persister.persistEpisodes(loading);
    }

    private void persistReleases(Collection<ClientRelease> releases) throws IOException {
        Collection<Integer> missingIds = new HashSet<>();
        Collection<ClientRelease> loading = new HashSet<>();

        releases.removeIf(value -> {
            if (!this.loadedData.getMedia().contains(value.getEpisodeId())) {
                missingIds.add(value.getEpisodeId());
                loading.add(value);
                return true;
            }
            return false;
        });

        this.persister.persistReleases(releases);
        if (missingIds.isEmpty()) {
            return;
        }
        try {
            Utils.doPartitionedAsync(missingIds, parentIds -> {
                Log.info("loading %d episodes", parentIds.size());
                final Response<List<ClientEpisode>> episodes = this.client.getEpisodes(parentIds);
                List<ClientEpisode> parents = checkAndGetBody(episodes);
                this.persistEpisodes(parents);
                return false;
            });
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw new IOException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
        this.persister.persistReleases(loading);
    }

    private void persistExternalLists(Collection<ClientExternalMediaList> externalMediaLists) throws IOException {
        Collection<String> missingIds = new HashSet<>();
        Collection<ClientExternalMediaList> loading = new HashSet<>();

        externalMediaLists.removeIf(value -> {
            if (!this.loadedData.getExternalUser().contains(value.getUuid())) {
                missingIds.add(value.getUuid());
                loading.add(value);
                return true;
            }
            return false;
        });

        this.persister.persistExternalMediaLists(externalMediaLists);
        if (missingIds.isEmpty()) {
            return;
        }
        try {
            Utils.doPartitionedAsync(missingIds, parentIds -> {
                Log.info("loading %d externalUser", parentIds.size());
                final Response<List<ClientExternalUser>> externalUser = this.client.getExternalUser(parentIds);
                List<ClientExternalUser> parents = checkAndGetBody(externalUser);
                this.persister.persistExternalUsers(parents);
                return false;
            });
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw new IOException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
        this.persister.persistExternalMediaLists(loading);
    }

    private void syncDeleted() throws IOException {
        Response<ClientStat> statResponse = this.client.getStats();
        ClientStat statBody = checkAndGetBody(statResponse);

        ClientStat.ParsedStat parsedStat = statBody.parse();
        this.persister.persist(parsedStat);

        ReloadPart reloadPart = this.storage.checkReload(parsedStat);

        if (!reloadPart.loadPartEpisodes.isEmpty()) {
            Response<Map<String, List<Integer>>> partEpisodesResponse = this.client.getPartEpisodes(reloadPart.loadPartEpisodes);
            Map<String, List<Integer>> partStringEpisodes = checkAndGetBody(partEpisodesResponse);

            Map<Integer, List<Integer>> partEpisodes = mapStringToInt(partStringEpisodes);

            Collection<Integer> missingEpisodes = new HashSet<>();

            for (Map.Entry<Integer, List<Integer>> entry : partEpisodes.entrySet()) {
                for (Integer episodeId : entry.getValue()) {
                    if (!this.loadedData.getEpisodes().contains(episodeId)) {
                        missingEpisodes.add(episodeId);
                    }
                }
            }
            if (!missingEpisodes.isEmpty()) {
                final Response<List<ClientEpisode>> clientEpisodes = this.client.getEpisodes(missingEpisodes);
                List<ClientEpisode> episodes = checkAndGetBody(clientEpisodes);
                this.persistEpisodes(episodes);
            }

            this.persister.deleteLeftoverEpisodes(partEpisodes);

            reloadPart = this.storage.checkReload(parsedStat);
        }


        if (!reloadPart.loadPartReleases.isEmpty()) {
            Response<Map<String, List<ClientSimpleRelease>>> partReleasesResponse = this.client.getPartReleases(reloadPart.loadPartReleases);

            Map<String, List<ClientSimpleRelease>> partStringReleases = checkAndGetBody(partReleasesResponse);

            Map<Integer, List<ClientSimpleRelease>> partReleases = mapStringToInt(partStringReleases);

            Collection<Integer> missingEpisodes = new HashSet<>();

            for (Map.Entry<Integer, List<ClientSimpleRelease>> entry : partReleases.entrySet()) {
                for (ClientSimpleRelease release : entry.getValue()) {
                    if (!this.loadedData.getEpisodes().contains(release.id)) {
                        missingEpisodes.add(release.id);
                    }
                }
            }
            if (!missingEpisodes.isEmpty()) {
                final Response<List<ClientEpisode>> clientEpisodes = this.client.getEpisodes(missingEpisodes);
                List<ClientEpisode> episodes = checkAndGetBody(clientEpisodes);
                this.persistEpisodes(episodes);
            }
            Collection<Integer> episodesToLoad = this.persister.deleteLeftoverReleases(partReleases);

            if (!episodesToLoad.isEmpty()) {
                try {
                    Utils.doPartitionedAsync(episodesToLoad, ids -> {
                        Log.info("loading %d episodes for syncDelete", ids.size());
                        final Response<List<ClientEpisode>> clientEpisodes = this.client.getEpisodes(ids);
                        List<ClientEpisode> episodes = checkAndGetBody(clientEpisodes);
                        this.persistEpisodes(episodes);
                        return false;
                    });
                } catch (Exception e) {
                    if (e instanceof IOException) {
                        throw new IOException(e);
                    } else {
                        e.printStackTrace();
                    }
                }
            }

            reloadPart = this.storage.checkReload(parsedStat);
        }

        // as even now some errors crop up, just load all this shit and dump it in 100er steps
        if (!reloadPart.loadPartEpisodes.isEmpty() || !reloadPart.loadPartReleases.isEmpty()) {
            Collection<Integer> partsToLoad = new HashSet<>();
            partsToLoad.addAll(reloadPart.loadPartEpisodes);
            partsToLoad.addAll(reloadPart.loadPartReleases);

            try {
                Utils.doPartitionedAsync(partsToLoad, ids -> {
                    Log.info("loading %d parts for syncDelete", ids.size());
                    final Response<List<ClientPart>> clientParts = this.client.getParts(ids);
                    List<ClientPart> parts = checkAndGetBody(clientParts);
                    this.persistParts(parts);
                    return false;
                });
            } catch (Exception e) {
                if (e instanceof IOException) {
                    throw new IOException(e);
                } else {
                    e.printStackTrace();
                }
            }
            reloadPart = this.storage.checkReload(parsedStat);
        }

        if (!reloadPart.loadPartEpisodes.isEmpty()) {
            String msg = String.format(
                    "Episodes of %d Parts to load even after running once",
                    reloadPart.loadPartEpisodes.size()
            );
            System.out.println(msg);
        }

        if (!reloadPart.loadPartReleases.isEmpty()) {
            String msg = String.format(
                    "Releases of %d Parts to load even after running once",
                    reloadPart.loadPartReleases.size()
            );
            System.out.println(msg);
        }
    }
}
