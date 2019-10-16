package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.Release;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

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
            statement.setString(4, Formatter.format(value.getReleaseDate()));
            statement.setBoolean(5, value.isLocked());
        }
    };

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
