package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.ListUser;
import com.mytlogos.enterprisedesktop.background.api.model.ClientExternalMediaList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.ExternalMediaList;
import com.mytlogos.enterprisedesktop.model.ExternalMediaListImpl;
import com.mytlogos.enterprisedesktop.model.MediumItem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 *
 */
class ExternalMediaListTable extends AbstractTable {
    private final QueryBuilder<ExternalMediaList> insertExternalMediaListQuery = new QueryBuilder<ExternalMediaList>(
            "INSERT OR IGNORE INTO external_media_list (uuid,externalListId, name, medium, url) VALUES (?,?,?,?,?)"
    ).setValueSetter((statement, value) -> {
        statement.setString(1, value.getUuid());
        statement.setInt(2, value.getListId());
        statement.setString(3, value.getName());
        statement.setInt(4, value.getMedium());
        statement.setString(5, value.getUrl());
    });

    private QueryBuilder<ExternalMediaList> getListsQuery = new QueryBuilder<ExternalMediaList>(
            "SELECT external_media_list.*, " +
                    "(SELECT COUNT(listId) FROM external_list_medium WHERE external_list_medium.listId=external_media_list.externalListId) " +
                    "as count " +
                    "FROM external_media_list"
    ).setConverter(value -> {
        final int listId = value.getInt("externalListId");
        final String uuid = value.getString("uuid");
        final String name = value.getString("name");
        final int medium = value.getInt("medium");
        final String url = value.getString("url");
        final int count = value.getInt("count");
        return new ExternalMediaListImpl(uuid, listId, name, medium, url, count);
    });

    private QueryBuilder<MediumItem> getMediumItems = new QueryBuilder<MediumItem>(
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
                    "FROM medium INNER JOIN external_list_medium " +
                    "ON external_list_medium.mediumId=medium.mediumId " +
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

    ExternalMediaListTable() {
        super("external_media_list");
    }

    public LiveData<List<ExternalMediaList>> getLists() {
        return this.getListsQuery.queryLiveDataList();
    }

    public List<ListUser> getListUser() {
        return new QueryBuilder<ListUser>("SELECT externalListId as listId, uuid FROM external_media_list")
                .setConverter(value -> new ListUser(value.getInt(1), value.getString(2)))
                .queryListIgnoreError();
    }

    public void delete(Set<Integer> deletedExLists) {
        this.executeDMLQuery(
                deletedExLists,
                new QueryBuilder<Integer>("DELETE FROM external_media_list WHERE externalListId = ?")
                        .setValueSetter((preparedStatement, listId) -> preparedStatement.setInt(1, listId))
        );
    }

    public void update(List<ClientExternalMediaList> update) {
        final HashMap<String, Function<ClientExternalMediaList, ?>> attrMap = new HashMap<>();
        attrMap.put("url", (StringProducer<ClientExternalMediaList>) ClientExternalMediaList::getUrl);
        attrMap.put("name", (StringProducer<ClientExternalMediaList>) ClientExternalMediaList::getName);
        attrMap.put("medium", (IntProducer<ClientExternalMediaList>) ClientExternalMediaList::getMedium);

        final Map<String, Function<ClientExternalMediaList, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("externalListId", (IntProducer<ClientExternalMediaList>) ClientExternalMediaList::getId);
        this.update(update, "external_media_list", attrMap, keyExtractors);
    }

    LiveData<List<MediumItem>> getMediumItems(int listId) {
        return this.getMediumItems.setValues(value -> value.setInt(1, listId)).queryLiveDataList();
    }

    void insert(ExternalMediaList externalMediaList) {
        this.insert(Collections.singleton(externalMediaList));
    }

    void insert(Collection<? extends ExternalMediaList> externalMediaLists) {
        this.executeDMLQuery(externalMediaLists, this.insertExternalMediaListQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS external_media_list (`uuid` TEXT, `externalListId` INTEGER NOT NULL, `name` TEXT, `medium` INTEGER NOT NULL, `url` TEXT, PRIMARY KEY(`externalListId`), FOREIGN KEY(`uuid`) REFERENCES `externalUser`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT externalListId FROM external_media_list";
    }
}
