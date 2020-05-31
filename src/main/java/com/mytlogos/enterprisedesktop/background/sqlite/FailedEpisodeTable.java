package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.FailedEpisode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class FailedEpisodeTable extends AbstractTable {
    private final QueryBuilder<FailedEpisode> insertFailedEpisodeQuery = new QueryBuilder<FailedEpisode>(
            "Insert FailedEpisode",
            "INSERT OR IGNORE INTO failed_episode (episodeId, failCount) VALUES (?,?)"
    ).setValueSetter((statement, failedEpisode) -> {
        statement.setInt(1, failedEpisode.getEpisodeId());
        statement.setInt(2, failedEpisode.getFailCount());
    });
    private final QueryBuilder<FailedEpisode> failedEpisodeQuery = new QueryBuilder<>("Select FailedEpisode","SELECT episodeId, failCount FROM failed_episode WHERE episodeId $?");

    FailedEpisodeTable() {
        super("failed_episode");
    }

    public List<FailedEpisode> getFailedEpisodes(Collection<Integer> episodeIds) {
        try {
            // FIXME: 17.11.2019: could be a bug to use query in and queryList together
            return this.failedEpisodeQuery
                    .setQueryIn(episodeIds, QueryBuilder.Type.INT)
                    .setConverter(value -> new FailedEpisode(value.getInt(1), value.getInt(2)))
                    .queryList();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void updateFailedDownload(int episodeId) {
        // TODO 17.11.2019:
    }

    void insert(FailedEpisode failedEpisode) {
        this.executeDMLQuery(failedEpisode, this.insertFailedEpisodeQuery);
    }

    void insert(Collection<? extends FailedEpisode> failedEpisodes) {
        this.executeDMLQuery(failedEpisodes, this.insertFailedEpisodeQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS `failed_episode` (`episodeId` INTEGER NOT NULL, `failCount` INTEGER NOT NULL, PRIMARY KEY(`episodeId`), FOREIGN KEY(`episodeId`) REFERENCES `episode`(`episodeId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
