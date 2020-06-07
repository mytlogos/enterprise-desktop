package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.SmallRelease;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.controller.ReleaseFilter;
import com.mytlogos.enterprisedesktop.model.DisplayRelease;
import com.mytlogos.enterprisedesktop.model.Release;
import com.mytlogos.enterprisedesktop.profile.DisplayEpisodeProfile;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class ReleaseTable extends AbstractTable {
    private final QueryBuilder<Release> insertReleaseQuery = new QueryBuilder<Release>(
            "Insert Release",
            "INSERT OR IGNORE INTO episode_release (episodeId, title, url, releaseDate, locked) VALUES (?,?,?,?,?)"
    ).setValueSetter((statement, release) -> {
        statement.setInt(1, release.getEpisodeId());
        statement.setString(2, release.getTitle());
        statement.setString(3, release.getUrl());
        statement.setString(4, Formatter.isoFormat(release.getReleaseDate()));
        statement.setBoolean(5, release.isLocked());
    });

    private final QueryBuilder<DisplayRelease> getReleasesQuery = new QueryBuilder<DisplayRelease>(
            "Select DisplayRelease",
            "SELECT episode.episodeId, episode.saved, episode.combiIndex, \n" +
                    "medium.title || ' - ' || episode_release.title, \n" +
                    "CASE episode.progress WHEN 1 THEN 1 ELSE 0 END as read, \n " +
                    "episode_release.releaseDate, episode_release.locked, " +
                    "medium.mediumId " +
                    "FROM episode_release \n" +
                    "INNER JOIN episode ON episode_release.episodeId = episode.episodeId \n" +
                    "INNER JOIN part ON episode.partId = part.partId \n" +
                    "INNER JOIN medium ON part.mediumId = medium.mediumId \n" +
//                    "LEFT JOIN list_medium ON medium.mediumId = list_medium.mediumId \n" +
                    "WHERE CASE ? " +
                    "WHEN 0 THEN progress < 1\n" +
                    "WHEN 1 THEN progress = 1\n" +
                    "ELSE 1 END " +
                    "AND (? = 0 OR (? & medium) > 0)\n" +
                    "AND (? < 0 OR saved=?)\n" +
                    "AND (? < 0 OR episode.combiIndex >= ?)\n" +
                    "AND (? < 0 OR episode.combiIndex <= ?)\n" +
//                    "AND (? = 1 OR (list_medium.listId $? AND NOT ?))" +
                    "AND (? = 1 OR (CASE medium.mediumId $? WHEN 1 THEN NOT ? ELSE ? END))" +
                    "ORDER BY episode_release.releaseDate DESC, episode.combiIndex DESC"
    ).setDependencies(
            EpisodeTable.class,
            PartTable.class,
            ReleaseTable.class,
            MediumTable.class
    ).setConverter(value -> {
        final int episodeId = value.getInt(1);
        final boolean dbSaved = value.getBoolean(2);
        final String combiIndex = value.getString(3);
        final String title = value.getString(4);
        final boolean dbRead = value.getBoolean(5);
        final LocalDateTime releaseDate = Formatter.parseLocalDateTime(value.getString(6));
        final boolean locked = value.getBoolean(7);
        final int mediumId = value.getInt(8);

        return new DisplayRelease(episodeId, title, combiIndex, dbSaved, dbRead, releaseDate, locked, mediumId);
    });

    ReleaseTable() {
        super("episode_release");
    }

    public void delete(List<SmallRelease> releases) {
        this.executeDMLQuery(
                releases,
                new QueryBuilder<SmallRelease>("Delete Release","DELETE FROM episode_release WHERE episodeId = ? AND url = ?")
                        .setValueSetter((preparedStatement, smallRelease) -> {
                            preparedStatement.setInt(1, smallRelease.episodeId);
                            preparedStatement.setString(2, smallRelease.url);
                        })
        );
    }

    public List<String> getLinks(int episodeId) {
        return new QueryBuilder<String>("Select EpisodeReleaseLinks","SELECT url FROM episode_release WHERE episodeId=?")
                .setValues(value -> value.setInt(1, episodeId))
                .setConverter(value -> value.getString(1))
                .queryListIgnoreError();
    }

    LiveData<PagedList<DisplayRelease>> getReleases(DisplayEpisodeProfile filter) {
        return this.getReleasesQuery
                .setQueryIn(filter.mediumIds, QueryBuilder.Type.INT)
                .setValues(value -> {
                    value.setInt(1, filter.readFilter);
                    value.setInt(2, filter.medium);
                    value.setInt(3, filter.medium);
                    value.setInt(4, filter.savedFilter);
                    value.setInt(5, filter.savedFilter);
                    value.setInt(6, filter.minEpisodeIndex);
                    value.setInt(7, filter.minEpisodeIndex);
                    value.setInt(8, filter.maxEpisodeIndex);
                    value.setInt(9, filter.maxEpisodeIndex);
//                    value.setBoolean(10, filter.listsIds.isEmpty());
//                    value.setBoolean(11, filter.ignoreLists);
                    value.setBoolean(10, filter.mediumIds.isEmpty());
                    value.setBoolean(11, filter.ignoreMedia);
                    value.setBoolean(12, filter.ignoreMedia);
                })
                .doEmpty()
                .selectInLiveDataList()
                .map(PagedList::new);
    }

    void insert(Release release) {
        this.executeDMLQuery(release, this.insertReleaseQuery);
    }

    void insert(Collection<? extends Release> releases) {
        this.executeDMLQuery(releases, this.insertReleaseQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS episode_release (`episodeId` INTEGER NOT NULL, `title` TEXT NOT NULL, `url` TEXT NOT NULL, `releaseDate` TEXT NOT NULL, `locked` INTEGER NOT NULL, PRIMARY KEY(`episodeId`, `url`), FOREIGN KEY(`episodeId`) REFERENCES `episode`(`episodeId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
