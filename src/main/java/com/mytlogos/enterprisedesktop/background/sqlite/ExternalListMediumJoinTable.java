package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ListMediumJoin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
class ExternalListMediumJoinTable extends AbstractTable {

    private final QueryBuilder<ListMediumJoin> joinInsertQuery = new QueryBuilder<ListMediumJoin>(
            "Insert ExternalListJoin",
            "INSERT OR IGNORE INTO external_list_medium (listId, mediumId) VALUES (?,?)", getManager()
    ).setValueSetter((preparedStatement, listMediumJoin) -> {
        preparedStatement.setInt(1, listMediumJoin.getListId());
        preparedStatement.setInt(2, listMediumJoin.getMediumId());
    });

    private final QueryBuilder<Integer> deleteJoinQuery = new QueryBuilder<Integer>(
            "Delete ExternalListJoin",
            "DELETE FROM external_list_medium WHERE listId = ?", getManager()
    ).setValueSetter((preparedStatement, value) -> preparedStatement.setInt(1, value));

    private final QueryBuilder<Integer> getMediumItemsIdsQuery = new
            QueryBuilder<Integer>("Select ExternalListItems","SELECT mediumId FROM external_list_medium WHERE listId=?", getManager())
            .setValueSetter((preparedStatement, listId) -> preparedStatement.setInt(1, listId))
            .setConverter(value -> value.getInt(1));

    public ExternalListMediumJoinTable(ConnectionManager manager) {
        super("external_list_medium", manager);
    }

    public Collection<Integer> getMediumItemsIds(Integer externalListId) {
        return this.getMediumItemsIdsQuery.setValue(Collections.singleton(externalListId)).queryListIgnoreError();
    }

    public void delete(int listId) {
        this.delete(Collections.singletonList(listId));
    }

    public void delete(List<Integer> listIds) {
        this.executeDMLQuery(listIds, this.deleteJoinQuery);
    }

    public List<ListMediumJoin> getListItems() {
        return new QueryBuilder<ListMediumJoin>("Select ExternalListJoin", "SELECT listId, mediumId FROM external_list_medium", getManager())
                .setConverter(value -> new ListMediumJoin(value.getInt(1), value.getInt(2), true))
                .queryListIgnoreError();
    }

    public void removeJoin(List<ListMediumJoin> exListJoins) {
        this.executeDMLQuery(
                exListJoins,
                new QueryBuilder<ListMediumJoin>("Remove ExternalListJoint", "DELETE FROM external_list_medium WHERE listId = ? and mediumId = ?;", getManager())
                        .setValueSetter((preparedStatement, listMediumJoin) -> {
                            if (!listMediumJoin.external) {
                                throw new IllegalArgumentException("Trying to delete a normal ListJoin from an external one");
                            }
                            preparedStatement.setInt(1, listMediumJoin.listId);
                            preparedStatement.setInt(2, listMediumJoin.mediumId);
                        }));
    }

    void insert(ListMediumJoin join) {
        this.insert(Collections.singleton(join));
    }

    void insert(Collection<? extends ListMediumJoin> joins) {
        this.executeDMLQuery(joins, this.joinInsertQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS external_list_medium " +
                "(" +
                "`listId` INTEGER NOT NULL, " +
                "`mediumId` INTEGER NOT NULL, " +
                "PRIMARY KEY(`listId`, `mediumId`), " +
                "FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE , " +
                "FOREIGN KEY(`listId`) REFERENCES `external_media_list`(`externalListId`) ON UPDATE NO ACTION ON DELETE CASCADE " +
                ")";
    }

}
