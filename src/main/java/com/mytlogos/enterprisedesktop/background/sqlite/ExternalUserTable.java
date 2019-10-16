package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.ExternalUser;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class ExternalUserTable extends AbstractTable {
    private final PreparedQuery<ExternalUser> insertExternalUserQuery = new PreparedQuery<ExternalUser>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO external_user (uuid, userUuid, identifier, type) VALUES (?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, ExternalUser value) throws SQLException {
            statement.setString(1, value.getUuid());
            statement.setString(2, value.getUserUuid());
            statement.setString(3, value.getIdentifier());
            statement.setInt(4, value.getType());
        }
    };

    void insert(ExternalUser externalUser) {
        this.execute(externalUser, this.insertExternalUserQuery);
    }

    void insert(Collection<? extends ExternalUser> externalUsers) {
        this.execute(externalUsers, this.insertExternalUserQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS external_user (`uuid` TEXT NOT NULL, `userUuid` TEXT NOT NULL, `identifier` TEXT NOT NULL, `type` INTEGER NOT NULL, PRIMARY KEY(`uuid`), FOREIGN KEY(`userUuid`) REFERENCES `user`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )";
    }
}
