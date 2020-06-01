package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.EditEvent;

import java.util.Collection;

/**
 *
 */
class EditEventTable extends AbstractTable {
    private final QueryBuilder<EditEvent> insertEditEventQuery = new QueryBuilder<EditEvent>(
            "Insert EditEvent",
            "INSERT OR IGNORE INTO edit_event (id, objectType, eventType, dateTime, firstValue, secondValue) VALUES (?,?,?,?,?,?)"
    )
            .setValueSetter((preparedStatement, editEvent) -> {
                preparedStatement.setInt(1, editEvent.getId());
                preparedStatement.setInt(2, editEvent.getObjectType());
                preparedStatement.setInt(3, editEvent.getEventType());
                preparedStatement.setString(4, Formatter.isoFormat(editEvent.getDateTime()));
                preparedStatement.setString(5, editEvent.getFirstValue());
                preparedStatement.setString(6, editEvent.getSecondValue());
            });

    EditEventTable() {
        super("edit_event");
    }

    void insert(EditEvent value) {
        this.executeDMLQuery(value, this.insertEditEventQuery);
    }

    void insert(Collection<? extends EditEvent> value) {
        this.executeDMLQuery(value, this.insertEditEventQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS `edit_event` (`id` INTEGER NOT NULL, `objectType` INTEGER NOT NULL, `eventType` INTEGER NOT NULL, `dateTime` TEXT NOT NULL, `firstValue` TEXT, `secondValue` TEXT, PRIMARY KEY(`id`, `objectType`, `eventType`, `dateTime`))";
    }
}
