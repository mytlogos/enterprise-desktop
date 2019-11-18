package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ExternalUser;

import java.util.Collection;
import java.util.Collections;

/**
 *
 */
class ExternalUserTable extends AbstractTable {
    private final QueryBuilder<ExternalUser> insertExternalUserQuery = new QueryBuilder<ExternalUser>(
            "INSERT OR IGNORE INTO external_user (uuid, userUuid, identifier, type) VALUES (?,?,?,?)"
    ).setValueSetter((statement, externalUser) -> {
        statement.setString(1, externalUser.getUuid());
        statement.setString(2, externalUser.getUserUuid());
        statement.setString(3, externalUser.getIdentifier());
        statement.setInt(4, externalUser.getType());

    });

    void insert(ExternalUser externalUser) {
        this.insert(Collections.singleton(externalUser));
    }

    void insert(Collection<? extends ExternalUser> externalUsers) {
        this.executeDMLQuery(externalUsers, this.insertExternalUserQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS external_user (`uuid` TEXT NOT NULL, `userUuid` TEXT NOT NULL, `identifier` TEXT NOT NULL, `type` INTEGER NOT NULL, PRIMARY KEY(`uuid`), FOREIGN KEY(`userUuid`) REFERENCES `user`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT uuid FROM external_user";
    }
}
