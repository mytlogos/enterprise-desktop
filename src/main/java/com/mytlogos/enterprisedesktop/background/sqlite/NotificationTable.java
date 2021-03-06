package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.NotificationItem;

import java.util.Collection;

/**
 *
 */
class NotificationTable extends AbstractTable {
    private final QueryBuilder<NotificationItem> insertNotificationItemQuery = new QueryBuilder<NotificationItem>(
            "Insert Notification",
            "INSERT OR IGNORE INTO notification (title, description, dateTime) VALUES (?,?,?)", getManager()
    ).setValueSetter((statement, notificationItem) -> {
        statement.setString(1, notificationItem.getTitle());
        statement.setString(2, notificationItem.getDescription());
        statement.setString(3, Formatter.isoFormat(notificationItem.getDatetime()));
    });

    NotificationTable(ConnectionManager manager) {
        super("notification", manager);
    }

    void insert(NotificationItem item) {
        this.executeDMLQuery(item, this.insertNotificationItemQuery);
    }

    void insert(Collection<? extends NotificationItem> items) {
        this.executeDMLQuery(items, this.insertNotificationItemQuery);
    }
    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS `notification` " +
                "(" +
                "`title` TEXT NOT NULL, " +
                "`description` TEXT NOT NULL, " +
                "`dateTime` TEXT NOT NULL, " +
                "PRIMARY KEY(`title`, `dateTime`)" +
                ")";
    }
}
