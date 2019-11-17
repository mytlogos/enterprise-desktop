package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.Medium;
import com.mytlogos.enterprisedesktop.model.MediumSetting;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 *
 */
class MediumTable extends AbstractTable {
    private final PreparedQuery<Medium> insertMediumQuery = new PreparedQuery<Medium>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO medium (mediumId, currentRead, countryOfOrigin, languageOfOrigin, author, title, medium, artist, lang, stateOrigin, stateTl, series, universe) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, Medium value) throws SQLException {
            statement.setInt(1, value.getMediumId());
            statement.setInt(2, value.getCurrentRead());
            statement.setString(3, value.getCountryOfOrigin());
            statement.setString(4, value.getLanguageOfOrigin());
            statement.setString(5, value.getAuthor());
            statement.setString(6, value.getTitle());
            statement.setInt(7, value.getMedium());
            statement.setString(8, value.getArtist());
            statement.setString(9, value.getLang());
            statement.setInt(10, value.getStateOrigin());
            statement.setInt(11, value.getStateTL());
            statement.setString(12, value.getSeries());
            statement.setString(13, value.getUniverse());
        }
    };

    public Flowable<MediumSetting> getSettings(int mediumId) {
        return Flowable.create(emitter -> {
            final MediumSetting setting = this.selectSingle(
                    "SELECT title, author, artist, medium, stateTL, stateOrigin, " +
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
                            "WHERE medium.mediumId=?",
                    preparedStatement -> preparedStatement.setInt(1, mediumId),
                    resultSet -> {
                        final String title = resultSet.getString(1);
                        final String author = resultSet.getString(2);
                        final String artist = resultSet.getString(3);
                        final int medium = resultSet.getInt(4);
                        final int stateTl = resultSet.getInt(5);
                        final int stateOrigin = resultSet.getInt(6);
                        final String countryOrigin = resultSet.getString(7);
                        final String langOrigin = resultSet.getString(8);
                        final String language = resultSet.getString(9);
                        final String series = resultSet.getString(10);
                        final String universe = resultSet.getString(11);
                        final boolean toDownload = resultSet.getBoolean(12);
                        final int currentReadIndex = resultSet.getInt(13);
                        final int currentReadId = resultSet.getInt(14);
                        final int lastEpisodeIndex = resultSet.getInt(15);
                        final LocalDateTime lastUpdated = Formatter.parseLocalDateTime(resultSet.getString(16));

                        return new MediumSetting(
                                title, mediumId, author, artist, medium, stateTl, stateOrigin, countryOrigin,
                                langOrigin, language, series, universe, currentReadId, currentReadIndex,
                                lastEpisodeIndex, lastUpdated, toDownload
                        );
                    });
            emitter.onNext(setting);
        }, BackpressureStrategy.LATEST);
    }

    public SimpleMedium getSimpleMedium(int mediumId) {
        try {
            return this.selectSingle(
                    "SELECT mediumId, title, medium FROM medium WHERE mediumId=?",
                    value -> value.setInt(1, mediumId),
                    value -> new SimpleMedium(
                            value.getInt(1),
                            value.getString(2),
                            value.getInt(3)
                    )
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    void insert(Medium medium) {
        this.execute(medium, this.insertMediumQuery);
    }

    void insert(Collection<? extends Medium> media) {
        this.execute(media, this.insertMediumQuery);
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
