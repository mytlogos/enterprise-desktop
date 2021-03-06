package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.PartEpisode;
import com.mytlogos.enterprisedesktop.background.PartStat;
import com.mytlogos.enterprisedesktop.background.SmallRelease;
import com.mytlogos.enterprisedesktop.background.api.model.ClientEpisode;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Sorting;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 *
 */
class EpisodeTable extends AbstractTable {

    private final QueryBuilder<Episode> insertEpisodeQuery = new QueryBuilder<Episode>(
            "Insert Episode",
            "INSERT OR IGNORE INTO episode (episodeId, progress, readDate, partId, totalIndex, partialIndex, combiIndex, saved) VALUES (?,?,?,?,?,?,?,?)", getManager()
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

    private final QueryBuilder<SimpleEpisode> simpleEpisodeQuery = new QueryBuilder<SimpleEpisode>(
            "Select SimpleEpisode",
            "SELECT episodeId, totalIndex, partialIndex, progress FROM episode WHERE episodeId = ?", getManager()
    )
            .setConverter(value -> new SimpleEpisode(
                            value.getInt(1),
                            value.getInt(2),
                            value.getInt(3),
                            value.getFloat(4)
                    )
            );
    private final QueryBuilder<SimpleEpisode> simpleEpisodesQuery = new QueryBuilder<SimpleEpisode>(
            "Select SimpleEpisodes",
            "SELECT episodeId, totalIndex, partialIndex, progress FROM episode WHERE episodeId $?", getManager()
    )
            .setConverter(value -> new SimpleEpisode(
                            value.getInt(1),
                            value.getInt(2),
                            value.getInt(3),
                            value.getFloat(4)
                    )
            );

    private final QueryBuilder<Boolean> updateSavedQuery = new QueryBuilder<>("Update Saved", "UPDATE episode SET saved=? WHERE episodeId $?", getManager());
    private final QueryBuilder<Boolean> updateProgressQuery = new QueryBuilder<>("Update Progress", "UPDATE episode SET progress=?, readDate=? WHERE episodeId=?", getManager());
    private final QueryBuilder<Boolean> updateProgressMultiQuery = new QueryBuilder<>("Update Progresses", "UPDATE episode SET progress=?, readDate=? WHERE episodeId $?", getManager());
    private final QueryBuilder<Integer> saveEpisodesQuery = new QueryBuilder<>("Select Saved", "SELECT episodeId FROM episode WHERE saved=1;", getManager());
    private final QueryBuilder<Integer> getDownloadableQuery = new QueryBuilder<Integer>(
            "Select Downloadable",
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
                    "WHERE saved = 0", getManager()
    ).setConverter(value -> value.getInt(1));

    private final QueryBuilder<TocEpisode> getTocAscQuery = new QueryBuilder<TocEpisode>(
            "Select Toc Asc",
            "SELECT episode.episodeId, episode.progress, episode.readDate, episode.partId, " +
                    "episode.totalIndex, episode.partialIndex, episode.saved " +
                    "FROM episode " +
                    "INNER JOIN part ON episode.partId=part.partId " +
                    "INNER JOIN medium ON part.mediumId=medium.mediumId " +
                    "WHERE medium.mediumId=? " +
                    "AND (? < 0 OR (? == 0 AND progress < 1) OR ? = progress)" +
                    "AND (? < 0 OR ?=saved)" +
                    "ORDER BY episode.combiIndex ASC", getManager()
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
    private final QueryBuilder<TocEpisode> getTocDescQuery = new QueryBuilder<TocEpisode>(
            "Select Toc Desc",
            "SELECT episode.episodeId, episode.progress, episode.readDate, episode.partId, " +
                    "episode.totalIndex, episode.partialIndex, episode.saved " +
                    "FROM episode " +
                    "INNER JOIN part ON episode.partId=part.partId " +
                    "INNER JOIN medium ON part.mediumId=medium.mediumId " +
                    "WHERE medium.mediumId=? " +
                    "AND (? < 0 OR (? == 0 AND progress < 1) OR ? = progress)" +
                    "AND (? < 0 OR ?=saved)" +
                    "ORDER BY episode.combiIndex DESC", getManager()
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
    private final QueryBuilder<Integer> deleteMediumEpisodes = new QueryBuilder<Integer>(
            "Delete MediumEpisode",
            "DELETE FROM episode " +
                    "WHERE partId IN " +
                    "(" +
                    "SELECT partId FROM part " +
                    "WHERE part.mediumId = ?" +
                    ")", getManager()
    ).setValueSetter((statement, mediumId) -> statement.setInt(1, mediumId));

    EpisodeTable(ConnectionManager manager) {
        super("episode", manager);
    }

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

    public void updateProgress(Collection<Integer> episodeId, float progress, LocalDateTime readDate) {
        final boolean update = this.updateProgressMultiQuery.
                setQueryIn(episodeId, QueryBuilder.Type.INT)
                .setValues((statement) -> {
                    statement.setFloat(1, progress);
                    statement.setString(2, Formatter.isoFormat(readDate));
                })
                .updateInIgnoreError();
        if (update) {
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

    public List<SimpleEpisode> getSimpleEpisodes(Collection<Integer> episodeIds) {
        return this.simpleEpisodesQuery.setQueryIn(episodeIds, QueryBuilder.Type.INT).selectInListIgnoreError();
    }

    public void updateSaved(Collection<Integer> episodeIds) {
        this.updateSaved(episodeIds, true);
    }

    public void updateSaved(Collection<Integer> episodeIds, boolean saved) {
        try {
            final boolean update = this.updateSavedQuery
                    .setQueryIn(episodeIds, QueryBuilder.Type.INT)
                    .setValues(value -> value.setBoolean(1, saved))
                    .updateIn();
            if (update) {
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

    public List<PartStat> getStat() {
        try {
            return new QueryBuilder<PartStat>(
                    "Select PartStat",
                    "SELECT partId, count(DISTINCT episode.episodeId) as episodeCount, " +
                            "sum(DISTINCT episode.episodeId) as episodeSum, count(url) as releaseCount " +
                            "FROM episode LEFT JOIN episode_release ON episode.episodeId=episode_release.episodeId " +
                            "GROUP BY partId", getManager())
                    .setConverter(value -> new PartStat(
                            value.getInt(1),
                            value.getLong(2),
                            value.getLong(3),
                            value.getLong(4)
                    ))
                    .queryList();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<PartEpisode> getEpisodes(Set<Integer> partIds) {
        return new QueryBuilder<PartEpisode>("Select PartEpisodes", "SELECT partId, episodeId FROM episode WHERE partId $?", getManager())
                .setQueryIn(partIds, QueryBuilder.Type.INT)
                .setConverter(value -> new PartEpisode(value.getInt(2), value.getInt(1)))
                .selectInListIgnoreError();
    }

    public void deletePerId(List<Integer> deleteEpisodes) {
        this.executeDMLQuery(
                deleteEpisodes,
                new QueryBuilder<Integer>("Delete EpisodeIds", "DELETE FROM episode WHERE episodeId = ?", getManager())
                        .setValueSetter((preparedStatement, episodeId) -> preparedStatement.setInt(1, episodeId))
        );
    }

    public List<SmallRelease> getReleases(Set<Integer> partIds) {
        return new QueryBuilder<SmallRelease>("Select SmallReleases", "SELECT partId, episode.episodeId, episode_release.url \n" +
                "FROM episode INNER JOIN episode_release ON episode_release.episodeId=episode.episodeId \n" +
                "WHERE partId $?", getManager())
                .setQueryIn(partIds, QueryBuilder.Type.INT)
                .selectInListIgnoreError(value -> new SmallRelease(
                        value.getInt(1),
                        value.getInt(2),
                        value.getString(3)
                ));
    }

    public void update(List<ClientEpisode> update) {
        final HashMap<String, Function<ClientEpisode, ?>> attrMap = new HashMap<>();
        attrMap.put("progress", (FloatProducer<ClientEpisode>) ClientEpisode::getProgress);
        attrMap.put("totalIndex", (IntProducer<ClientEpisode>) ClientEpisode::getTotalIndex);
        attrMap.put("partialIndex", (IntProducer<ClientEpisode>) ClientEpisode::getPartialIndex);
        attrMap.put("combiIndex", (DoubleProducer<ClientEpisode>) ClientEpisode::getCombiIndex);
//        attrMap.put("saved", (BooleanProducer<ClientEpisode>) ClientEpisode::isSaved);

        final Map<String, Function<ClientEpisode, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("episodeId", (IntProducer<ClientEpisode>) ClientEpisode::getId);
        this.update(update, "episode", attrMap, keyExtractors);
    }

    public Collection<Integer> getMediumEpisodeIds(int mediumId) {
        return new QueryBuilder<Integer>("Select Medium Episode Ids",
                "SELECT episodeId FROM part \n" +
                        "INNER JOIN episode ON part.partId=episode.partId \n" +
                        "WHERE part.mediumId=?", getManager())
                .setValues(value -> value.setInt(1, mediumId))
                .setConverter(value -> value.getInt(1))
                .queryListIgnoreError();
    }

    public void removeMediumEpisodes(int mediumId) {
        this.executeDMLQuery(mediumId, this.deleteMediumEpisodes);
    }

    List<MediumEpisode> getMediumEpisodes() {
        return new QueryBuilder<MediumEpisode>(
                "Select MediumEpisode",
                //language=SQLite
                "SELECT episode.episodeId, part.mediumId, " +
                        "episode.combiIndex, episode.saved, CASE episode.progress WHEN 1 THEN 1 ELSE 0 END as read " +
                        "FROM episode " +
                        "INNER JOIN part ON episode.partId=part.partId " +
                        "INNER JOIN medium ON part.mediumId=medium.mediumId ", getManager()
        ).setConverter(value -> new MediumEpisode(
                value.getInt(1),
                value.getInt(2),
                value.getDouble(3),
                value.getBoolean(4),
                value.getBoolean(5)
        )).queryListIgnoreError();
    }

    LiveData<PagedList<TocEpisode>> getToc(int mediumId, Sorting sortings, byte read, byte saved) {
        return LiveData.create(() -> {
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
            List<Release> releases = new QueryBuilder<Release>(
                    "Select LiveReleases",
                    "SELECT episodeId, title, url, releaseDate, locked FROM episode_release WHERE episodeId $?", getManager()
            )
                    .setQueryIn(idsMap.keySet(), QueryBuilder.Type.INT)
                    .setConverter(value -> {
                        final int episodeId = value.getInt(1);
                        final String episodeTitle = value.getString(2);
                        final String url = value.getString(3);
                        final LocalDateTime releaseDate = Formatter.parseLocalDateTime(value.getString(4));
                        final boolean locked = value.getBoolean(5);
                        return new SimpleRelease(episodeId, episodeTitle, url, locked, releaseDate);
                    })
                    .selectInListIgnoreError();
            for (Release release : releases) {
                final TocEpisode episode = idsMap.get(release.getEpisodeId());
                episode.getReleases().add(release);
            }
            return new PagedList<>(tocEpisodes);
        }, Arrays.asList(EpisodeTable.class, ReleaseTable.class, PartTable.class, MediumTable.class));
    }

    void insert(Episode episode) {
        this.executeDMLQuery(episode, this.insertEpisodeQuery);
    }

    void insert(Collection<? extends Episode> episodes) {
        this.executeDMLQuery(episodes, this.insertEpisodeQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS episode " +
                "(" +
                "`episodeId` INTEGER NOT NULL, " +
                "`progress` REAL NOT NULL, " +
                "`readDate` TEXT, " +
                "`partId` INTEGER NOT NULL, " +
                "`totalIndex` INTEGER NOT NULL, " +
                "`partialIndex` INTEGER NOT NULL, " +
                "`combiIndex` REAL NOT NULL, " +
                "`saved` INTEGER NOT NULL, " +
                "PRIMARY KEY(`episodeId`), " +
                "FOREIGN KEY(`partId`) REFERENCES `part`(`partId`) ON UPDATE NO ACTION ON DELETE CASCADE " +
                ")";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT episodeId FROM episode";
    }
}
