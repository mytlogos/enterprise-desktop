package com.mytlogos.enterprisedesktop.background;


import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Sortings;
import io.reactivex.Observable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


/**
 * Interface for querying and deleting data.
 * It models a server-driven database.
 * <p>
 * To insert or update data, {@link #getPersister(Repository, LoadData)} is used to persist data
 * from the server.
 * </p>
 */
public interface DatabaseStorage {
    Observable<User> getUser();

    User getUserNow();

    Observable<HomeStats> getHomeStats();

    void deleteAllUser();

    ClientModelPersister getPersister(Repository repository, LoadData loadedData);

    DependantGenerator getDependantGenerator(LoadData loadedData);

    void deleteOldNews();

    boolean isLoading();

    void setLoading(boolean loading);

    LoadData getLoadData();

    Observable<PagedList<News>> getNews();

    List<Integer> getSavedEpisodes();

    List<Integer> getToDeleteEpisodes();

    void updateSaved(int episodeId, boolean saved);

    void updateSaved(Collection<Integer> episodeIds, boolean saved);

    List<ToDownload> getAllToDownloads();

    void removeToDownloads(Collection<ToDownload> toDownloads);

    Collection<Integer> getListItems(Integer listId);

    Observable<List<Integer>> getLiveListItems(Integer listId);

    Collection<Integer> getExternalListItems(Integer externalListId);

    Observable<List<Integer>> getLiveExternalListItems(Integer externalListId);

    List<Integer> getDownloadableEpisodes(Integer mediumId, int limit);

    List<Integer> getDownloadableEpisodes(Collection<Integer> mediumId);

    Observable<PagedList<DisplayRelease>> getDisplayEpisodes(int saved, int medium, int read, int minIndex, int maxIndex, boolean latestOnly);

    Observable<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium);

    Observable<List<MediaList>> getLists();

    void insertDanglingMedia(Collection<Integer> mediaIds);

    void removeDanglingMedia(Collection<Integer> mediaIds);

    Observable<? extends MediaListSetting> getListSetting(int id, boolean isExternal);

    MediaListSetting getListSettingNow(int id, boolean isExternal);

    void updateToDownload(boolean add, ToDownload toDownload);

    Observable<PagedList<MediumItem>> getAllMedia(Sortings sortings, String title, int medium, String author, LocalDateTime lastUpdate, int minCountEpisodes, int minCountReadEpisodes);

    Observable<MediumSetting> getMediumSettings(int mediumId);

    MediumSetting getMediumSettingsNow(int mediumId);

    Observable<PagedList<TocEpisode>> getToc(int mediumId, Sortings sortings, byte read, byte saved);

    Observable<List<MediumItem>> getMediumItems(int listId, boolean isExternal);

    boolean listExists(String listName);

    int countSavedEpisodes(Integer mediumId);

    List<Integer> getSavedEpisodes(int mediumId);

    Episode getEpisode(int episodeId);

    List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> ids);

    void updateProgress(Collection<Integer> episodeIds, float progress);

    Observable<PagedList<MediumInWait>> getMediaInWaitBy(String filter, int mediumFilter, String hostFilter, Sortings sortings);

    Observable<PagedList<ReadEpisode>> getReadTodayEpisodes();

    Observable<List<MediaList>> getInternLists();

    void addItemsToList(int listId, Collection<Integer> ids);

    Observable<List<MediumInWait>> getSimilarMediaInWait(MediumInWait mediumInWait);

    Observable<List<SimpleMedium>> getMediaSuggestions(String title, int medium);

    Observable<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium);

    Observable<List<MediaList>> getListSuggestion(String name);

    Observable<Boolean> onDownloadAble();

    void clearMediaInWait();

    void deleteMediaInWait(Collection<MediumInWait> toDelete);

    Observable<List<MediumItem>> getAllDanglingMedia();

    void removeItemFromList(int listId, int mediumId);

    void removeItemFromList(int listId, Collection<Integer> mediumId);

    void moveItemsToList(int oldListId, int listId, Collection<Integer> ids);

    Observable<PagedList<DisplayExternalUser>> getExternalUser();

    SpaceMedium getSpaceMedium(int mediumId);

    int getMediumType(Integer mediumId);

    List<String> getReleaseLinks(int episodeId);

    void clearLocalMediaData();

    Observable<PagedList<NotificationItem>> getNotifications();

    void updateFailedDownload(int episodeId);

    List<FailedEpisode> getFailedEpisodes(Collection<Integer> episodeIds);

    void addNotification(NotificationItem notification);

    SimpleEpisode getSimpleEpisode(int episodeId);

    SimpleMedium getSimpleMedium(int mediumId);

    void clearNotifications();

    void clearFailEpisodes();

    Collection<Integer> getAllEpisodes(int mediumId);

    void syncProgress();

    void updateDataStructure(List<Integer> mediaIds, List<Integer> partIds);

    List<Integer> getEpisodeIdsWithHigherIndex(double combiIndex, int mediumId, boolean read);

    List<Integer> getEpisodeIdsWithHigherIndex(double combiIndex, int mediumId);

    List<Integer> getEpisodeIdsWithLowerIndex(double combiIndex, int mediumId, boolean read);

    List<Integer> getEpisodeIdsWithLowerIndex(double combiIndex, int mediumId);

    Collection<Integer> getSavedEpisodeIdsWithHigherIndex(double combiIndex, int mediumId);

    Collection<Integer> getSavedEpisodeIdsWithLowerIndex(double combiIndex, int mediumId);

    void removeEpisodes(List<Integer> episodeIds);

    void removeParts(Collection<Integer> partIds);

    List<Integer> getReadEpisodes(Collection<Integer> episodeIds, boolean read);

    void insertEditEvent(EditEvent event);

    void insertEditEvent(Collection<EditEvent> events);

    List<? extends EditEvent> getEditEvents();

    void removeEditEvents(Collection<EditEvent> editEvents);
}
