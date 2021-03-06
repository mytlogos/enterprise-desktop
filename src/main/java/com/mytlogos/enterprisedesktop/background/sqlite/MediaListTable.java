package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediaList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.MediaList;
import com.mytlogos.enterprisedesktop.model.MediaListImpl;
import com.mytlogos.enterprisedesktop.model.MediumItem;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 *
 */
class MediaListTable extends AbstractTable {
    private final QueryBuilder<MediaList> insertMediaListQuery;
    private final QueryBuilder<MediaList> getListsQuery;
    private final QueryBuilder<MediaList> getParentListsQuery;
    private final QueryBuilder<Integer> getMediumItemsIdsQuery;
    private final QueryBuilder<Integer> getListsMediaItemsIdsQuery;
    private final QueryBuilder<MediumItem> getMediumItemsQuery;
    private final QueryBuilder<SimpleMedium> getSimpleMediumItemsQuery;

    MediaListTable(ConnectionManager manager) {
        super("media_list", manager);
        this.getMediumItemsIdsQuery = new QueryBuilder<Integer>(
                "Select ListItems",
                "SELECT medium.mediumId FROM medium INNER JOIN list_medium " +
                        "ON list_medium.mediumId=medium.mediumId " +
                        "WHERE listId=?", getManager()
        ).setConverter(value -> value.getInt(1));
        this.getListsMediaItemsIdsQuery = new QueryBuilder<Integer>(
                "Select ListsItems",
                "SELECT medium.mediumId FROM medium INNER JOIN list_medium " +
                        "ON list_medium.mediumId=medium.mediumId " +
                        "WHERE listId $?", getManager()
        ).setDependencies(MediumTable.class, ListMediumJoinTable.class);
        this.getMediumItemsQuery = new QueryBuilder<MediumItem>(
                "Select MediumItems",
                "SELECT title, medium.mediumId, author, artist, medium, stateTL, stateOrigin, " +
                        "countryOfOrigin, languageOfOrigin, lang, series, universe, " +
                        "(" +
                        "   SELECT episodeId FROM episode " +
                        "   INNER JOIN part ON part.partId= episode.partId " +
                        "   WHERE mediumId=medium.mediumId " +
                        "   ORDER BY episode.combiIndex DESC " +
                        "   LIMIT 1" +
                        ") as currentRead," +
                        "(" +
                        "    SELECT episode.combiIndex \n" +
                        "    FROM episode\n" +
                        "    INNER JOIN part ON part.partId=episode.partId\n" +
                        "    WHERE part.mediumId=medium.mediumId AND episode.progress=1" +
                        "    ORDER BY episode.combiIndex DESC" +
                        "    LIMIT 1" +
                        ") as currentReadEpisode," +
                        "(" +
                        "   SELECT MAX(episode.combiIndex) FROM episode " +
                        "   INNER JOIN part ON part.partId=episode.partId  " +
                        "   WHERE part.mediumId=medium.mediumId" +
                        ") as lastEpisode , " +
                        "(" +
                        "   SELECT MAX(episode_release.releaseDate) FROM episode " +
                        "   INNER JOIN episode_release ON episode.episodeId=episode_release.episodeId " +
                        "   INNER JOIN part ON part.partId=episode.partId  " +
                        "   WHERE part.mediumId=medium.mediumId" +
                        ") as lastUpdated " +
                        "FROM medium INNER JOIN list_medium " +
                        "ON list_medium.mediumId=medium.mediumId " +
                        "WHERE listId=? " +
                        "ORDER BY title", getManager()
        ).setDependencies(
                EpisodeTable.class,
                PartTable.class,
                ReleaseTable.class,
                MediumTable.class,
                ListMediumJoinTable.class
        ).setConverter(value -> {
            final String title = value.getString(1);
            final int mediumId = value.getInt(2);
            final String author = value.getString(3);
            final String artist = value.getString(4);
            final int medium = value.getInt(5);
            final int stateTl = value.getInt(6);
            final int stateOrigin = value.getInt(7);
            final String countryOfOrigin = value.getString(8);
            final String languageOfOrigin = value.getString(9);
            final String language = value.getString(10);
            final String series = value.getString(11);
            final String universe = value.getString(12);
            final int currentRead = value.getInt(13);
            final double currentReadEpisode = value.getDouble(14);
            final int lastEpisode = value.getInt(15);
            final LocalDateTime lastUpdated = Formatter.parseLocalDateTime(value.getString(16));
            return new MediumItem(title, mediumId, author, artist, medium, stateTl, stateOrigin, countryOfOrigin, languageOfOrigin, language, series, universe, currentRead, currentReadEpisode, lastEpisode, lastUpdated);
        });
        this.getSimpleMediumItemsQuery = new QueryBuilder<SimpleMedium>(
                "Select MediumItems",
                "SELECT title, medium.mediumId, medium " +
                        "FROM medium INNER JOIN list_medium " +
                        "ON list_medium.mediumId=medium.mediumId " +
                        "WHERE listId=? " +
                        "ORDER BY title", getManager()
        ).setDependencies(
                MediumTable.class,
                ListMediumJoinTable.class
        ).setConverter(value -> {
            final String title = value.getString(1);
            final int mediumId = value.getInt(2);
            final int medium = value.getInt(3);
            return new SimpleMedium(mediumId, title, medium);
        });
        this.getParentListsQuery = new QueryBuilder<MediaList>(
                "Select ParentList",
                "SELECT media_list.*, (SELECT COUNT(listId) FROM list_medium WHERE list_medium.listId=media_list.listId) as count " +
                        "FROM media_list " +
                        "WHERE media_list.listId IN " +
                        "(" +
                        "SELECT listId FROM list_medium WHERE mediumId = ?" +
                        ");", getManager()
        )
                .setDependencies(MediaListTable.class, ListMediumJoinTable.class)
                .setConverter(value -> {
                    final int listId = value.getInt("listId");
                    final String uuid = value.getString("uuid");
                    final String name = value.getString("name");
                    final int medium = value.getInt("medium");
                    final int count = value.getInt("count");
                    return new MediaListImpl(uuid, listId, name, medium, count);
                });
        this.getListsQuery = new QueryBuilder<MediaList>(
                "Select List",
                "SELECT media_list.*, (SELECT COUNT(listId) FROM list_medium WHERE list_medium.listId=media_list.listId) as count FROM media_list", getManager()
        ).setConverter(value -> {
            final int listId = value.getInt("listId");
            final String uuid = value.getString("uuid");
            final String name = value.getString("name");
            final int medium = value.getInt("medium");
            final int count = value.getInt("count");
            return new MediaListImpl(uuid, listId, name, medium, count);
        });
        this.insertMediaListQuery = new QueryBuilder<MediaList>(
                "Insert List",
                "INSERT OR IGNORE INTO media_list (listId, uuid, name, medium) VALUES (?,?,?,?)", getManager()
        ).setValueSetter((statement, mediaList) -> {
            statement.setInt(1, mediaList.getListId());
            statement.setString(2, mediaList.getUuid());
            statement.setString(3, mediaList.getName());
            statement.setInt(4, mediaList.getMedium());
        });
    }

    public LiveData<List<MediaList>> getLists() {
        return LiveData.create(this.getListsQuery::queryList, Arrays.asList(ListMediumJoinTable.class, MediaListTable.class));
    }

    public Collection<Integer> getMediumItemsIds(Integer listId) {
        return this.getMediumItemsIdsQuery.setValues(value -> value.setInt(1, listId)).queryListIgnoreError();
    }

    public void delete(Set<Integer> deletedLists) {
        this.executeDMLQuery(
                deletedLists,
                new QueryBuilder<Integer>("Delete ListIds", "DELETE FROM media_list WHERE listId = ?", getManager())
                        .setValueSetter((preparedStatement, listId) -> preparedStatement.setInt(1, listId))
        );
    }

    public void update(List<ClientMediaList> update) {
        final HashMap<String, Function<ClientMediaList, ?>> attrMap = new HashMap<>();
        attrMap.put("name", (StringProducer<ClientMediaList>) ClientMediaList::getName);
        attrMap.put("medium", (IntProducer<ClientMediaList>) ClientMediaList::getMedium);

        final Map<String, Function<ClientMediaList, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("listId", (IntProducer<ClientMediaList>) ClientMediaList::getId);
        this.update(update, "media_list", attrMap, keyExtractors);
    }

    public LiveData<List<Integer>> getMediumItemsIds(Collection<Integer> listIds) {
        return this.getListsMediaItemsIdsQuery.setQueryIn(listIds, QueryBuilder.Type.INT).setConverter(value -> value.getInt(1)).selectInLiveDataList();
    }

    public LiveData<List<SimpleMedium>> getSimpleMediumItems(int listId) {
        return this.getSimpleMediumItemsQuery.setValues(value -> value.setInt(1, listId)).queryLiveDataList();
    }

    public LiveData<List<MediaList>> getParentLists(int mediumId) {
        return this.getParentListsQuery.setValues(value -> value.setInt(1, mediumId)).queryLiveDataList();
    }

    LiveData<List<MediumItem>> getMediumItems(int listId) {
        return this.getMediumItemsQuery.setValues(value -> value.setInt(1, listId)).queryLiveDataList();
    }

    void insert(MediaList mediaList) {
        this.executeDMLQuery(mediaList, this.insertMediaListQuery);
    }

    void insert(Collection<? extends MediaList> mediaLists) {
        this.executeDMLQuery(mediaLists, this.insertMediaListQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS media_list " +
                "(" +
                "`listId` INTEGER NOT NULL, " +
                "`uuid` TEXT, " +
                "`name` TEXT, " +
                "`medium` INTEGER NOT NULL, " +
                "PRIMARY KEY(`listId`), " +
                "FOREIGN KEY(`uuid`) REFERENCES `user`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE " +
                ")";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT listId FROM media_list";
    }
}
