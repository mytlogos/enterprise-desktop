package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.EditEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class EditEventTable extends AbstractTable {
    private final PreparedQuery<EditEvent> insertEditEventQuery = new PreparedQuery<EditEvent>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO edit_event (id, objectType, eventType, dateTime, firstValue, secondValue) VALUES (?,?,?,?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, EditEvent value) throws SQLException {
            statement.setInt(1, value.getId());
            statement.setInt(2, value.getObjectType());
            statement.setInt(3, value.getEventType());
            statement.setString(4, Formatter.format(value.getDateTime()));
            statement.setString(5, value.getFirstValue());
            statement.setString(6, value.getSecondValue());
        }
    };
    void insert(EditEvent editEvent) {
        this.execute(editEvent, this.insertEditEventQuery);
    }

    void insert(Collection<? extends EditEvent> editEvents) {
        this.execute(editEvents, this.insertEditEventQuery);
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS `edit_event` (`id` INTEGER NOT NULL, `objectType` INTEGER NOT NULL, `eventType` INTEGER NOT NULL, `dateTime` TEXT NOT NULL, `firstValue` TEXT, `secondValue` TEXT, PRIMARY KEY(`id`, `objectType`, `eventType`, `dateTime`))";
    }
}
