package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.api.model.ClientExternalUser;
import com.mytlogos.enterprisedesktop.model.ExternalUser;

import java.util.*;
import java.util.function.Function;

/**
 *
 */
class ExternalUserTable extends AbstractTable {
    private final QueryBuilder<ExternalUser> insertExternalUserQuery = new QueryBuilder<ExternalUser>(
            "Insert ExternalUser",
            "INSERT OR IGNORE INTO external_user (uuid, userUuid, identifier, type) VALUES (?,?,?,?)"
    ).setValueSetter((statement, externalUser) -> {
        statement.setString(1, externalUser.getUuid());
        statement.setString(2, externalUser.getUserUuid());
        statement.setString(3, externalUser.getIdentifier());
        statement.setInt(4, externalUser.getType());

    });

    ExternalUserTable() {
        super("external_user");
    }

    public void delete(Set<String> deletedExUser) {
        this.executeDMLQuery(
                deletedExUser,
                new QueryBuilder<String>("Delete ExternalUuid","DELETE FROM external_user WHERE uuid = ?")
                        .setValueSetter((preparedStatement, uuid) -> preparedStatement.setString(1, uuid))
        );
    }

    public void update(List<ClientExternalUser> update) {
        final HashMap<String, Function<ClientExternalUser, ?>> attrMap = new HashMap<>();
        attrMap.put("identifier", (StringProducer<ClientExternalUser>) ClientExternalUser::getIdentifier);
        attrMap.put("type", (IntProducer<ClientExternalUser>) ClientExternalUser::getType);

        final Map<String, Function<ClientExternalUser, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("uuid", (StringProducer<ClientExternalUser>) ClientExternalUser::getUuid);
        this.update(update, "external_user", attrMap, keyExtractors);
    }

    void insert(ExternalUser externalUser) {
        this.insert(Collections.singleton(externalUser));
    }

    void insert(Collection<? extends ExternalUser> externalUsers) {
        this.executeDMLQuery(externalUsers, this.insertExternalUserQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS external_user " +
                "(" +
                "`uuid` TEXT NOT NULL, " +
                "`userUuid` TEXT NOT NULL, " +
                "`identifier` TEXT NOT NULL, " +
                "`type` INTEGER NOT NULL, " +
                "PRIMARY KEY(`uuid`), " +
                "FOREIGN KEY(`userUuid`) REFERENCES `user`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE " +
                ")";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT uuid FROM external_user";
    }
}
