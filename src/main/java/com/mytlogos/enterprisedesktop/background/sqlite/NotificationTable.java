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
    private final QueryBuilder<NotificationItem> insertNotificationItemQuery = new QueryBuilder<NotificationItem>(
            "INSERT OR IGNORE INTO notification (title, description, dateTime) VALUES (?,?,?)"
    ).setValueSetter((statement, notificationItem) -> {
        statement.setString(1, notificationItem.getTitle());
        statement.setString(2, notificationItem.getDescription());
        statement.setString(3, Formatter.isoFormat(notificationItem.getDatetime()));
    });

    NotificationTable() {
        super("notification");
    }

    void insert(NotificationItem item) {
        this.executeDMLQuery(item, this.insertNotificationItemQuery);
    }

    void insert(Collection<? extends NotificationItem> items) {
        this.executeDMLQuery(items, this.insertNotificationItemQuery);
    }
    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS `notification` (`title` TEXT NOT NULL, `description` TEXT NOT NULL, `dateTime` TEXT NOT NULL, PRIMARY KEY(`title`, `dateTime`))";
    }
}
