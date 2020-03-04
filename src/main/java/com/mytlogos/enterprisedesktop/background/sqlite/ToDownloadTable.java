package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ToDownload;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class ToDownloadTable extends AbstractTable {
    private final QueryBuilder<ToDownload> insertToDownloadQuery = new QueryBuilder<ToDownload>(
            "INSERT OR IGNORE INTO todownload (toDownloadId, prohibited, mediumId, listId, externalListId) VALUES (?,?,?,?,?)"
    ).setValueSetter((statement, toDownload) -> {
        statement.setBoolean(1, toDownload.isProhibited());
        statement.setInt(2, toDownload.getMediumId());
        statement.setInt(3, toDownload.getListId());
        statement.setInt(4, toDownload.getExternalListId());
    });

    private final QueryBuilder<ToDownload> getItemsQuery = new QueryBuilder<ToDownload>(
            "SELECT prohibited, mediumId, listId, externalListId FROM todownload"
    ).setConverter(value -> {
        final boolean prohibited = value.getBoolean(1);
        final int mediumId = value.getInt(2);
        final int listId = value.getInt(3);
        final int externalListId = value.getInt(4);
        return new ToDownload(prohibited, mediumId, listId, externalListId);
    });

    ToDownloadTable() {
        super("todownload");
    }

    public List<ToDownload> getItems() {
        return this.getItemsQuery.queryListIgnoreError();
    }

    void insert(ToDownload toDownload) {
        this.executeDMLQuery(toDownload, this.insertToDownloadQuery);
    }

    void insert(Collection<? extends ToDownload> toDownloads) {
        this.executeDMLQuery(toDownloads, this.insertToDownloadQuery);
    }


    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS todownload (`toDownloadId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `prohibited` INTEGER NOT NULL, `mediumId` INTEGER, `listId` INTEGER, `externalListId` INTEGER, FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`listId`) REFERENCES `media_list`(`listId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`externalListId`) REFERENCES `external_media_list`(`externalListId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
