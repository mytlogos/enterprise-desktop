package com.mytlogos.enterprisedesktop.background;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.api.Client;
import com.mytlogos.enterprisedesktop.background.api.DesktopNetworkIdentificator;
import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorkGenerator;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.SqliteStorage;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.profile.DisplayEpisodeProfile;
import com.mytlogos.enterprisedesktop.tools.ContentTool;
import com.mytlogos.enterprisedesktop.tools.FileTools;
import com.mytlogos.enterprisedesktop.tools.Sorting;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.concurrent.Task;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
class RepositoryImpl implements Repository {
    private final Client client;
    private final DatabaseStorage storage;
    private final ClientModelPersister persister;
    private final LiveData<User> storageUserLiveData;
    private final LoadData loadedData;
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
            // TODO 02.6.2020: implement this
            throw new UnsupportedOperationException();
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
            Utils.doPartitionedEx(episodeIds, ids -> {
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
    public LiveData<PagedList<DisplayRelease>> getDisplayEpisodes(DisplayEpisodeProfile filter) {
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
    public LiveData<List<MediumItem>> getAllMedia(Sorting sortings) {
        return this.storage.getAllMedia(sortings);
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
    public LiveData<List<SimpleMedium>> getSimpleMediumItems(int listId, boolean external) {
        return this.storage.getSimpleMediumItems(listId, external);
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
    public int addList(MediaList list, boolean autoDownload) throws IOException {
        final User value = this.storage.getUserNow();

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
        if (autoDownload) {
            ToDownload toDownload = new ToDownload(
                    false,
                    null,
                    clientMediaList.getId(),
                    null
            );
            this.storage.updateToDownload(true, toDownload);
        }
        return clientMediaList.getListId();
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
        TaskManager.runTask(() -> {
            try {
                final Boolean success = checkAndGetBody(this.client.updateProgress(episodeId, progress));
                if (success) {
                    this.storage.updateProgress(Collections.singleton(episodeId), progress);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<SearchResponse> requestSearch(SearchRequest searchRequest) {
        try {
            final Response<List<SearchResponse>> response = this.client.searchRequest(searchRequest);
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public LiveData<List<Integer>> getListItems(Collection<Integer> listIds) {
        return this.storage.getListItems(listIds);
    }

    @Override
    public ReloadStat checkReload(ClientStat.ParsedStat parsedStat) {
        return this.storage.checkReload(parsedStat);
    }

    @Override
    public boolean isEpisodeLoaded(int episodeId) {
        return this.loadedData.getEpisodes().contains(episodeId);
    }

    @Override
    public boolean isExternalUserLoaded(String uuid) {
        return this.loadedData.getExternalUser().contains(uuid);
    }

    @Override
    public boolean isPartLoaded(int partId) {
        return this.loadedData.getPart().contains(partId);
    }

    @Override
    public boolean isMediumLoaded(int mediumId) {
        return this.loadedData.getMedia().contains(mediumId);
    }

    @Override
    public Client getClient(Task<?> worker) {
        if (worker == null || worker.isDone()) {
            throw new IllegalArgumentException("not an active Worker");
        }
        return this.client;
    }

    @Override
    public ClientModelPersister getPersister(Task<?> worker) {
        if (worker == null || worker.isDone()) {
            throw new IllegalArgumentException("not an active Worker");
        }
        return this.persister;
    }

    @Override
    public LiveData<List<String>> getToc(int mediumId) {
        return this.storage.getToc(mediumId);
    }

    @Override
    public LiveData<List<MediaList>> getParentLists(int mediumId) {
        return this.storage.getParentLists(mediumId);
    }

    @Override
    public boolean removeToc(int mediumId, String link) {
        try {
            final Response<Boolean> response = this.client.removeToc(mediumId, link);
            final Boolean body = Utils.checkAndGetBody(response);

            if (body) {
                this.storage.removeToc(mediumId, link);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addToc(int mediumId, String link) {
        try {
            final Response<Boolean> response = this.client.removeToc(mediumId, link);
            final Boolean body = Utils.checkAndGetBody(response);

            if (body) {
                this.persister.persistTocs(Collections.singleton(new SimpleToc(mediumId, link)));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CompletableFuture<Boolean> mergeMedia(int sourceId, int destinationId) {
        return TaskManager.runCompletableTask(() -> {
            try {
                final Boolean success = Utils.checkAndGetBody(this.client.mergeMedia(sourceId, destinationId));
                if (success) {
                    this.storage.removeMedium(sourceId);
                }
                return success;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    @Override
    public List<Toc> getAllTocs() {
        return this.storage.getAllTocs();
    }

    @Override
    public List<MediumEpisode> getMediumEpisodes() {
        return this.storage.getMediumEpisodes();
    }

    private void reloadEpisodes(Collection<Integer> episodeIds) throws Exception {
        Utils.doPartitionedEx(episodeIds, integers -> {
            List<ClientEpisode> episodes = this.client.getEpisodes(integers).body();

            if (episodes == null) {
                return null;
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
}
