package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Sorting;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
class EpisodeTable extends AbstractTable {
    private final PreparedQuery<Episode> insertEpisodeQuery = new PreparedQuery<Episode>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO episode (episodeId, progress, readDate, partId, totalIndex, partialIndex, combiIndex, saved) VALUES (?,?,?,?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, Episode value) throws SQLException {
            statement.setInt(1, value.getEpisodeId());
            statement.setFloat(2, value.getProgress());
            statement.setString(3, Formatter.isoFormat(value.getReadDate()));
            statement.setInt(4, value.getPartId());
            statement.setInt(5, value.getTotalIndex());
            statement.setInt(6, value.getPartialIndex());
            statement.setDouble(7, value.getCombiIndex());
            statement.setBoolean(8, value.isSaved());
        }
    };

    private QueryBuilder<SimpleEpisode> simpleEpisodeQuery = new QueryBuilder<SimpleEpisode>("SELECT episodeId, partialIndex, totalIndex, progress FROM episode WHERE episodeId = ?")
            .setConverter(value -> new SimpleEpisode(
                            value.getInt(1),
                            value.getInt(2),
                            value.getInt(3),
                            value.getFloat(4)
                    )
            );

    private QueryBuilder<Void> updateSavedQuery = new QueryBuilder<>("UPDATE episode SET saved=? WHERE episodeId $?");
    private QueryBuilder<Integer> saveEpisodesQuery = new QueryBuilder<>("SELECT episodeId FROM episode WHERE saved=1;");

    public void updateProgress(int episodeId, float progress, LocalDateTime readDate) {
        this.execute(
                "UPDATE episode SET progress=?, readDate=? WHERE episodeId=?",
                value -> {
                    value.setFloat(1, progress);
                    value.setString(2, Formatter.isoFormat(readDate));
                    value.setInt(3, episodeId);
                }
        );
    }

    public List<Integer> getDownloadable(Integer mediumId, int limit) {
        try {
            return this.selectList(
                    "SELECT episodeId FROM (SELECT episode.episodeId, episode.saved FROM episode " +
                            "LEFT JOIN failedEpisode ON failedEpisode.episodeId=episode.episodeId " +
                            "INNER JOIN part ON part.partId=episode.partId " +
                            "INNER JOIN medium ON part.mediumId=medium.mediumId " +
                            "WHERE " +
                            "progress < 1 " +
                            "AND part.mediumId = ? " +
                            "AND episode.episodeId IN (SELECT episodeId FROM release WHERE locked=0)" +
                            "ORDER BY " +
                            "CASE medium.medium " +
                            "WHEN 1 THEN 1 " +
                            "WHEN 2 THEN 2 " +
                            "WHEN 4 THEN 4 " +
                            "WHEN 8 THEN 3 " +
                            "ELSE 5 " +
                            "END, " +
                            "episode.combiIndex LIMIT CASE WHEN ? < 0 THEN 0 ELSE ? END) " +
                            "as episode " +
                            "WHERE saved = 0",
                    value -> {
                        value.setInt(1, mediumId);
                        value.setInt(2, limit);
                        value.setInt(3, limit);
                    },
                    value -> value.getInt(1)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public SimpleEpisode getSimpleEpisode(int episodeId) {
        try {
            return this.simpleEpisodeQuery.setValues(value -> value.setInt(1, episodeId)).query(this.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateSaved(Collection<Integer> episodeIds) {
        this.updateSaved(episodeIds, true);
    }

    public void updateSaved(Collection<Integer> episodeIds, boolean saved) {
        try {
            this.updateSavedQuery
                    .setQueryIn(episodeIds, QueryBuilder.Type.INT)
                    .executeIn(
                            SqlUtils.update(value -> value.setBoolean(1, saved)),
                            (o, o1) -> null
                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getSavedEpisodes() {
        try {
            return saveEpisodesQuery.setConverter(value -> value.getInt(1)).queryList();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    Flowable<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved) {
        return Flowable.create(emitter -> {
            // TODO 19.10.2019: select releases by mediumId and add them
            final List<TocEpisode> items = this.selectList(
                    "SELECT episode.episodeId, episode.progress, episode.readDate, episode.partId, " +
                            "episode.totalIndex, episode.partialIndex, episode.saved " +
                            "FROM episode " +
                            "INNER JOIN part ON episode.partId=part.partId " +
                            "INNER JOIN medium ON part.mediumId=medium.mediumId " +
                            "WHERE medium.mediumId=? " +
                            "AND (? < 0 OR (? == 0 AND progress < 1) OR ? = progress)" +
                            "AND (? < 0 OR ?=saved)" +
                            "ORDER BY episode.combiIndex " + (sortings.getSortValue() > 0 ? "ASC" : "DESC"),
                    value -> {
                        value.setInt(1, mediumId);
                        value.setByte(2, read);
                        value.setByte(3, read);
                        value.setByte(4, read);
                        value.setByte(5, saved);
                        value.setByte(6, saved);
                    },
                    value -> {
                        final int episodeId = value.getInt(1);
                        final float progress = value.getFloat(2);
                        final LocalDateTime localDateTime = Formatter.parseLocalDateTime(value.getString(3));
                        final int partId = value.getInt(4);
                        final int totalIndex = value.getInt(5);
                        final int partialIndex = value.getInt(6);
                        final boolean savedDb = value.getBoolean(7);
                        return new TocEpisode(episodeId, progress, partId, partialIndex, totalIndex, localDateTime, savedDb, new ArrayList<>());
                    }
            );
            Map<Integer, TocEpisode> idsMap = new HashMap<>();
            for (TocEpisode item : items) {
                idsMap.put(item.getEpisodeId(), item);
            }
            List<Release> releases = new QueryBuilder<List<Release>>("SELECT episodeId, title, url, releaseDate, locked FROM episode_release WHERE episodeId $?")
                    .setQueryIn(idsMap.keySet(), QueryBuilder.Type.INT)
                    .executeIn(
                            SqlUtils.getResults(value -> {
                                final int episodeId = value.getInt(1);
                                final String episodeTitle = value.getString(2);
                                final String url = value.getString(3);
                                final LocalDateTime releaseDate = Formatter.parseLocalDateTime(value.getString(4));
                                final boolean locked = value.getBoolean(5);
                                return new SimpleRelease(episodeId, episodeTitle, url, locked, releaseDate);
                            }),
                            (releases1, releases2) -> {
                                if (releases1 == null) {
                                    return releases2;
                                }
                                releases1.addAll(releases2);
                                return releases1;
                            }
                    );
            for (Release release : releases) {
                final TocEpisode episode = idsMap.get(release.getEpisodeId());
                episode.getReleases().add(release);
            }
            emitter.onNext(new PagedList<>(items));
        }, BackpressureStrategy.LATEST);
    }

    void insert(Episode episode) {
        this.execute(episode, this.insertEpisodeQuery);
    }

    void insert(Collection<? extends Episode> episodes) {
        this.execute(episodes, this.insertEpisodeQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS episode (`episodeId` INTEGER NOT NULL, `progress` REAL NOT NULL, `readDate` TEXT, `partId` INTEGER NOT NULL, `totalIndex` INTEGER NOT NULL, `partialIndex` INTEGER NOT NULL, `combiIndex` REAL NOT NULL, `saved` INTEGER NOT NULL, PRIMARY KEY(`episodeId`), FOREIGN KEY(`partId`) REFERENCES `part`(`partId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT episodeId FROM episode";
    }
}
