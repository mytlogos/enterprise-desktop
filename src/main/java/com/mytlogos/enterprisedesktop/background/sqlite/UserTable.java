package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.User;
import com.mytlogos.enterprisedesktop.model.UserImpl;

import java.sql.SQLException;

/**
 *
 */
class UserTable extends AbstractTable {

    private final QueryBuilder<User> insertUserQuery = new QueryBuilder<User>(
            "Insert User",
            "INSERT OR REPLACE INTO user (uuid, session, name) VALUES (?,?,?)", getManager()
    ).setValueSetter((statement, user) -> {
        statement.setString(1, user.getUuid());
        statement.setString(2, user.getSession());
        statement.setString(3, user.getName());
    });

    private final QueryBuilder<User> getUserQuery = new QueryBuilder<User>("Select User","SELECT * FROM user", getManager())
            .setConverter(resultSet -> {
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                String session = resultSet.getString("session");
                return new UserImpl(uuid, session, name);
            });

    UserTable(ConnectionManager manager) {
        super("user", manager);
    }

    public void update(User user) {
        System.out.println("updating user");
    }

    void deleteAllUser() {
        try {
            this.executeDMLQuery("DELETE FROM user");
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

    LiveData<User> getUser() {
        return this.getUserQuery.queryLiveData();
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS user (name TEXT, uuid TEXT, session TEXT, PRIMARY KEY(uuid))";
    }
}
