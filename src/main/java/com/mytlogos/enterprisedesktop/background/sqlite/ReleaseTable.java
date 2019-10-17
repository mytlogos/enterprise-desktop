package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.DisplayRelease;
import com.mytlogos.enterprisedesktop.model.Release;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class ReleaseTable extends AbstractTable {
    private final PreparedQuery<Release> insertReleaseQuery = new PreparedQuery<Release>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO episode_release (episodeId, title, url, releaseDate, locked) VALUES (?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, Release value) throws SQLException {
            statement.setInt(1, value.getEpisodeId());
            statement.setString(2, value.getTitle());
            statement.setString(3, value.getUrl());
            statement.setString(4, Formatter.isoFormat(value.getReleaseDate()));
            statement.setBoolean(5, value.isLocked());
        }
    };

    Observable<PagedList<DisplayRelease>> getReleases(int saved, int medium, int read, int minIndex, int maxIndex) {
        return Observable.create(emitter -> {
            try {
                final List<DisplayRelease> releases = this.selectList(
                        "SELECT episode.episodeId, episode.saved, episode.partialIndex, episode.totalIndex, \n" +
                                "medium.mediumId, medium.title as mediumTitle, \n" +
                                "CASE episode.progress WHEN 1 THEN 1 ELSE 0 END as read, \n " +
                                "episode_release.releaseDate, episode_release.title, episode_release.url, episode_release.locked " +
                                "FROM episode_release \n" +
                                "INNER JOIN episode ON episode_release.episodeId = episode.episodeId \n" +
                                "INNER JOIN part ON episode.partId = part.partId \n" +
                                "INNER JOIN medium ON part.mediumId = medium.mediumId \n" +
                                "WHERE CASE ? " +
                                "WHEN 0 THEN progress < 1\n" +
                                "WHEN 1 THEN progress = 1\n" +
                                "ELSE 1 END " +
                                "AND (? = 0 OR (? & medium) > 0)\n" +
                                "AND (? < 0 OR saved=?)\n" +
                                "AND (? < 0 OR episode.combiIndex >= ?)\n" +
                                "AND (? < 0 OR episode.combiIndex <= ?)\n" +
                                "ORDER BY episode_release.releaseDate DESC, episode.combiIndex DESC",
                        value -> {
                            value.setInt(1, read);
                            value.setInt(2, medium);
                            value.setInt(3, medium);
                            value.setInt(4, saved);
                            value.setInt(5, saved);
                            value.setInt(6, minIndex);
                            value.setInt(7, minIndex);
                            value.setInt(8, maxIndex);
                            value.setInt(9, maxIndex);
                        },
                        value -> {
                            final int episodeId = value.getInt(1);
                            final boolean dbSaved = value.getBoolean(2);
                            final int partialIndex = value.getInt(3);
                            final int totalIndex = value.getInt(4);
                            final int mediumId = value.getInt(5);
                            final String mediumTitle = value.getString(6);
                            final boolean dbRead = value.getBoolean(7);
                            final LocalDateTime releaseDate = Formatter.parseLocalDateTime(value.getString(8));
                            final String episodeTitle = value.getString(9);
                            final String url = value.getString(10);
                            final boolean locked = value.getBoolean(11);

                            return new DisplayRelease(episodeId, mediumId, mediumTitle, totalIndex, partialIndex, dbSaved, dbRead, episodeTitle, url, releaseDate, locked);
                        });
                emitter.onNext(new PagedList<>(releases));
            } catch (SQLException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    void insert(Release release) {
        this.execute(release, this.insertReleaseQuery);
    }

    void insert(Collection<? extends Release> releases) {
        this.execute(releases, this.insertReleaseQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS episode_release (`episodeId` INTEGER NOT NULL, `title` TEXT NOT NULL, `url` TEXT NOT NULL, `releaseDate` TEXT NOT NULL, `locked` INTEGER NOT NULL, PRIMARY KEY(`episodeId`, `url`), FOREIGN KEY(`episodeId`) REFERENCES `episode`(`episodeId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
