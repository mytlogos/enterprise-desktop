package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ToDownload;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class ToDownloadTable extends AbstractTable {
    private final PreparedQuery<ToDownload> insertToDownloadQuery = new PreparedQuery<ToDownload>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO todownload (toDownloadId, prohibited, mediumId, listId, externalListId) VALUES (?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, ToDownload value) throws SQLException {
            statement.setBoolean(1, value.isProhibited());
            statement.setInt(2, value.getMediumId());
            statement.setInt(3, value.getListId());
            statement.setInt(4, value.getExternalListId());
        }
    };

    void insert(ToDownload toDownload) {
        this.execute(toDownload, this.insertToDownloadQuery);
    }

    void insert(Collection<? extends ToDownload> toDownloads) {
        this.execute(toDownloads, this.insertToDownloadQuery);
    }


    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS todownload (`toDownloadId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `prohibited` INTEGER NOT NULL, `mediumId` INTEGER, `listId` INTEGER, `externalListId` INTEGER, FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`listId`) REFERENCES `media_list`(`listId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`externalListId`) REFERENCES `external_media_list`(`externalListId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
