package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ToDownload;

import java.util.Collection;
import java.util.List;

/**
 *
 */
class ToDownloadTable extends AbstractTable {
    private final QueryBuilder<ToDownload> insertToDownloadQuery = new QueryBuilder<ToDownload>(
            "Insert ToDownload",
            "INSERT OR IGNORE INTO todownload (toDownloadId, prohibited, mediumId, listId, externalListId) VALUES (?,?,?,?,?)", getManager()
    ).setValueSetter((statement, toDownload) -> {
        statement.setBoolean(1, toDownload.isProhibited());
        statement.setInt(2, toDownload.getMediumId());
        statement.setInt(3, toDownload.getListId());
        statement.setInt(4, toDownload.getExternalListId());
    });

    private final QueryBuilder<ToDownload> getItemsQuery = new QueryBuilder<ToDownload>(
            "Select ToDownload",
            "SELECT prohibited, mediumId, listId, externalListId FROM todownload", getManager()
    ).setConverter(value -> {
        final boolean prohibited = value.getBoolean(1);
        final int mediumId = value.getInt(2);
        final int listId = value.getInt(3);
        final int externalListId = value.getInt(4);
        return new ToDownload(prohibited, mediumId, listId, externalListId);
    });
    private final QueryBuilder<Integer> removeToDownloadQuery = new QueryBuilder<Integer>(
            "Delete MediumToDownload",
            "DELETE FROM todownload " +
                    "WHERE mediumId = ? ", getManager()
    ).setValueSetter((statement, mediumId) -> statement.setInt(1, mediumId));

    ToDownloadTable(ConnectionManager manager) {
        super("todownload", manager);
    }

    public List<ToDownload> getItems() {
        return this.getItemsQuery.queryListIgnoreError();
    }

    public void removeToDownload(int mediumId) {
        this.executeDMLQuery(mediumId, this.removeToDownloadQuery);
    }

    void insert(ToDownload toDownload) {
        this.executeDMLQuery(toDownload, this.insertToDownloadQuery);
    }

    void insert(Collection<? extends ToDownload> toDownloads) {
        this.executeDMLQuery(toDownloads, this.insertToDownloadQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS todownload " +
                "(`toDownloadId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`prohibited` INTEGER NOT NULL, " +
                "`mediumId` INTEGER, " +
                "`listId` INTEGER, " +
                "`externalListId` INTEGER, " +
                "FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE , " +
                "FOREIGN KEY(`listId`) REFERENCES `media_list`(`listId`) ON UPDATE NO ACTION ON DELETE CASCADE , " +
                "FOREIGN KEY(`externalListId`) REFERENCES `external_media_list`(`externalListId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
