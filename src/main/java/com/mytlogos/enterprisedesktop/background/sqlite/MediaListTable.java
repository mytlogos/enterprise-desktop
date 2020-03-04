package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediaList;
import com.mytlogos.enterprisedesktop.background.api.model.ClientPart;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.MediaList;
import com.mytlogos.enterprisedesktop.model.MediaListImpl;
import com.mytlogos.enterprisedesktop.model.MediumItem;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 *
 */
class MediaListTable extends AbstractTable {
    private final QueryBuilder<MediaList> insertMediaListQuery = new QueryBuilder<MediaList>(
            "INSERT OR IGNORE INTO media_list (listId, uuid, name, medium) VALUES (?,?,?,?)"
    ).setValueSetter((statement, mediaList) -> {
        statement.setInt(1, mediaList.getListId());
        statement.setString(2, mediaList.getUuid());
        statement.setString(3, mediaList.getName());
        statement.setInt(4, mediaList.getMedium());
    });

    private final QueryBuilder<MediaList> getListsQuery = new QueryBuilder<MediaList>(
            "SELECT media_list.*, (SELECT COUNT(listId) FROM list_medium WHERE list_medium.listId=media_list.listId) as count FROM media_list"
    ).setConverter(value -> {
        final int listId = value.getInt("listId");
        final String uuid = value.getString("uuid");
        final String name = value.getString("name");
        final int medium = value.getInt("medium");
        final int count = value.getInt("count");
        return new MediaListImpl(uuid, listId, name, medium, count);
    });

    private final QueryBuilder<Integer> getMediumItemsIdsQuery = new QueryBuilder<Integer>(
            "SELECT medium.mediumId FROM medium INNER JOIN media_list_medium " +
                    "ON media_list.mediumId=medium.mediumId " +
                    "WHERE listId=?"
    ).setConverter(value -> value.getInt(1));

    private final QueryBuilder<MediumItem> getMediumItemsQuery = new QueryBuilder<MediumItem>(
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
                    "ORDER BY title"
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
        final int currentReadEpisode = value.getInt(14);
        final int lastEpisode = value.getInt(15);
        final LocalDateTime lastUpdated = Formatter.parseLocalDateTime(value.getString(16));
        return new MediumItem(title, mediumId, author, artist, medium, stateTl, stateOrigin, countryOfOrigin, languageOfOrigin, language, series, universe, currentRead, currentReadEpisode, lastEpisode, lastUpdated);

    });

    MediaListTable() {
        super("media_list");
    }

    public LiveData<List<MediaList>> getLists() {
        return LiveData.create(this.getListsQuery::queryList);
    }

    public Collection<Integer> getMediumItemsIds(Integer listId) {
        return this.getMediumItemsIdsQuery.setValues(value -> value.setInt(1, listId)).queryListIgnoreError();
    }

    public void delete(Set<Integer> deletedLists) {
        this.executeDMLQuery(
                deletedLists,
                new QueryBuilder<Integer>("DELETE FROM media_list WHERE listId = ?")
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
        return "CREATE TABLE IF NOT EXISTS media_list (`listId` INTEGER NOT NULL, `uuid` TEXT, `name` TEXT, `medium` INTEGER NOT NULL, PRIMARY KEY(`listId`), FOREIGN KEY(`uuid`) REFERENCES `user`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT listId FROM media_list";
    }
}
