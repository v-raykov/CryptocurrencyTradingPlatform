package com.viktor.cryptocurrency.trading.platform.repository.util.queries;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public enum UserQueries {
    FIND_USER_BY_USERNAME(String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), fields.USERNAME.name)),
    SAVE_USER(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", getTableName(), fields.USERNAME.name, fields.PASSWORD.name)),
    RESET_USER_BY_ID("CALL reset_user(?)"),
    UPDATE_USER_BALANCE_BY_ID(String.format("UPDATE %s SET %s = %s + ? WHERE %s = ? ", getTableName(), fields.BALANCE.name, fields.BALANCE.name, fields.ID.name)),
    FIND_USER_BALANCE_BY_ID(String.format("SELECT %s FROM %s WHERE %s = ?", fields.BALANCE.name, getTableName(), fields.ID.name));

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

    public static BigDecimal mapBalance(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(fields.BALANCE.name);
    }

    private static String getTableName() {
        return "User";
    }
}