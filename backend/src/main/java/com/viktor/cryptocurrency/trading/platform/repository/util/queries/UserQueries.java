package com.viktor.cryptocurrency.trading.platform.repository.util.queries;

import com.viktor.cryptocurrency.trading.platform.model.domain.User;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public enum UserQueries {
    GET_USER_BY_USERNAME(String.format("SELECT * FROM %s WHERE %s = ?", fields.TABLE.name, fields.USERNAME.name)),
    SAVE_USER(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", fields.TABLE.name, fields.USERNAME.name, fields.PASSWORD.name));

    @Getter
    private enum fields {
        TABLE("User"),
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
}