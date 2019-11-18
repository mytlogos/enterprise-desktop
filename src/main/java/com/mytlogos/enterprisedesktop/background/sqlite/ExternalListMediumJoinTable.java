package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ListMediumJoin;

import java.sql.SQLException;
import java.util.*;

/**
 *
 */
class ExternalListMediumJoinTable extends AbstractTable {

    private final QueryBuilder<ListMediumJoin> joinInsertQuery = new QueryBuilder<ListMediumJoin>(
            "INSERT OR IGNORE INTO external_list_medium (listId, mediumId) VALUES (?,?)"
    ).setValueSetter((preparedStatement, listMediumJoin) -> {
        preparedStatement.setInt(1, listMediumJoin.getListId());
        preparedStatement.setInt(2, listMediumJoin.getMediumId());
    });

    private final QueryBuilder<Integer> deleteJoinQuery = new QueryBuilder<Integer>(
            "DELETE FROM external_list_medium WHERE listId = ?"
    ).setValueSetter((preparedStatement, value) -> preparedStatement.setInt(1, value));

    private final QueryBuilder<Integer> getMediumItemsIdsQuery = new
            QueryBuilder<Integer>("SELECT mediumId FROM external_list_medium WHERE listId=?")
            .setValueSetter((preparedStatement, listId) -> preparedStatement.setInt(1, listId))
            .setConverter(value -> value.getInt(1));

    public Collection<Integer> getMediumItemsIds(Integer externalListId) {
        try {
            return this.getMediumItemsIdsQuery.setValue(Collections.singleton(externalListId)).queryList();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void delete(List<Integer> listIds) {
        this.executeDMLQuery(listIds, this.deleteJoinQuery);
    }

    public void delete(int listId) {
        this.delete(Collections.singletonList(listId));
    }

    void insert(ListMediumJoin join) {
        this.insert(Collections.singleton(join));
    }

    void insert(Collection<? extends ListMediumJoin> joins) {
        this.executeDMLQuery(joins, this.joinInsertQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS external_list_medium (`listId` INTEGER NOT NULL, `mediumId` INTEGER NOT NULL, PRIMARY KEY(`listId`, `mediumId`), FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`listId`) REFERENCES `externalMediaList`(`externalListId`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }

}
