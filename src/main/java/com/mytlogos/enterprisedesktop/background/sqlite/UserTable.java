package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.User;
import com.mytlogos.enterprisedesktop.model.UserImpl;

import java.sql.SQLException;

/**
 *
 */
class UserTable extends AbstractTable {

    private final QueryBuilder<User> insertUserQuery = new QueryBuilder<User>(
            "INSERT OR REPLACE INTO user (uuid, session, name) VALUES (?,?,?)"
    ).setValueSetter((statement, user) -> {
        statement.setString(1, user.getUuid());
        statement.setString(2, user.getSession());
        statement.setString(3, user.getName());
    });

    private final QueryBuilder<User> getUserQuery = new QueryBuilder<User>("SELECT * FROM user")
            .setConverter(resultSet -> {
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                String session = resultSet.getString("session");
                return new UserImpl(uuid, session, name);
            });

    public void update(User user) {

    }

    void deleteAllUser() {
        try {
            this.executeQuery("DELETE FROM user");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    User getUserNow() {
        return this.getUserQuery.query();
    }

    void insert(User user) {
        this.executeDMLQuery(user, this.insertUserQuery);
    }

    User getUser() {
        return this.getUserQuery.query();
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS user (name TEXT, uuid TEXT, session TEXT, PRIMARY KEY(uuid))";
    }
}
