package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.Part;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class PartTable extends AbstractTable {
    private final QueryBuilder<Part> insertPartQuery = new QueryBuilder<Part>(
            "INSERT OR IGNORE INTO part (partId, mediumId, title, totalIndex, partialIndex, combiIndex) VALUES (?,?,?,?,?,?)"
    ).setValueSetter((statement, part) -> {
        statement.setInt(1, part.getPartId());
        statement.setInt(2, part.getMediumId());
        statement.setString(3, part.getTitle());
        statement.setInt(4, part.getTotalIndex());
        statement.setInt(5, part.getPartialIndex());
        statement.setDouble(6, part.getCombiIndex());
    });


    void insert(Part part) {
        this.executeDMLQuery(part, this.insertPartQuery);
    }

    void insert(Collection<? extends Part> parts) {
        this.executeDMLQuery(parts, this.insertPartQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS part (`partId` INTEGER NOT NULL, `mediumId` INTEGER NOT NULL, `title` TEXT, `totalIndex` INTEGER NOT NULL, `partialIndex` INTEGER NOT NULL, `combiIndex` REAL NOT NULL, PRIMARY KEY(`partId`), FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE SET NULL )";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT partId FROM part";
    }
}
