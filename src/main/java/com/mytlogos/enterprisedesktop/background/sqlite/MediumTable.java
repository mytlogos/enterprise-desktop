package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.Medium;
import com.mytlogos.enterprisedesktop.model.MediumSetting;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 *
 */
class MediumTable extends AbstractTable {
    private final QueryBuilder<Medium> insertMediumQuery = new QueryBuilder<Medium>(
            "INSERT OR IGNORE INTO medium (mediumId, currentRead, countryOfOrigin, languageOfOrigin, author, title, medium, artist, lang, stateOrigin, stateTl, series, universe) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
    ).setValueSetter((statement, medium) -> {
        statement.setInt(1, medium.getMediumId());
        statement.setInt(2, medium.getCurrentRead());
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
                    "WHERE medium.mediumId=?"
    ).setConverter(value -> {
        final int mediumId = value.getInt(1);
        final String title = value.getString(2);
        final String author = value.getString(3);
        final String artist = value.getString(4);
        final int medium = value.getInt(5);
        final int stateTl = value.getInt(6);
        final int stateOrigin = value.getInt(7);
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
            "SELECT mediumId, title, medium FROM medium WHERE mediumId=?"
    ).setConverter(value -> new SimpleMedium(
            value.getInt(1),
            value.getString(2),
            value.getInt(3)
    ));

    public Flowable<MediumSetting> getSettings(int mediumId) {
        return Flowable.create(emitter -> {
            final MediumSetting setting = this.getSettingsQuery.setValues(value -> value.setInt(1, mediumId)).query();
            emitter.onNext(setting);
        }, BackpressureStrategy.LATEST);
    }

    public SimpleMedium getSimpleMedium(int mediumId) {
        return this.getSimpleMediumQuery.setValues(value -> value.setInt(1, mediumId)).query();
    }

    void insert(Medium medium) {
        this.executeDMLQuery(medium, this.insertMediumQuery);
    }

    void insert(Collection<? extends Medium> media) {
        this.executeDMLQuery(media, this.insertMediumQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS medium (`mediumId` INTEGER NOT NULL, `currentRead` INTEGER, `countryOfOrigin` TEXT, `languageOfOrigin` TEXT, `author` TEXT, `title` TEXT, `medium` INTEGER NOT NULL, `artist` TEXT, `lang` TEXT, `stateOrigin` INTEGER NOT NULL, `stateTL` INTEGER NOT NULL, `series` TEXT, `universe` TEXT, PRIMARY KEY(`mediumId`), FOREIGN KEY(`currentRead`) REFERENCES `episode`(`episodeId`) ON UPDATE NO ACTION ON DELETE SET NULL )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT mediumId FROM medium";
    }
}
