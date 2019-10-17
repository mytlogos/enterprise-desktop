package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ExternalMediaList;
import com.mytlogos.enterprisedesktop.model.ExternalMediaListImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class ExternalMediaListTable extends AbstractTable {
    private final PreparedQuery<ExternalMediaList> insertExternalMediaListQuery = new PreparedQuery<ExternalMediaList>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO external_media_list (uuid,externalListId, name, medium, url) VALUES (?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, ExternalMediaList value) throws SQLException {
            statement.setString(1, value.getUuid());
            statement.setInt(2, value.getListId());
            statement.setString(3, value.getName());
            statement.setInt(4, value.getMedium());
            statement.setString(5, value.getUrl());
        }
    };

    void insert(ExternalMediaList externalMediaList) {
        this.execute(externalMediaList, this.insertExternalMediaListQuery);
    }

    void insert(Collection<? extends ExternalMediaList> externalMediaLists) {
        this.execute(externalMediaLists, this.insertExternalMediaListQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS external_media_list (`uuid` TEXT, `externalListId` INTEGER NOT NULL, `name` TEXT, `medium` INTEGER NOT NULL, `url` TEXT, PRIMARY KEY(`externalListId`), FOREIGN KEY(`uuid`) REFERENCES `externalUser`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT externalListId FROM external_media_list";
    }
}
