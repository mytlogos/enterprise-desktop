package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.Medium;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class MediumTable extends AbstractTable {
    private final PreparedQuery<Medium> insertMediumQuery = new PreparedQuery<Medium>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO medium (mediumId, currentRead, countryOfOrigin, languageOfOrigin, author, title, medium, artist, lang, stateOrigin, stateTl, series, universe) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, Medium value) throws SQLException {
            statement.setInt(1, value.getMediumId());
            statement.setInt(2, value.getCurrentRead());
            statement.setString(3, value.getCountryOfOrigin());
            statement.setString(4, value.getLanguageOfOrigin());
            statement.setString(5, value.getAuthor());
            statement.setString(6, value.getTitle());
            statement.setInt(7, value.getMedium());
            statement.setString(8, value.getArtist());
            statement.setString(9, value.getLang());
            statement.setInt(10, value.getStateOrigin());
            statement.setInt(11, value.getStateTL());
            statement.setString(12, value.getSeries());
            statement.setString(13, value.getUniverse());
        }
    };

    void insert(Medium medium) {
        this.execute(medium, this.insertMediumQuery);
    }

    void insert(Collection<? extends Medium> media) {
        this.execute(media, this.insertMediumQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS medium (`mediumId` INTEGER NOT NULL, `currentRead` INTEGER, `countryOfOrigin` TEXT, `languageOfOrigin` TEXT, `author` TEXT, `title` TEXT, `medium` INTEGER NOT NULL, `artist` TEXT, `lang` TEXT, `stateOrigin` INTEGER NOT NULL, `stateTL` INTEGER NOT NULL, `series` TEXT, `universe` TEXT, PRIMARY KEY(`mediumId`), FOREIGN KEY(`currentRead`) REFERENCES `episode`(`episodeId`) ON UPDATE NO ACTION ON DELETE SET NULL )";
    }
}
