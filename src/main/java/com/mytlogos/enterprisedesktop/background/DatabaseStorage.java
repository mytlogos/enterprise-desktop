package com.mytlogos.enterprisedesktop.background;


import com.mytlogos.enterprisedesktop.background.api.model.ClientStat;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.profile.DisplayEpisodeProfile;
import com.mytlogos.enterprisedesktop.tools.Sorting;

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
    LiveData<User> getUser();

    User getUserNow();

    LiveData<HomeStats> getHomeStats();

    void deleteAllUser();

    ClientModelPersister getPersister(Repository repository, LoadData loadedData);

    void deleteOldNews();

    boolean isLoading();

    void setLoading(boolean loading);

    LoadData getLoadData();

    LiveData<PagedList<News>> getNews();

    List<Integer> getSavedEpisodes();

    List<Integer> getToDeleteEpisodes();

    void updateSaved(int episodeId, boolean saved);

    void updateSaved(Collection<Integer> episodeIds, boolean saved);

    List<ToDownload> getAllToDownloads();

    void removeToDownloads(Collection<ToDownload> toDownloads);

    Collection<Integer> getListItems(Integer listId);

    LiveData<List<Integer>> getLiveListItems(Integer listId);

    Collection<Integer> getExternalListItems(Integer externalListId);

    LiveData<List<Integer>> getLiveExternalListItems(Integer externalListId);

    List<Integer> getDownloadableEpisodes(Integer mediumId, int limit);

    List<Integer> getDownloadableEpisodes(Collection<Integer> mediumId);

    LiveData<PagedList<DisplayRelease>> getDisplayEpisodes(DisplayEpisodeProfile filter);

    LiveData<PagedList<DisplayEpisode>> getDisplayEpisodesGrouped(int saved, int medium);

    LiveData<List<MediaList>> getLists();

    void insertDanglingMedia(Collection<Integer> mediaIds);

    void removeDanglingMedia(Collection<Integer> mediaIds);

    LiveData<? extends MediaListSetting> getListSetting(int id, boolean isExternal);

    MediaListSetting getListSettingNow(int id, boolean isExternal);

    void updateToDownload(boolean add, ToDownload toDownload);

    LiveData<PagedList<MediumItem>> getAllMedia(Sorting sortings, String title, int medium, String author, LocalDateTime lastUpdate, int minCountEpisodes, int minCountReadEpisodes);

    LiveData<List<MediumItem>> getAllMedia(Sorting sortings);

    LiveData<MediumSetting> getMediumSettings(int mediumId);

    MediumSetting getMediumSettingsNow(int mediumId);

    LiveData<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved);

    LiveData<List<MediumItem>> getMediumItems(int listId, boolean isExternal);

    LiveData<List<SimpleMedium>> getSimpleMediumItems(int listId, boolean external);

    boolean listExists(String listName);

    int countSavedEpisodes(Integer mediumId);

    List<Integer> getSavedEpisodes(int mediumId);

    Episode getEpisode(int episodeId);

    List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> ids);

    void updateProgress(Collection<Integer> episodeIds, float progress);

    LiveData<PagedList<MediumInWait>> getMediaInWaitBy(String filter, int mediumFilter, String hostFilter, Sorting sortings);

    LiveData<PagedList<ReadEpisode>> getReadTodayEpisodes();

    LiveData<List<MediaList>> getInternLists();

    void addItemsToList(int listId, Collection<Integer> ids);

    List<MediumInWait> getSimilarMediaInWait(MediumInWait mediumInWait);

    LiveData<List<SimpleMedium>> getMediaSuggestions(String title, int medium);

    LiveData<List<MediumInWait>> getMediaInWaitSuggestions(String title, int medium);

    LiveData<List<MediaList>> getListSuggestion(String name);

    LiveData<Boolean> onDownloadAble();

    void clearMediaInWait();

    void deleteMediaInWait(Collection<MediumInWait> toDelete);

    LiveData<List<MediumItem>> getAllDanglingMedia();

    void removeItemFromList(int listId, int mediumId);

    void removeItemFromList(int listId, Collection<Integer> mediumId);

    void moveItemsToList(int oldListId, int listId, Collection<Integer> ids);

    LiveData<PagedList<DisplayExternalUser>> getExternalUser();

    SpaceMedium getSpaceMedium(int mediumId);

    int getMediumType(Integer mediumId);

    List<String> getReleaseLinks(int episodeId);

    void clearLocalMediaData();

    LiveData<PagedList<NotificationItem>> getNotifications();

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

    ReloadStat checkReload(ClientStat.ParsedStat parsedStat);

    void deleteMedia(Collection<Integer> toDelete);

    void deleteParts(Collection<Integer> toDelete);

    void deleteEpisodes(Collection<Integer> toDelete);

    void deleteReleases(Collection<Release> toDelete);

    void deleteList(Collection<Integer> toDelete);

    void deleteExternalList(Collection<Integer> toDelete);

    void deleteExternalUser(Collection<Integer> toDelete);

    void clearAll();

    LiveData<List<SimpleMedium>> getSimpleMedium();

    LiveData<List<Integer>> getListItems(Collection<Integer> listIds);

    LiveData<List<String>> getToc(int mediumId);

    LiveData<List<MediaList>> getParentLists(int mediumId);

    void removeToc(int mediumId, String link);

    void removeMedium(int sourceId);
}
