package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.api.model.ClientNews;
import com.mytlogos.enterprisedesktop.model.News;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 */
class NewsTable extends AbstractTable {
    private final QueryBuilder<News> insertNewsQuery = new QueryBuilder<News>(
            "Insert News",
            "INSERT OR IGNORE INTO news (title, timeStamp, newsId, read, link) VALUES (?,?,?,?,?)"
    ).setValueSetter((statement, news) -> {
        statement.setString(1, news.getTitle());
        statement.setString(2, news.getTimeStampString());
        statement.setInt(3, news.getId());
        statement.setBoolean(4, news.isRead());
        statement.setString(5, news.getUrl());
    });

    NewsTable() {
        super("news");
    }

    public void update(List<ClientNews> update) {
        final HashMap<String, Function<ClientNews, ?>> attrMap = new HashMap<>();
        attrMap.put("title", (StringProducer<ClientNews>) ClientNews::getTitle);
        attrMap.put("timeStamp", (StringProducer<ClientNews>) ClientNews::getTimeStampString);
        attrMap.put("read", (BooleanProducer<ClientNews>) ClientNews::isRead);
        attrMap.put("link", (StringProducer<ClientNews>) ClientNews::getUrl);

        final Map<String, Function<ClientNews, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("newsId", (IntProducer<ClientNews>) ClientNews::getId);
        this.update(update, "news", attrMap, keyExtractors);
    }

    void insert(News news) {
        this.executeDMLQuery(news, this.insertNewsQuery);
    }

    void insert(Collection<? extends News> news) {
        this.executeDMLQuery(news, this.insertNewsQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS news " +
                "(" +
                "`title` TEXT, " +
                "`timeStamp` TEXT, " +
                "`newsId` INTEGER NOT NULL, " +
                "`read` INTEGER NOT NULL, " +
                "`link` TEXT, PRIMARY KEY(`newsId`)" +
                ")";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT newsId FROM news";
    }
}
