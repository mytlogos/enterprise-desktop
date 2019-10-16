package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.NotificationItem;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
class NotificationTable extends AbstractTable {
    private final PreparedQuery<NotificationItem> insertNotificationItemQuery = new PreparedQuery<NotificationItem>() {
        @Override
        public String getQuery() {
            return "INSERT OR IGNORE INTO notification (title, description, dateTime) VALUES (?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, NotificationItem value) throws SQLException {
            statement.setString(1, value.getTitle());
            statement.setString(2, value.getDescription());
            statement.setString(3, Formatter.format(value.getDatetime()));
        }
    };

    void insert(NotificationItem item) {
        this.execute(item, this.insertNotificationItemQuery);
    }

    void insert(Collection<? extends NotificationItem> items) {
        this.execute(items, this.insertNotificationItemQuery);
    }
    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS `notification` (`title` TEXT NOT NULL, `description` TEXT NOT NULL, `dateTime` TEXT NOT NULL, PRIMARY KEY(`title`, `dateTime`))";
    }
}
