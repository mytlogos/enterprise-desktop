package com.mytlogos.enterprisedesktop.background;

import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorker;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.controller.ReleaseFilter;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Sorting;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Repository {
    boolean isClientOnline();

    boolean isClientAuthenticated();

    LoadWorker getLoadWorker();

    LiveData<HomeStats> getHomeStats();

    LiveData<User> getUser();

    void updateUser(UpdateUser updateUser);

    void deleteAllUser();

    void login(String email, String password) throws IOException;

    void register(String email, String password) throws IOException;

    void logout();

    void loadAllMedia();

    CompletableFuture<List<ClientEpisode>> loadEpisodeAsync(Collection<Integer> episodeIds);

    List<ClientEpisode> loadEpisodeSync(Collection<Integer> episodeIds);

    CompletableFuture<List<ClientMedium>> loadMediaAsync(Collection<Integer> mediaIds);

    List<ClientMedium> loadMediaSync(Collection<Integer> mediaIds);

    CompletableFuture<List<ClientPart>> loadPartAsync(Collection<Integer> partIds);

    List<ClientPart> loadPartSync(Collection<Integer> partIds);

    CompletableFuture<ClientMultiListQuery> loadMediaListAsync(Collection<Integer> listIds);

    ClientMultiListQuery loadMediaListSync(Collection<Integer> listIds);

    CompletableFuture<List<ClientExternalMediaList>> loadExternalMediaListAsync(Collection<Integer> externalListIds);

    List<ClientExternalMediaList> loadExternalMediaListSync(Collection<Integer> externalListIds);

    CompletableFuture<List<ClientExternalUser>> loadExternalUserAsync(Collection<String> externalUuids);

    List<ClientExternalUser> loadExternalUserSync(Collection<String> externalUuids);

    CompletableFuture<List<ClientNews>> loadNewsAsync(Collection<Integer> newsIds);

    List<ClientNews> loadNewsSync(Collection<Integer> newsIds);

    LiveData<PagedList<News>> getNews();

    void removeOldNews();

    boolean isLoading();

    void refreshNews(LocalDateTime latest) throws IOException;

    void loadInvalidated() throws IOException;

    List<Integer> getSavedEpisodes();

    void updateSaved(int episodeId, boolean saved);

    void updateSaved(Collection<Integer> episodeIds, boolean saved);

    List<Integer> getToDeleteEpisodes();

    List<ClientDownloadedEpisode> downloadEpisodes(Collection<Integer> episodeIds) throws IOException;

    List<ToDownload> getToDownload();

    void addToDownload(ToDownload toDownload);

    void removeToDownloads(Collection<ToDownload> toDownloads);

    Collection<Integer> getExternalListItems(Integer externalListId);

    Collection<Integer> getListItems(Integer listId);

    List<Integer> getDownloadableEpisodes(Collection<Integer> mediaIds);

    List<Integer> getDownloadableEpisodes(Integer mediumId, int limit);

    LiveData<PagedList<DisplayRelease>> getDisplayEpisodes(ReleaseFilter filter);

    LiveData<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium);

    LiveData<List<MediaList>> getLists();

    LiveData<? extends MediaListSetting> getListSettings(int id, boolean isExternal);

    CompletableFuture<String> updateListName(MediaListSetting listSetting, String text);

    CompletableFuture<String> updateListMedium(MediaListSetting listSetting, int newMediumType);

    void updateToDownload(boolean add, ToDownload toDownload);

    LiveData<PagedList<MediumItem>> getAllMedia(Sorting sortings, String title, int medium, String author, LocalDateTime lastUpdate, int minCountEpisodes, int minCountReadEpisodes);

    LiveData<MediumSetting> getMediumSettings(int mediumId);

    CompletableFuture<String> updateMedium(MediumSetting mediumSettings);

    LiveData<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved);

    LiveData<List<MediumItem>> getMediumItems(int listId, boolean isExternal);

    void loadMediaInWaitSync() throws IOException;

    void addList(MediaList list, boolean autoDownload) throws IOException;

    boolean listExists(String listName);

    int countSavedUnreadEpisodes(Integer mediumId);

    List<Integer> getSavedEpisodes(int mediumId);

    Episode getEpisode(int episodeId);

    List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> ids);

    LiveData<PagedList<ReadEpisode>> getReadTodayEpisodes();

    LiveData<PagedList<MediumInWait>> getMediaInWaitBy(String filter, int mediumFilter, String hostFilter, Sorting sortings);

    LiveData<List<MediaList>> getInternLists();

    CompletableFuture<Boolean> moveMediaToList(int oldListId, int listId, Collection<Integer> ids);

    LiveData<List<MediumInWait>> getSimilarMediaInWait(MediumInWait mediumInWait);

    LiveData<List<SimpleMedium>> getMediaSuggestions(String title, int medium);

    LiveData<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium);

    CompletableFuture<Boolean> consumeMediumInWait(SimpleMedium selectedMedium, List<MediumInWait> mediumInWaits);

    CompletableFuture<Boolean> createMedium(MediumInWait mediumInWait, List<MediumInWait> mediumInWaits, MediaList list);

    CompletableFuture<Boolean> removeItemFromList(int listId, int mediumId);

    CompletableFuture<Boolean> removeItemFromList(int listId, Collection<Integer> mediumId);

    CompletableFuture<Boolean> moveItemFromList(int oldListId, int newListId, int mediumId);

    LiveData<List<MediaList>> getListSuggestion(String name);

    LiveData<Boolean> onDownloadable();

    void removeDanglingMedia(Collection<Integer> mediaIds);

    LiveData<List<MediumItem>> getAllDanglingMedia();

    CompletableFuture<Boolean> addMediumToList(int listId, Collection<Integer> ids);

    LiveData<PagedList<DisplayExternalUser>> getExternalUser();

    SpaceMedium getSpaceMedium(int mediumId);

    int getMediumType(Integer mediumId);

    List<String> getReleaseLinks(int episodeId);

    void syncWithTime() throws IOException;

    void syncUser() throws IOException;

    void clearLocalMediaData();

    LiveData<PagedList<NotificationItem>> getNotifications();

    void updateFailedDownloads(int episodeId);

    List<FailedEpisode> getFailedEpisodes(Collection<Integer> episodeIds);

    void addNotification(NotificationItem notification);

    SimpleEpisode getSimpleEpisode(int episodeId);

    SimpleMedium getSimpleMedium(Integer mediumId);

    LiveData<List<SimpleMedium>> getSimpleMedium();

    void clearNotifications();

    void clearFailEpisodes();

    void updateRead(int episodeId, boolean read) throws Exception;

    void updateRead(Collection<Integer> episodeIds, boolean read) throws Exception;

    void updateAllRead(int mediumId, boolean read) throws Exception;

    void updateReadWithHigherIndex(double episodeId, boolean read, int mediumId) throws Exception;

    void updateReadWithLowerIndex(double episodeId, boolean read, int mediumId) throws Exception;

    void deleteAllLocalEpisodes(int mediumId) throws IOException;

    void deleteLocalEpisodesWithLowerIndex(double episodeId, int mediumId) throws IOException;

    void deleteLocalEpisodesWithHigherIndex(double combiIndex, int mediumId) throws IOException;

    void deleteLocalEpisodes(Set<Integer> episodeId, int mediumId) throws IOException;

    void addProgressListener(Consumer<Integer> consumer);

    void removeProgressListener(Consumer<Integer> consumer);

    void addTotalWorkListener(Consumer<Integer> consumer);

    void removeTotalWorkListener(Consumer<Integer> consumer);

    int getLoadWorkerProgress();

    int getLoadWorkerTotalWork();

    void syncProgress();

    void updateDataStructure(List<Integer> mediaIds, List<Integer> partIds);

    void reloadLowerIndex(double combiIndex, int mediumId) throws Exception;

    void reloadHigherIndex(double combiIndex, int mediumId) throws Exception;

    void reload(Set<Integer> episodeId) throws Exception;

    void reloadAll(int mediumId) throws IOException;

    void downloadLowerIndex(double combiIndex, int mediumId) throws IOException;

    void downloadHigherIndex(double combiIndex, int mediumId) throws IOException;

    void download(Set<Integer> episodeId, int mediumId) throws IOException;

    void downloadAll(int mediumId) throws IOException;

    void updateProgress(int episodeId, float progress);

    List<SearchResponse> requestSearch(SearchRequest searchRequest);

    LiveData<List<Integer>> getListItems(Collection<Integer> listIds);
}
