package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.Episode;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;

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

    public void updateProgress(int episodeId, float progress, LocalDateTime readDate) {
        // TODO 16.10.2019:
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
