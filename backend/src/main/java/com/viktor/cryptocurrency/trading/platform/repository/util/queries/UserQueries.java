package com.viktor.cryptocurrency.trading.platform.repository.util.queries;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public enum UserQueries {
    GET_USER_BY_USERNAME(String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), fields.USERNAME.name)),
    SAVE_USER(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", getTableName(), fields.USERNAME.name, fields.PASSWORD.name)),
    RESET_USER_BALANCE(String.format("UPDATE %s SET %s = 0 WHERE %s = ? ", getTableName(), fields.BALANCE.name, fields.ID.name));

    private enum fields {
        ID("user_id"),
        USERNAME("username"),
        PASSWORD("password"),
        BALANCE("balance");
        private final String name;

        fields(String name) {
            this.name = name;
        }
    }

    private final String query;

    UserQueries(String query) {
        this.query = query;
    }

    public static User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong(fields.ID.name),
                rs.getString(fields.USERNAME.name),
                rs.getString(fields.PASSWORD.name),
                rs.getBigDecimal(fields.BALANCE.name));
    }

    private static String getTableName() {
        return "User";
    }
}