package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.News;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class NewsTable extends AbstractTable {
    private final PreparedQuery<News> insertNewsQuery = new PreparedQuery<News>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO news (title, timeStamp, newsId, read, link) VALUES (?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, News value) throws SQLException {
            statement.setString(1, value.getTitle());
            statement.setString(2, value.getTimeStampString());
            statement.setInt(3, value.getId());
            statement.setBoolean(4, value.isRead());
            statement.setString(5, value.getUrl());
        }
    };

    void insert(News news) {
        this.execute(news, this.insertNewsQuery);
    }

    void insert(Collection<? extends News> news) {
        this.execute(news, this.insertNewsQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS news (`title` TEXT, `timeStamp` TEXT, `newsId` INTEGER NOT NULL, `read` INTEGER NOT NULL, `link` TEXT, PRIMARY KEY(`newsId`))";
    }
}
