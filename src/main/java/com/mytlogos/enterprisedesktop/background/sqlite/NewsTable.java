package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.News;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class NewsTable extends AbstractTable {
    private final QueryBuilder<News> insertNewsQuery = new QueryBuilder<News>(
            "INSERT OR IGNORE INTO news (title, timeStamp, newsId, read, link) VALUES (?,?,?,?,?)"
    ).setValueSetter((statement, news) -> {
        statement.setString(1, news.getTitle());
        statement.setString(2, news.getTimeStampString());
        statement.setInt(3, news.getId());
        statement.setBoolean(4, news.isRead());
        statement.setString(5, news.getUrl());
    });

    void insert(News news) {
        this.executeDMLQuery(news, this.insertNewsQuery);
    }

    void insert(Collection<? extends News> news) {
        this.executeDMLQuery(news, this.insertNewsQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS news (`title` TEXT, `timeStamp` TEXT, `newsId` INTEGER NOT NULL, `read` INTEGER NOT NULL, `link` TEXT, PRIMARY KEY(`newsId`))";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT newsId FROM news";
    }
}
