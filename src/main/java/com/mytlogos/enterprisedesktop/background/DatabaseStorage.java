package com.mytlogos.enterprisedesktop.background;


import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Sorting;
import io.reactivex.Flowable;

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
    Flowable<User> getUser();

    User getUserNow();

    Flowable<HomeStats> getHomeStats();

    void deleteAllUser();

    ClientModelPersister getPersister(Repository repository, LoadData loadedData);

    DependantGenerator getDependantGenerator(LoadData loadedData);

    void deleteOldNews();

    boolean isLoading();

    void setLoading(boolean loading);

    LoadData getLoadData();

    Flowable<PagedList<News>> getNews();

    List<Integer> getSavedEpisodes();

    List<Integer> getToDeleteEpisodes();

    void updateSaved(int episodeId, boolean saved);

    void updateSaved(Collection<Integer> episodeIds, boolean saved);

    List<ToDownload> getAllToDownloads();

    void removeToDownloads(Collection<ToDownload> toDownloads);

    Collection<Integer> getListItems(Integer listId);

    Flowable<List<Integer>> getLiveListItems(Integer listId);

    Collection<Integer> getExternalListItems(Integer externalListId);

    Flowable<List<Integer>> getLiveExternalListItems(Integer externalListId);

    List<Integer> getDownloadableEpisodes(Integer mediumId, int limit);

    List<Integer> getDownloadableEpisodes(Collection<Integer> mediumId);

    Flowable<PagedList<DisplayRelease>> getDisplayEpisodes(int saved, int medium, int read, int minIndex, int maxIndex, boolean latestOnly);

    Flowable<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium);

    Flowable<List<MediaList>> getLists();

    void insertDanglingMedia(Collection<Integer> mediaIds);

    void removeDanglingMedia(Collection<Integer> mediaIds);

    Flowable<? extends MediaListSetting> getListSetting(int id, boolean isExternal);

    MediaListSetting getListSettingNow(int id, boolean isExternal);

    void updateToDownload(boolean add, ToDownload toDownload);

    Flowable<PagedList<MediumItem>> getAllMedia(Sorting sortings, String title, int medium, String author, LocalDateTime lastUpdate, int minCountEpisodes, int minCountReadEpisodes);

    Flowable<MediumSetting> getMediumSettings(int mediumId);

    MediumSetting getMediumSettingsNow(int mediumId);

    Flowable<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved);

    Flowable<List<MediumItem>> getMediumItems(int listId, boolean isExternal);

    boolean listExists(String listName);

    int countSavedEpisodes(Integer mediumId);

    List<Integer> getSavedEpisodes(int mediumId);

    Episode getEpisode(int episodeId);

    List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> ids);

    void updateProgress(Collection<Integer> episodeIds, float progress);

    Flowable<PagedList<MediumInWait>> getMediaInWaitBy(String filter, int mediumFilter, String hostFilter, Sorting sortings);

    Flowable<PagedList<ReadEpisode>> getReadTodayEpisodes();

    Flowable<List<MediaList>> getInternLists();

    void addItemsToList(int listId, Collection<Integer> ids);

    Flowable<List<MediumInWait>> getSimilarMediaInWait(MediumInWait mediumInWait);

    Flowable<List<SimpleMedium>> getMediaSuggestions(String title, int medium);

    Flowable<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium);

    Flowable<List<MediaList>> getListSuggestion(String name);

    Flowable<Boolean> onDownloadAble();

    void clearMediaInWait();

    void deleteMediaInWait(Collection<MediumInWait> toDelete);

    Flowable<List<MediumItem>> getAllDanglingMedia();

    void removeItemFromList(int listId, int mediumId);

    void removeItemFromList(int listId, Collection<Integer> mediumId);

    void moveItemsToList(int oldListId, int listId, Collection<Integer> ids);

    Flowable<PagedList<DisplayExternalUser>> getExternalUser();

    SpaceMedium getSpaceMedium(int mediumId);

    int getMediumType(Integer mediumId);

    List<String> getReleaseLinks(int episodeId);

    void clearLocalMediaData();

    Flowable<PagedList<NotificationItem>> getNotifications();

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
