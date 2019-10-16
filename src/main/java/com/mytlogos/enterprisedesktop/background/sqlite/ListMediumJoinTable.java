package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ListMediumJoin;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class ListMediumJoinTable extends AbstractTable {
    private final PreparedQuery<ListMediumJoin> insertListMediumJoinQuery = new PreparedQuery<ListMediumJoin>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO list_medium (listId, mediumId) VALUES (?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, ListMediumJoin value) throws SQLException {
            statement.setInt(1, value.getListId());
            statement.setInt(2, value.getMediumId());
        }
    };
    private final PreparedQuery<Integer> deleteJoinQuery = new PreparedQuery<Integer>() {
        @Override
        public String getQuery() {
            return "DELETE FROM list_medium WHERE listId = ?";
        }

        @Override
        public void setValues(PreparedStatement statement, Integer value) throws SQLException {
            statement.setInt(1, value);
        }
    };

    public void delete(int listId) {
        this.execute(listId, this.deleteJoinQuery);
    }

    public void delete(List<Integer> listIds) {
        this.execute(listIds, this.deleteJoinQuery);
    }

    void insert(ListMediumJoin listMediumJoin) {
        this.execute(listMediumJoin, this.insertListMediumJoinQuery);
    }

    void insert(Collection<? extends ListMediumJoin> listMediumJoins) {
        this.execute(listMediumJoins, this.insertListMediumJoinQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS list_medium (`listId` INTEGER NOT NULL, `mediumId` INTEGER NOT NULL, PRIMARY KEY(`listId`, `mediumId`), FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`listId`) REFERENCES `mediaList`(`listId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
