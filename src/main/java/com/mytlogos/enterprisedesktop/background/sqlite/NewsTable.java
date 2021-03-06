package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.api.model.ClientPureNews;
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
            "INSERT OR IGNORE INTO news (title, timeStamp, newsId, read, link) VALUES (?,?,?,?,?)", getManager()
    ).setValueSetter((statement, news) -> {
        statement.setString(1, news.getTitle());
        statement.setString(2, news.getTimeStampString());
        statement.setInt(3, news.getId());
        statement.setBoolean(4, news.isRead());
        statement.setString(5, news.getUrl());
    });

    NewsTable(ConnectionManager manager) {
        super("news", manager);
    }

    public void update(List<ClientPureNews> update) {
        final HashMap<String, Function<ClientPureNews, ?>> attrMap = new HashMap<>();
        attrMap.put("title", (StringProducer<ClientPureNews>) ClientPureNews::getTitle);
        attrMap.put("timeStamp", (StringProducer<ClientPureNews>) ClientPureNews::getTimeStampString);
        attrMap.put("read", (BooleanProducer<ClientPureNews>) ClientPureNews::isRead);
        attrMap.put("link", (StringProducer<ClientPureNews>) ClientPureNews::getUrl);

        final Map<String, Function<ClientPureNews, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("newsId", (IntProducer<ClientPureNews>) ClientPureNews::getId);
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
