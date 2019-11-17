package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.ExternalMediaList;
import com.mytlogos.enterprisedesktop.model.ExternalMediaListImpl;
import com.mytlogos.enterprisedesktop.model.MediumItem;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class ExternalMediaListTable extends AbstractTable {
    private final PreparedQuery<ExternalMediaList> insertExternalMediaListQuery = new PreparedQuery<ExternalMediaList>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO external_media_list (uuid,externalListId, name, medium, url) VALUES (?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, ExternalMediaList value) throws SQLException {
            statement.setString(1, value.getUuid());
            statement.setInt(2, value.getListId());
            statement.setString(3, value.getName());
            statement.setInt(4, value.getMedium());
            statement.setString(5, value.getUrl());
        }
    };

    public Flowable<List<ExternalMediaList>> getLists() {
        return Flowable.create(emitter -> {
            final List<ExternalMediaList> mediaLists = this.selectList(
                    "SELECT external_media_list.*, " +
                            "(SELECT COUNT(listId) FROM external_list_medium WHERE external_list_medium.listId=external_media_list.externalListId) as count " +
                            "FROM external_media_list",
                    value -> {
                        final int listId = value.getInt("externalListId");
                        final String uuid = value.getString("uuid");
                        final String name = value.getString("name");
                        final int medium = value.getInt("medium");
                        final String url = value.getString("url");
                        final int count = value.getInt("count");
                        return new ExternalMediaListImpl(uuid, listId, name, medium, url, count);
                    }
            );
            emitter.onNext(mediaLists);
        }, BackpressureStrategy.LATEST);
    }

    Flowable<List<MediumItem>> getMediumItems(int listId) {
        return Flowable.create(emitter -> {
            final List<MediumItem> items = this.selectList(
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
                            "ORDER BY title",
                    value -> value.setInt(1, listId),
                    value -> {
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
                    }
            );
            emitter.onNext(items);
        }, BackpressureStrategy.LATEST);
    }

    void insert(ExternalMediaList externalMediaList) {
        this.execute(externalMediaList, this.insertExternalMediaListQuery);
    }

    void insert(Collection<? extends ExternalMediaList> externalMediaLists) {
        this.execute(externalMediaLists, this.insertExternalMediaListQuery);
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
