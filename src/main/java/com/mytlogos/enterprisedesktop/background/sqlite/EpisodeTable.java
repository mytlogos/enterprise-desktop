package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Sorting;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
class EpisodeTable extends AbstractTable {
    private final QueryBuilder<Episode> insertEpisodeQuery = new QueryBuilder<Episode>(
            "INSERT OR IGNORE INTO episode (episodeId, progress, readDate, partId, totalIndex, partialIndex, combiIndex, saved) VALUES (?,?,?,?,?,?,?,?)"
    ).setValueSetter((statement, episode) -> {
        statement.setInt(1, episode.getEpisodeId());
        statement.setFloat(2, episode.getProgress());
        statement.setString(3, Formatter.isoFormat(episode.getReadDate()));
        statement.setInt(4, episode.getPartId());
        statement.setInt(5, episode.getTotalIndex());
        statement.setInt(6, episode.getPartialIndex());
        statement.setDouble(7, episode.getCombiIndex());
        statement.setBoolean(8, episode.isSaved());
    });

    private QueryBuilder<SimpleEpisode> simpleEpisodeQuery = new QueryBuilder<SimpleEpisode>("SELECT episodeId, partialIndex, totalIndex, progress FROM episode WHERE episodeId = ?")
            .setConverter(value -> new SimpleEpisode(
                            value.getInt(1),
                            value.getInt(2),
                            value.getInt(3),
                            value.getFloat(4)
                    )
            );

    private QueryBuilder<Boolean> updateSavedQuery = new QueryBuilder<>("UPDATE episode SET saved=? WHERE episodeId $?");
    private QueryBuilder<Boolean> updateProgressQuery = new QueryBuilder<>("UPDATE episode SET progress=?, readDate=? WHERE episodeId=?");
    private QueryBuilder<Integer> saveEpisodesQuery = new QueryBuilder<>("SELECT episodeId FROM episode WHERE saved=1;");
    private QueryBuilder<Integer> getDownloadableQuery = new QueryBuilder<Integer>(
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
                    "WHERE saved = 0"
    ).setConverter(value -> value.getInt(1));

    private QueryBuilder<TocEpisode> getTocAscQuery = new QueryBuilder<TocEpisode>(
            "SELECT episode.episodeId, episode.progress, episode.readDate, episode.partId, " +
                    "episode.totalIndex, episode.partialIndex, episode.saved " +
                    "FROM episode " +
                    "INNER JOIN part ON episode.partId=part.partId " +
                    "INNER JOIN medium ON part.mediumId=medium.mediumId " +
                    "WHERE medium.mediumId=? " +
                    "AND (? < 0 OR (? == 0 AND progress < 1) OR ? = progress)" +
                    "AND (? < 0 OR ?=saved)" +
                    "ORDER BY episode.combiIndex ASC"
    ).setConverter(value -> {
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
    private QueryBuilder<TocEpisode> getTocDescQuery = new QueryBuilder<TocEpisode>(
            "SELECT episode.episodeId, episode.progress, episode.readDate, episode.partId, " +
                    "episode.totalIndex, episode.partialIndex, episode.saved " +
                    "FROM episode " +
                    "INNER JOIN part ON episode.partId=part.partId " +
                    "INNER JOIN medium ON part.mediumId=medium.mediumId " +
                    "WHERE medium.mediumId=? " +
                    "AND (? < 0 OR (? == 0 AND progress < 1) OR ? = progress)" +
                    "AND (? < 0 OR ?=saved)" +
                    "ORDER BY episode.combiIndex DESC"
    ).setConverter(value -> {
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

    public void updateProgress(int episodeId, float progress, LocalDateTime readDate) {
        final QueryBuilder<Boolean> voidQueryBuilder = this.updateProgressQuery.setValues((statement) -> {
            statement.setFloat(1, progress);
            statement.setString(2, Formatter.isoFormat(readDate));
            statement.setInt(3, episodeId);
        });
        if (voidQueryBuilder.execute()) {
            this.setInvalidated();
        }
    }

    public List<Integer> getDownloadable(Integer mediumId, int limit) {
        try {
            final QueryBuilder<Integer> builder = this.getDownloadableQuery.setValues(value -> {
                value.setInt(1, mediumId);
                value.setInt(2, limit);
                value.setInt(3, limit);
            });
            return builder.queryList();
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
            final Boolean update = this.updateSavedQuery
                    .setQueryIn(episodeIds, QueryBuilder.Type.INT)
                    .executeIn(
                            SqlUtils.update(value -> value.setBoolean(1, saved)),
                            (o, o1) -> o || o1
                    );
            if (update != null && update) {
                this.setInvalidated();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getSavedEpisodes() {
        try {
            return this.saveEpisodesQuery.setConverter(value -> value.getInt(1)).queryList();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    Flowable<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved) {
        return Flowable.create(emitter -> {
            QueryBuilder<TocEpisode> queryBuilder = sortings.getSortValue() > 0 ? this.getTocAscQuery : this.getTocDescQuery;
            final List<TocEpisode> tocEpisodes = queryBuilder.setValues(value -> {
                value.setInt(1, mediumId);
                value.setByte(2, read);
                value.setByte(3, read);
                value.setByte(4, read);
                value.setByte(5, saved);
                value.setByte(6, saved);
            }).queryList();
            // TODO 19.10.2019: try select releases by mediumId and add them
            Map<Integer, TocEpisode> idsMap = new HashMap<>();
            for (TocEpisode item : tocEpisodes) {
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
                            SqlUtils::mergeLists
                    );
            for (Release release : releases) {
                final TocEpisode episode = idsMap.get(release.getEpisodeId());
                episode.getReleases().add(release);
            }
            emitter.onNext(new PagedList<>(tocEpisodes));
        }, BackpressureStrategy.LATEST);
    }

    void insert(Episode episode) {
        this.executeDMLQuery(episode, this.insertEpisodeQuery);
    }

    void insert(Collection<? extends Episode> episodes) {
        this.executeDMLQuery(episodes, this.insertEpisodeQuery);
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
