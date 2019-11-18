package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ListMediumJoin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class ListMediumJoinTable extends AbstractTable {
    private final QueryBuilder<ListMediumJoin> insertListMediumJoinQuery = new QueryBuilder<ListMediumJoin>(
            "INSERT OR IGNORE INTO list_medium (listId, mediumId) VALUES (?,?)"
    ).setValueSetter((statement, listMediumJoin) -> {
        statement.setInt(1, listMediumJoin.getListId());
        statement.setInt(2, listMediumJoin.getMediumId());
    });

    private final QueryBuilder<Integer> deleteJoinQuery = new QueryBuilder<Integer>(
            "DELETE FROM list_medium WHERE listId = ?"
    ).setValueSetter((statement, integer) -> statement.setInt(1, integer));

    private final QueryBuilder<Integer> getMediumItemsIdsQuery = new QueryBuilder<Integer>(
            "SELECT mediumId FROM list_medium WHERE listId=?"
    ).setConverter(value -> value.getInt(1));

    public Collection<Integer> getMediumItemsIds(Integer listId) {
        try {
            return this.getMediumItemsIdsQuery.setValues(value -> value.setInt(1, listId)).queryList();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void delete(int listId) {
        this.executeDMLQuery(listId, this.deleteJoinQuery);
    }

    public void delete(List<Integer> listIds) {
        this.executeDMLQuery(listIds, this.deleteJoinQuery);
    }

    void insert(ListMediumJoin listMediumJoin) {
        this.executeDMLQuery(listMediumJoin, this.insertListMediumJoinQuery);
    }

    void insert(Collection<? extends ListMediumJoin> listMediumJoins) {
        this.executeDMLQuery(listMediumJoins, this.insertListMediumJoinQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS list_medium (`listId` INTEGER NOT NULL, `mediumId` INTEGER NOT NULL, PRIMARY KEY(`listId`, `mediumId`), FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`listId`) REFERENCES `mediaList`(`listId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
