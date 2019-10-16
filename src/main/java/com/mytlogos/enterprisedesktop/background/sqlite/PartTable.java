package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.Part;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class PartTable extends AbstractTable {
    private final PreparedQuery<Part> insertPartQuery = new PreparedQuery<Part>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO part (partId, mediumId, title, totalIndex, partialIndex, combiIndex) VALUES (?,?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, Part value) throws SQLException {
            statement.setInt(1, value.getPartId());
            statement.setInt(2, value.getMediumId());
            statement.setString(3, value.getTitle());
            statement.setInt(4, value.getTotalIndex());
            statement.setInt(5, value.getPartialIndex());
            statement.setDouble(6, value.getCombiIndex());
        }
    };

    void insert(Part part) {
        this.execute(part, this.insertPartQuery);
    }

    void insert(Collection<? extends Part> parts) {
        this.execute(parts, this.insertPartQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS part (`partId` INTEGER NOT NULL, `mediumId` INTEGER NOT NULL, `title` TEXT, `totalIndex` INTEGER NOT NULL, `partialIndex` INTEGER NOT NULL, `combiIndex` REAL NOT NULL, PRIMARY KEY(`partId`), FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE SET NULL )";
    }
}
