package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.FailedEpisode;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class FailedEpisodeTable extends AbstractTable {
    private final PreparedQuery<FailedEpisode> insertFailedEpisodeQuery = new PreparedQuery<FailedEpisode>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO failed_episode (episodeId, failCount) VALUES (?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, FailedEpisode value) throws SQLException {
            statement.setInt(1, value.getEpisodeId());
            statement.setInt(2, value.getFailCount());
        }
    };

    void insert(FailedEpisode failedEpisode) {
        this.execute(failedEpisode, this.insertFailedEpisodeQuery);
    }

    void insert(Collection<? extends FailedEpisode> failedEpisodes) {
        this.execute(failedEpisodes, this.insertFailedEpisodeQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS `failed_episode` (`episodeId` INTEGER NOT NULL, `failCount` INTEGER NOT NULL, PRIMARY KEY(`episodeId`), FOREIGN KEY(`episodeId`) REFERENCES `episode`(`episodeId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
