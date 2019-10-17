package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.MediaList;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class MediaListTable extends AbstractTable {
    private final PreparedQuery<MediaList> insertMediaListQuery = new PreparedQuery<MediaList>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO media_list (listId, uuid, name, medium) VALUES (?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, MediaList value) throws SQLException {
            statement.setInt(1, value.getListId());
            statement.setString(2, value.getUuid());
            statement.setString(3, value.getName());
            statement.setInt(4, value.getMedium());
        }
    };

    void insert(MediaList mediaList) {
        this.execute(mediaList, this.insertMediaListQuery);
    }

    void insert(Collection<? extends MediaList> mediaLists) {
        this.execute(mediaLists, this.insertMediaListQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS media_list (`listId` INTEGER NOT NULL, `uuid` TEXT, `name` TEXT, `medium` INTEGER NOT NULL, PRIMARY KEY(`listId`), FOREIGN KEY(`uuid`) REFERENCES `user`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT listId FROM media_list";
    }
}
