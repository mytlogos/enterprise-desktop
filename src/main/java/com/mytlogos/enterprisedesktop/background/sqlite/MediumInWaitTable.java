package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.MediumInWait;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class MediumInWaitTable extends AbstractTable {
    private final QueryBuilder<MediumInWait> insertMediumInWaitQuery = new QueryBuilder<MediumInWait>(
            "INSERT OR IGNORE INTO medium_in_wait (title, medium, link) VALUES (?,?,?)"
    ).setValueSetter((statement, mediumInWait) -> {
        statement.setString(1, mediumInWait.getTitle());
        statement.setInt(2, mediumInWait.getMedium());
        statement.setString(3, mediumInWait.getLink());
    });

    void insert(MediumInWait mediumInWait) {
        this.executeDMLQuery(mediumInWait, this.insertMediumInWaitQuery);
    }

    void insert(Collection<? extends MediumInWait> mediumInWaits) {
        this.executeDMLQuery(mediumInWaits, this.insertMediumInWaitQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS medium_in_wait (`title` TEXT NOT NULL, `medium` INTEGER NOT NULL, `link` TEXT NOT NULL, PRIMARY KEY(`title`, `medium`, `link`))";
    }
}
