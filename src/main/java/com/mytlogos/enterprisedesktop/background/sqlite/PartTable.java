package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.api.model.ClientPart;
import com.mytlogos.enterprisedesktop.model.Part;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 */
class PartTable extends AbstractTable {
    private final QueryBuilder<Part> insertPartQuery = new QueryBuilder<Part>(
            "Insert Part",
            "INSERT OR IGNORE INTO part (partId, mediumId, title, totalIndex, partialIndex, combiIndex) VALUES (?,?,?,?,?,?)", getManager()
    ).setValueSetter((statement, part) -> {
        statement.setInt(1, part.getPartId());
        statement.setInt(2, part.getMediumId());
        statement.setString(3, part.getTitle());
        statement.setInt(4, part.getTotalIndex());
        statement.setInt(5, part.getPartialIndex());
        statement.setDouble(6, part.getCombiIndex());
    });
    private final QueryBuilder<Integer> removeMediumPartQuery = new QueryBuilder<Integer>(
            "Delete MediumPart",
            "DELETE FROM part " +
                    "WHERE part.mediumId = ?", getManager()
    ).setValueSetter((statement, mediumId) -> statement.setInt(1, mediumId));;

    PartTable(ConnectionManager manager) {
        super("part", manager);
    }

    public void update(List<ClientPart> update) {
        final HashMap<String, Function<ClientPart, ?>> attrMap = new HashMap<>();
        attrMap.put("title", (StringProducer<ClientPart>) ClientPart::getTitle);
        attrMap.put("totalIndex", (IntProducer<ClientPart>) ClientPart::getTotalIndex);
        attrMap.put("partialIndex", (IntProducer<ClientPart>) ClientPart::getPartialIndex);
        attrMap.put("combiIndex", (DoubleProducer<ClientPart>) ClientPart::getCombiIndex);

        final Map<String, Function<ClientPart, ?>> keyExtractors = new HashMap<>();
        keyExtractors.put("partId", (IntProducer<ClientPart>) ClientPart::getPartId);
        this.update(update, "part", attrMap, keyExtractors);
    }

    public void removeMediumPart(int mediumId) {
        this.executeDMLQuery(mediumId, this.removeMediumPartQuery);
    }


    void insert(Part part) {
        this.executeDMLQuery(part, this.insertPartQuery);
    }

    void insert(Collection<? extends Part> parts) {
        this.executeDMLQuery(parts, this.insertPartQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS part " +
                "(" +
                "`partId` INTEGER NOT NULL, " +
                "`mediumId` INTEGER NOT NULL, " +
                "`title` TEXT, " +
                "`totalIndex` INTEGER NOT NULL, " +
                "`partialIndex` INTEGER NOT NULL, " +
                "`combiIndex` REAL NOT NULL, " +
                "PRIMARY KEY(`partId`), " +
                "FOREIGN KEY(`mediumId`) REFERENCES `medium`(`mediumId`) ON UPDATE NO ACTION ON DELETE CASCADE " +
                ")";
    }

    @Override
    String getLoadedQuery() {
        return "SELECT partId FROM part";
    }
}
