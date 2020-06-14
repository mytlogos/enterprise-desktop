package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.SimpleToc;
import com.mytlogos.enterprisedesktop.model.Toc;

import java.util.*;

/**
 *
 */
public class TocTable extends AbstractTable {
    private final QueryBuilder<Toc> insertTocQuery = new QueryBuilder<Toc>(
            "Insert ToC",
            "INSERT OR IGNORE INTO medium_toc (mediumId, link) VALUES (?,?)"
    ).setValueSetter((statement, toc) -> {
        statement.setInt(1, toc.getMediumId());
        statement.setString(2, toc.getLink());
    });
    private final QueryBuilder<Toc> deleteTocQuery = new QueryBuilder<Toc>(
            "Delete ToC",
            "DELETE FROM medium_toc WHERE (mediumId, link) = (?,?)"
    ).setValueSetter((statement, toc) -> {
        statement.setInt(1, toc.getMediumId());
        statement.setString(2, toc.getLink());
    });
    private final QueryBuilder<Map<Integer, Integer>> getTocStatsQuery = new QueryBuilder<Map<Integer, Integer>>(
            "Select TocStat",
            "SELECT mediumId, count(link) FROM medium_toc GROUP BY mediumId;"
    ).setConverter((statement) -> {
        Map<Integer, Integer> map = new HashMap<>();

        // query uses statement.next once
        map.put(statement.getInt(1), statement.getInt(2));

        while (statement.next()) {
            map.put(statement.getInt(1), statement.getInt(2));
        }
        return map;
    });
    private final QueryBuilder<Toc> getTocsQuery = new QueryBuilder<Toc>(
            "Select Tocs",
            "SELECT mediumId, link FROM medium_toc WHERE mediumId $?"
    ).setConverter((statement) -> new SimpleToc(statement.getInt(1), statement.getString(2)));

    TocTable() {
        super("medium_toc");
    }

    public List<Toc> getTocs(Collection<Integer> mediumIds) {
        return this.getTocsQuery.setQueryIn(mediumIds, QueryBuilder.Type.INT).queryListIgnoreError();
    }

    public void delete(List<Toc> tocs) {
        this.executeDMLQuery(tocs, this.deleteTocQuery);
    }

    void insert(Toc toc) {
        this.executeDMLQuery(Collections.singleton(toc), insertTocQuery);
    }

    void insert(Collection<? extends Toc> tocs) {
        this.executeDMLQuery(tocs, insertTocQuery);
    }

    Map<Integer, Integer> getStat() {
        final Map<Integer, Integer> map = this.getTocStatsQuery.query();
        return map != null ? map : Collections.emptyMap();
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS medium_toc (`mediumId` INTEGER NOT NULL, link TEXT NOT NULL, PRIMARY KEY(`mediumId`, `link`), FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE)";
    }
}
