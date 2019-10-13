package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.model.User;
import io.reactivex.rxjava3.core.Single;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
class UserTable extends AbstractTable {

    void insertUser(User user) {
        try (Connection connection = this.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO user (uuid, session, name) VALUES (?,?,?)")) {
                statement.setString(1, user.getUuid());
                statement.setString(2, user.getSession());
                statement.setString(3, user.getName());
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Single<User> getUser() {
        return Single.create(emitter -> {
            try (Connection connection = this.getConnection(); ResultSet set = connection.createStatement().executeQuery("SELECT * FROM user")) {
                User user = null;

                if (set.next()) {
                    String uuid = set.getString("uuid");
                    String name = set.getString("name");
                    String session = set.getString("session");
                    user = new User(uuid, session, name);
                }
                emitter.onSuccess(user);
            } catch (SQLException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    @Override
    void initialize() {
        try (Connection connection = this.getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS user (name TEXT, uuid TEXT, session TEXT, PRIMARY KEY(uuid))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
