package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMedium;
import com.mytlogos.enterprisedesktop.background.sqlite.QueryBuilder.OrderBy;
import com.mytlogos.enterprisedesktop.background.sqlite.QueryBuilder.SqlOrder;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.AllSortings;
import com.mytlogos.enterprisedesktop.tools.Sorting;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 *
 */
class MediumTable extends AbstractTable {
    private final QueryBuilder<Medium> insertMediumQuery = new QueryBuilder<Medium>(
            "Insert Medium",
            "INSERT OR IGNORE INTO medium (mediumId, currentRead, countryOfOrigin, languageOfOrigin, author, title, medium, artist, lang, stateOrigin, stateTl, series, universe) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", getManager()
    ).setValueSetter((statement, medium) -> {
        statement.setInt(1, medium.getMediumId());
        final Integer currentRead = medium.getCurrentRead();

//        if (currentRead == null) {
        statement.setNull(2, Types.INTEGER);
//        } else {
//            statement.setInt(2, currentRead);
//        }
        statement.setString(3, medium.getCountryOfOrigin());
        statement.setString(4, medium.getLanguageOfOrigin());
        statement.setString(5, medium.getAuthor());
        statement.setString(6, medium.getTitle());
        statement.setInt(7, medium.getMedium());
        statement.setString(8, medium.getArtist());
        statement.setString(9, medium.getLang());
        statement.setInt(10, medium.getStateOrigin());
        statement.setInt(11, medium.getStateTL());
        statement.setString(12, medium.getSeries());
        statement.setString(13, medium.getUniverse());
    });

    private final QueryBuilder<MediumSetting> getSettingsQuery = new QueryBuilder<MediumSetting>(
            "Select MediumSetting",
            "SELECT medium.mediumId, title, author, artist, medium, stateTL, stateOrigin, " +
                    "countryOfOrigin, languageOfOrigin, lang, series, universe, toDownload, " +
                    "(" +
                    "    SELECT episode.combiIndex \n" +
                    "    FROM episode\n" +
                    "    INNER JOIN part ON part.partId=episode.partId\n" +
                    "    WHERE part.mediumId=medium.mediumId AND episode.progress=1" +
                    "    ORDER BY episode.combiIndex DESC" +
                    "    LIMIT 1" +
                    ") as currentReadEpisode," +
                    "(" +
                    "   SELECT episodeId FROM episode " +
                    "   INNER JOIN part ON part.partId= episode.partId " +
                    "   WHERE mediumId=medium.mediumId " +
                    "   ORDER BY episode.combiIndex DESC " +
                    "   LIMIT 1" +
                    ") as currentRead, " +
                    "(" +
                    "   SELECT MAX(episode.combiIndex) FROM episode " +
                    "   INNER JOIN part ON part.partId=episode.partId  " +
                    "   WHERE part.mediumId=medium.mediumId" +
                    ") as lastEpisode, " +
                    "(" +
                    "   SELECT MAX(episode_release.releaseDate) FROM episode " +
                    "   INNER JOIN episode_release ON episode.episodeId=episode_release.episodeId " +
                    "   INNER JOIN part ON part.partId=episode.partId  " +
                    "   WHERE part.mediumId=medium.mediumId" +
                    ") as lastUpdated " +
                    "FROM medium " +
                    "LEFT JOIN " +
                    "(SELECT mediumId,1 as toDownload FROM todownload WHERE mediumId > 0) " +
                    "as todownload ON todownload.mediumId=medium.mediumId " +
                    "WHERE medium.mediumId=?", getManager()
    ).setConverter(value -> {
        final int mediumId = value.getInt(1);
        final String title = value.getString(2);
        final String author = value.getString(3);
        final String artist = value.getString(4);
        final int medium = value.getInt(5);
        final ReleaseState stateTl = ReleaseState.getInstance(value.getInt(6));
        final ReleaseState stateOrigin = ReleaseState.getInstance(value.getInt(7));
        final String countryOrigin = value.getString(8);
        final String langOrigin = value.getString(9);
        final String language = value.getString(10);
        final String series = value.getString(11);
        final String universe = value.getString(12);
        final boolean toDownload = value.getBoolean(13);
        final int currentReadIndex = value.getInt(14);
        final int currentReadId = value.getInt(15);
        final int lastEpisodeIndex = value.getInt(16);
        final LocalDateTime lastUpdated = Formatter.parseLocalDateTime(value.getString(17));

        return new MediumSetting(
                title, mediumId, author, artist, medium, stateTl, stateOrigin, countryOrigin,
                langOrigin, language, series, universe, currentReadId, currentReadIndex,
                lastEpisodeIndex, lastUpdated, toDownload
        );
    });

    private final QueryBuilder<SimpleMedium> getSimpleMediumQuery = new QueryBuilder<SimpleMedium>(
            "Select SimpleMedium",
            "SELECT mediumId, title, medium FROM medium WHERE mediumId=?", getManager()
    ).setConverter(value -> new SimpleMedium(
            value.getInt(1),
            value.getString(2),
            value.getInt(3)
    ));
    private final QueryBuilder<SimpleMedium> getAllSimpleMediumQuery = new QueryBuilder<SimpleMedium>(
            "Select SimpleMedia",
            "SELECT mediumId, title, medium FROM medium", getManager()
    ).setConverter(value -> new SimpleMedium(
            value.getInt(1),
            value.getString(2),
            value.getInt(3)
    ));
    private final QueryBuilder<MediumItem> getAllMediumAscQuery = new QueryBuilder<MediumItem>(
            "Select AllMedia",
            """
            SELECT title, medium.mediumId, author, artist, medium, stateTL, stateOrigin, 
            countryOfOrigin, languageOfOrigin, lang, series, universe, cr.currentRead,
            currentReadEpisode, lastEpisode, lastUpdated
            FROM medium
            LEFT JOIN
            (
                SELECT MAX(episode.combiIndex) as currentReadEpisode, episodeId as currentRead, mediumId
                FROM episode
                INNER JOIN part ON part.partId=episode.partId
                WHERE episode.progress=1
                GROUP BY mediumId
            ) as cr
            ON medium.mediumId=cr.mediumId
            LEFT JOIN
            (
                SELECT MAX(episode.combiIndex) as lastEpisode, mediumId FROM episode 
                INNER JOIN part ON part.partId=episode.partId
                GROUP BY mediumId
            ) as le
            ON medium.mediumId=le.mediumId
            LEFT JOIN
            (
                SELECT MAX(episode_release.releaseDate) as lastUpdated, mediumId FROM episode 
                INNER JOIN episode_release ON episode.episodeId=episode_release.episodeId 
                INNER JOIN part ON part.partId=episode.partId  
                GROUP BY mediumId
            ) as lu
            ON medium.mediumId=lu.mediumId
            """, getManager()
    )
            .setDependencies(MediumTable.class)
            .setConverter(value -> {
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
                final double currentReadEpisode = value.getInt(14);
                final int lastEpisode = value.getInt(15);
                final LocalDateTime lastUpdated = Formatter.parseLocalDateTime(value.getString(16));
                return new MediumItem(title, mediumId, author, artist, medium, stateTl, stateOrigin, countryOfOrigin, languageOfOrigin, language, series, universe, currentRead, currentReadEpisode, lastEpisode, lastUpdated);
            });

    private final QueryBuilder<Integer> deleteQuery = new QueryBuilder<Integer>(
            "Delete Medium",
            "DELETE FROM medium WHERE mediumId = ?;", getManager()
    ).setValueSetter((statement, mediumId) -> statement.setInt(1, mediumId));;

    MediumTable(ConnectionManager manager) {
        super("medium", manager);
    }

    public LiveData<MediumSetting> getSettings(int mediumId) {
        return this.getSettingsQuery.setValues(value -> value.setInt(1, mediumId)).queryLiveDataPassError();
    }

    public SimpleMedium getSimpleMedium(int mediumId) {
        return this.getSimpleMediumQuery.setValues(value -> value.setInt(1, mediumId)).query();
    }

    public void update(List<ClientMedium> update) {
        final HashMap<String, Function<ClientMedium, ?>> attrMap = new HashMap<>();
        attrMap.put("title", (StringProducer<ClientMedium>) ClientMedium::getTitle);
        attrMap.put("medium", (IntProducer<ClientMedium>) ClientMedium::getMedium);
        attrMap.put("artist", (StringProducer<ClientMedium>) ClientMedium::getArtist);
        attrMap.put("author", (StringProducer<ClientMedium>) ClientMedium::getAuthor);
        attrMap.put("countryOfOrigin", (StringProducer<ClientMedium>) ClientMedium::getCountryOfOrigin);
        attrMap.put("lang", (StringProducer<ClientMedium>) ClientMedium::getLang);
        attrMap.put("languageOfOrigin", (StringProducer<ClientMedium>) ClientMedium::getLanguageOfOrigin);
        attrMap.put("series", (StringProducer<ClientMedium>) ClientMedium::getSeries);
        attrMap.put("stateOrigin", (IntProducer<ClientMedium>) ClientMedium::getStateOrigin);
        attrMap.put("stateTL", (IntProducer<ClientMedium>) ClientMedium::getStateTL);
        attrMap.put("universe", (StringProducer<ClientMedium>) ClientMedium::getUniverse);

        final Map<String, Function<ClientMedium, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("mediumId", (IntProducer<ClientMedium>) ClientMedium::getId);
        this.update(update, "medium", attrMap, keyExtractors);
    }

    public LiveData<List<SimpleMedium>> getSimpleMedium() {
        return this.getAllSimpleMediumQuery.queryLiveDataList();
    }

    private OrderBy getOrder(Sorting sortings) {
        int sorting = sortings.getSortValue();
        SqlOrder order = sorting > 0 ? SqlOrder.ASC : SqlOrder.DESC;

        sorting = sorting > 0 ? sorting : -sorting;
        String column = null;

        if (sorting == AllSortings.TITLE_AZ.sortValue) {
            column = "title";
        } else if (sorting == AllSortings.MEDIUM.sortValue) {
            column = "medium";
        } else if (sorting == AllSortings.AUTHOR_ASC.sortValue) {
            column = "author";
        } else if (sorting == AllSortings.NUMBER_EPISODE_ASC.sortValue) {
            column = "lastEpisode";
        } else if (sorting == AllSortings.NUMBER_EPISODE_READ_ASC.sortValue) {
            column = "currentReadEpisode";
        } else if (sorting == AllSortings.LAST_UPDATE_ASC.sortValue) {
            column = "lastUpdated";
        } else {
            throw new IllegalArgumentException("Unknown Sorting: " + sortings);
        }
        return new OrderBy(column, order);
    }

    public LiveData<List<MediumItem>> getAllMedia(Sorting sortings) {
        QueryBuilder<MediumItem> queryBuilder = this.getAllMediumAscQuery.orderBy(getOrder(sortings));
        return queryBuilder.queryLiveDataList();
    }

    public void delete(int mediumId) {
        this.executeDMLQuery(mediumId, this.deleteQuery);
    }

    public void delete(Collection<Integer> mediaIds) {
        this.executeDMLQuery(mediaIds, this.deleteQuery);
    }

    void insert(Medium medium) {
        this.executeDMLQuery(medium, this.insertMediumQuery);
    }

    void insert(Collection<? extends Medium> media) {
        this.executeDMLQuery(media, this.insertMediumQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS medium " +
                "(" +
                "`mediumId` INTEGER NOT NULL, " +
                "`currentRead` INTEGER, " +
                "`countryOfOrigin` TEXT, " +
                "`languageOfOrigin` TEXT, " +
                "`author` TEXT, " +
                "`title` TEXT, " +
                "`medium` INTEGER NOT NULL, " +
                "`artist` TEXT, " +
                "`lang` TEXT, " +
                "`stateOrigin` INTEGER NOT NULL, " +
                "`stateTL` INTEGER NOT NULL, " +
                "`series` TEXT, " +
                "`universe` TEXT, " +
                "PRIMARY KEY(`mediumId`), " +
                "FOREIGN KEY(`currentRead`) REFERENCES `episode`(`episodeId`) ON UPDATE NO ACTION ON DELETE SET NULL " +
                ")";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT mediumId FROM medium";
    }
}
