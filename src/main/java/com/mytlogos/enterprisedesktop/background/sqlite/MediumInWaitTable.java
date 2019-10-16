package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.MediumInWait;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class MediumInWaitTable extends AbstractTable {
    private final PreparedQuery<MediumInWait> insertMediumInWaitQuery = new PreparedQuery<MediumInWait>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO medium_in_wait (title, medium, link) VALUES (?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, MediumInWait value) throws SQLException {
            statement.setString(1, value.getTitle());
            statement.setInt(2, value.getMedium());
            statement.setString(3, value.getLink());
        }
    };

    void insert(MediumInWait mediumInWait) {
        this.execute(mediumInWait, this.insertMediumInWaitQuery);
    }

    void insert(Collection<? extends MediumInWait> mediumInWaits) {
        this.execute(mediumInWaits, this.insertMediumInWaitQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS medium_in_wait (`title` TEXT NOT NULL, `medium` INTEGER NOT NULL, `link` TEXT NOT NULL, PRIMARY KEY(`title`, `medium`, `link`))";
    }
}
