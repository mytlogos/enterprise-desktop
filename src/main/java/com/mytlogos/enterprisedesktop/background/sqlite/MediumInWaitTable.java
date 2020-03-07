package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.MediumInWait;
import com.mytlogos.enterprisedesktop.model.MediumInWaitImpl;
import com.mytlogos.enterprisedesktop.tools.Sorting;

import java.util.Collection;
import java.util.List;

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

    MediumInWaitTable() {
        super("medium_in_wait");
    }

    public LiveData<PagedList<MediumInWait>> get(String filter, int mediumFilter, String hostFilter, Sorting sortings) {
        return new QueryBuilder<MediumInWait>(
                "SELECT title, medium, link FROM medium_in_wait " +
                        "WHERE (title IS NULL OR INSTR(lower(title), ?) > 0) " +
                        "AND (? = 0 OR (medium & ?) > 0) " +
                        "AND (link IS NULL OR INSTR(link, ?) > 0) " +
                        "ORDER BY title ASC;"
        )
                .setDependencies(
                        MediumInWaitTable.class
                )
                .setValues((statement) -> {
                    statement.setString(1, filter);
                    statement.setInt(2, mediumFilter);
                    statement.setInt(3, mediumFilter);
                    statement.setString(4, hostFilter);
                })
                .setConverter(value -> new MediumInWaitImpl(
                        value.getString(1),
                        value.getInt(2),
                        value.getString(3)
                ))
                .queryLiveDataList()
                .map(PagedList::new);
    }

    public List<MediumInWait> getSimilar(MediumInWait mediumInWait) {
        return new QueryBuilder<MediumInWait>("SELECT title, medium, link FROM medium_in_wait WHERE INSTR(title, ?) > 0 AND medium=?")
                .setValues(value -> {
                    value.setString(1, mediumInWait.getTitle());
                    value.setInt(2, mediumInWait.getMedium());
                })
                .setConverter(value -> new MediumInWaitImpl(
                        value.getString(1),
                        value.getInt(2),
                        value.getString(3)
                ))
                .queryListIgnoreError();
    }

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
