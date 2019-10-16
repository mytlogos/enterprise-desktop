package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.UserImpl;
import com.mytlogos.enterprisedesktop.model.User;
import io.reactivex.rxjava3.core.Single;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 */
class UserTable extends AbstractTable {

    private final PreparedQuery<User> insertUserQuery = new PreparedQuery<User>() {
        @Override
        public String getQuery() {
            return "INSERT OR REPLACE INTO user (uuid, session, name) VALUES (?,?,?)";
        }

        @Override
        public void setValues(PreparedStatement statement, User value) throws SQLException {
            statement.setString(1, value.getUuid());
            statement.setString(2, value.getSession());
            statement.setString(3, value.getName());
        }
    };

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
        try {
            return this.selectSingle("SELECT * FROM user", resultSet -> {
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                String session = resultSet.getString("session");
                return new UserImpl(uuid, session, name);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    void insert(User user) {
        this.execute(user, this.insertUserQuery);
    }

    Single<User> getUser() {
        return Single.create(emitter -> {
            try {
                final User user = this.selectSingle("SELECT * FROM user", resultSet -> {
                    String uuid = resultSet.getString("uuid");
                    String name = resultSet.getString("name");
                    String session = resultSet.getString("session");
                    return new UserImpl(uuid, session, name);
                });
                emitter.onSuccess(user);
            } catch (SQLException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    @Override
    String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS user (name TEXT, uuid TEXT, session TEXT, PRIMARY KEY(uuid))";
    }
}
